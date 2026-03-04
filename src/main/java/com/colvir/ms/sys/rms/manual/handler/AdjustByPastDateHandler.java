package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AdjustByPastDateDto;
import com.colvir.ms.sys.rms.dto.AdjustByPastDateJournalDto;
import com.colvir.ms.sys.rms.dto.AdjustByPastDateResultDto;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.BbpStateResult;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentDto;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentResponse;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.service.RequirementPaymentService;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.antlr.v4.runtime.misc.Pair;
import org.jboss.logging.Logger;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.colvir.ms.sys.rms.manual.constant.RmsConstants.SYS_RMS_REQUIREMENT_NAMESPACE;
import static com.colvir.ms.sys.rms.manual.constant.RmsConstants.UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH;

@ApplicationScoped
public class AdjustByPastDateHandler extends AbstractStepRunnerHandler<AdjustByPastDateDto, AdjustByPastDateJournalDto, AdjustByPastDateResultDto> {

    RequirementService requirementService;
    RequirementPaymentService paymentService;
    StepCreatorService stepCreatorService;

    @Inject
    public AdjustByPastDateHandler(RequirementService requirementService,
                                   RequirementPaymentService paymentService,
                                   StepCreatorService stepCreatorService,
                                   Logger log) {
        super(StepsNames.SYS_RMS_ADJUST_BY_PAST_DATE, log);
        this.requirementService = requirementService;
        this.paymentService = paymentService;
        this.stepCreatorService = stepCreatorService;
    }

    @Override
    public void validateProperties(AdjustByPastDateDto properties) {
        // Проверяем, что все действия UPDATE
        if (properties.requirements.stream().anyMatch(req -> !RequirementAction.UPDATE.equals(req.action))) {
            log.errorf("adjustByPastDate: invalid requirement action detected, expected only UPDATE: %s", properties.requirements);
            throw new RuntimeException("Incorrect Requirements, RequirementAction should be UPDATE: " + properties.requirements);
        }
    }

    @Override
    public AggregationResult<AdjustByPastDateDto, AdjustByPastDateJournalDto, AdjustByPastDateResultDto> process(StepMethod.RequestItem.Request<AdjustByPastDateDto, AdjustByPastDateJournalDto> request) {
        AdjustByPastDateResultDto result = new AdjustByPastDateResultDto();
        AdjustByPastDateJournalDto journal = request.getJournal();
        AdjustByPastDateDto properties = request.getProperties();

        if (journal.isFirstRun) {

            log.infof("Step %s is first run: %s", StepsNames.SYS_RMS_ADJUST_BY_PAST_DATE, true);
            journal.isFirstRun = false;

            if (properties.requirements == null || properties.requirements.isEmpty()) {
                log.infof("adjustByPastDate: empty requirements list, returning unchanged result");
                return new AggregationResult<>(journal, List.of());
            }

            List<Pair<RequirementStateInfoDto, Requirement>> requirementsWithEntities = mapRequirementsWithEntities(properties.requirements);
            log.infof("adjustByPastDate: starting to process requirements %s", requirementsWithEntities);

            Map<Long, Pair<RequirementStateInfoDto, Requirement>> increasingRequirements = requirementsWithEntities.stream()
                .filter(pair -> pair.a.payedAmount.compareTo(pair.b.paidAmount) > 0)
                .collect(Collectors.toMap(pair -> pair.b.id, pair -> pair, (left, right) -> right, HashMap::new));

            // Сначала перераспределяем то, что уже есть в БД, даже если на вход не пришли платежи.
            // Это важно, потому что требование могло измениться только по сумме, а движения по платежам нет.
            paymentService.redistributeExistingRequirementPayments(requirementsWithEntities, journal, result);

            log.infof("adjustByPastDate: increasing requirements=%s", increasingRequirements);

            // После перераспределения выполняем этап возвратов (если есть исходящие платежи)
            // и затем финально приводим требования к целевому входному состоянию.
            log.infof("adjustByPastDate: processing outgoing payments=%s", properties.outgoingPayments);
            paymentService.processRefundingPayment(properties.outgoingPayments, requirementsWithEntities, journal, result);

            // Обрабатываем входящие платежи
            if (properties.incomingPayments != null && !properties.incomingPayments.isEmpty() ) {
                log.infof("adjustByPastDate: processing %d incoming payments", properties.incomingPayments.size());

                Set<String> incomingPaymentPpcs = properties.incomingPayments.stream()
                    .map(payment -> payment.paymentPurposeCode)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

                RegistrationOfPaymentDto registrationRequest = new RegistrationOfPaymentDto();
                registrationRequest.payments = properties.incomingPayments;
                registrationRequest.requirements = increasingRequirements.values().stream()
                    .filter(pair -> pair.a.paymentPurposeCode != null && incomingPaymentPpcs.contains(pair.a.paymentPurposeCode))
                    .sorted(Comparator.comparingInt(pair -> pair.a.priority))
                    .map(pair -> new ReferenceDto(pair.b.id, SYS_RMS_REQUIREMENT_NAMESPACE))
                    .toList();

                log.infof("adjustByPastDate: registering incoming payments, requirements count=%d, payments count=%d",
                    registrationRequest.requirements.size(), registrationRequest.payments.size());

                if (!registrationRequest.requirements.isEmpty()) {
                    RegistrationOfPaymentResponse registrationResponse = paymentService.registrationOfPayment(registrationRequest);
                    log.infof("adjustByPastDate: registration completed. Created payments: %d, related payments: %d",
                        registrationResponse.journal.createdPayments.size(), registrationResponse.journal.createdRelatedPayments.size());

                    journal.paymentIds.addAll(registrationResponse.journal.createdPayments);
                    journal.relatedPaymentIds.addAll(registrationResponse.journal.createdRelatedPayments);

                    registrationResponse.requirements.forEach(updatedRequirement -> {
                        RequirementStateInfoDto reqInfoResult = increasingRequirements.get(updatedRequirement.id).a;
                        reqInfoResult.status = updatedRequirement.state;
                        reqInfoResult.payedAmount = updatedRequirement.paidAmount;
                        reqInfoResult.amount = updatedRequirement.amount;
                        reqInfoResult.paymentEndDate = updatedRequirement.paymentEndDate;
                        reqInfoResult.currentTransactionAmount = registrationResponse.currentTransactionAmounts.get(reqInfoResult.requirementId);
                    });
                }
            }

            requirementsWithEntities.forEach(pair -> {
                RequirementStateInfoDto sourceReq = pair.a;
                Requirement currentRequirement = pair.b;
                RequirementJournalDto requirementJournal = journal.requirementJournalMap.get(currentRequirement.id);
                if (requirementJournal == null) {
                    return;
                }

                boolean isChanged = currentRequirement.amount.compareTo(requirementJournal.amount) != 0
                    || currentRequirement.paidAmount.compareTo(requirementJournal.paidAmount) != 0
                    || currentRequirement.unpaidAmount.compareTo(requirementJournal.unpaidAmount) != 0
                    || !Objects.equals(currentRequirement.state, requirementJournal.state)
                    || !Objects.equals(currentRequirement.paymentEndDate, requirementJournal.paymentEndDate);

                if (!isChanged) {
                    return;
                }

                RequirementStateInfoDto reqInfoResult = new RequirementStateInfoDto();
                reqInfoResult.requirementId = currentRequirement.id;
                reqInfoResult.amount = currentRequirement.amount;
                reqInfoResult.payedAmount = currentRequirement.paidAmount;
                reqInfoResult.status = currentRequirement.state;
                reqInfoResult.paymentEndDate = currentRequirement.paymentEndDate;
                reqInfoResult.priority = sourceReq.priority;
                reqInfoResult.indicator = sourceReq.indicator;
                reqInfoResult.action = sourceReq.action;
                reqInfoResult.paymentPurposeCode = sourceReq.paymentPurposeCode;
                reqInfoResult.currentTransactionAmount = sourceReq.currentTransactionAmount;
                putRequirementResult(result, reqInfoResult);
            });

            log.infof("adjustByPastDate: final state of requirements size=%d : %s", result.requirements.size(), result.requirements);
            log.infof("adjustByPastDate: state of requirements journal size=%d : %s", journal.requirementJournalMap.size(), journal.requirementJournalMap.values());

            journal.intermediateResult = result;

            return new AggregationResult<>(journal, result,
                List.of(
                    stepCreatorService.createSysBbpPackStateSubStep(
                        result.requirements.stream().map(
                            newReqState -> new Pair<>(journal.requirementJournalMap.get(newReqState.requirementId), newReqState)
                        ).toList()
                    )
                )
            );

        } else {

            Map<String, BbpStateResult> bbpPackStateResult =
                ContextObjectMapper.get().convertValue(
                    request.getContextMapper()
                        .findNode(UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH),
                    new TypeReference<>() {}
                );

            requirementService.updateRequirementBbpStates(
                journal.requirementJournalMap.values().stream().toList(),
                bbpPackStateResult
            );

            return new AggregationResult<>(properties, journal, journal.intermediateResult);
        }
    }

    private void putRequirementResult(AdjustByPastDateResultDto result, RequirementStateInfoDto requirementStateInfo) {
        if (requirementStateInfo == null || requirementStateInfo.requirementId == null) {
            return;
        }

        for (int i = 0; i < result.requirements.size(); i++) {
            RequirementStateInfoDto existing = result.requirements.get(i);
            if (Objects.equals(existing.requirementId, requirementStateInfo.requirementId)) {
                result.requirements.set(i, requirementStateInfo);
                return;
            }
        }
        result.requirements.add(requirementStateInfo);
    }

    @Override
    public void undo(AdjustByPastDateJournalDto journal) {
        log.infof("adjustByPastDateUndo:\n%s", journal);
        paymentService.undoRedistributedRelatedPayments(journal.redistributedRelatedPayments);
        paymentService.undoRedistributedRefundingPayments(journal.redistributedRefundingPayments);

        requirementService.restoreWithoutBbpCancelExecution(
            journal.requirementJournalMap.values().stream().toList(),
            journal.paymentIds, journal.relatedPaymentIds,
            journal.refundingPaymentIds, journal.requirementRefundingPaymentIds
        );
    }

    private List<Pair<RequirementStateInfoDto, Requirement>> mapRequirementsWithEntities(List<RequirementStateInfoDto> requirements) {
        Map<Long, Requirement> dbRequirements = requirementService.getRequirementsByIds(
                requirements.stream().map(req -> req.requirementId).collect(Collectors.toSet())
            ).stream()
            .collect(Collectors.toMap(req -> req.id, req -> req));

        List<Pair<RequirementStateInfoDto, Requirement>> requirementsWithEntities = requirements.stream()
            .map(req -> new Pair<>(req, dbRequirements.get(req.requirementId)))
            .toList();

        if (requirementsWithEntities.stream().map(pair -> pair.b).anyMatch(Objects::isNull)) {
            throw new RuntimeException("One or more requirements were not found in DB for adjustByPastDate");
        }
        return requirementsWithEntities;
    }
}
