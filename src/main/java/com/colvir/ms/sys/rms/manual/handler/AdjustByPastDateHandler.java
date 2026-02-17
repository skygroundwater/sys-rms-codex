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
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            List<RequirementStateInfoDto> newRequirements = new ArrayList<>(properties.requirements);

            log.infof("adjustByPastDate: starting to process requirements %s", newRequirements);

            List<Pair<RequirementStateInfoDto, ReferenceDto>> decreasingRequirements = new ArrayList<>();

            Map<Long, Pair<RequirementStateInfoDto, ReferenceDto>> increasingRequirements = new HashMap<>();

            List<RequirementStateInfoDto> zeroChangeRequirements = new ArrayList<>();

            // Загружаем текущие требования из БД и заполняем журнал
            newRequirements.forEach(
                req -> {
                    if (req.payedAmount.compareTo(req.amount) > 0) {
                        decreasingRequirements.add(new Pair<>(req, new ReferenceDto(req.requirementId, SYS_RMS_REQUIREMENT_NAMESPACE)));
                    } else if (req.payedAmount.compareTo(req.amount) < 0) {
                        increasingRequirements.put(req.requirementId, new Pair<>(req, new ReferenceDto(req.requirementId, SYS_RMS_REQUIREMENT_NAMESPACE)));
                    } else {
                        zeroChangeRequirements.add(req);
                    }
                }
            );

            log.infof("adjustByPastDate: decreasing requirements=%s", decreasingRequirements);
            log.infof("adjustByPastDate: increasing requirements=%s", increasingRequirements);
            log.infof("adjustByPastDate: zero change requirements=%s", zeroChangeRequirements);

            // Обрабатываем исходящие платежи
            if (!decreasingRequirements.isEmpty()) {
                log.infof("adjustByPastDate: processing outgoing payments=%s", properties.outgoingPayments);
                paymentService.processRefundingPayment(properties.outgoingPayments, decreasingRequirements, journal, result);
            }

            // Обрабатываем входящие платежи
            if (properties.incomingPayments != null && !properties.incomingPayments.isEmpty() ) {
                log.infof("adjustByPastDate: processing %d incoming payments", properties.incomingPayments.size());

                RegistrationOfPaymentDto registrationRequest = new RegistrationOfPaymentDto();
                registrationRequest.payments = properties.incomingPayments;
                registrationRequest.requirements = increasingRequirements.values().stream()
                    .sorted(Comparator.comparingInt(pair -> pair.a.priority))
                    .map(pair -> pair.b)
                    .toList();

                log.infof("adjustByPastDate: registering incoming payments, requirements count=%d, payments count=%d",
                    registrationRequest.requirements.size(), registrationRequest.payments.size());

                RegistrationOfPaymentResponse registrationResponse = paymentService.registrationOfPayment(registrationRequest);
                log.infof("adjustByPastDate: registration completed. Created payments: %d, related payments: %d",
                    registrationResponse.journal.createdPayments.size(), registrationResponse.journal.createdRelatedPayments.size());

                registrationResponse.journal.requirementJournal.forEach(
                    reqJournal -> journal.requirementJournalMap.put(reqJournal.id, reqJournal)
                );
                journal.paymentIds.addAll(registrationResponse.journal.createdPayments);
                journal.relatedPaymentIds.addAll(registrationResponse.journal.createdRelatedPayments);

                result.requirements.addAll(
                    registrationResponse.requirements.stream()
                        .map(updatedRequirement -> {
                            RequirementStateInfoDto reqInfoResult = increasingRequirements.get(updatedRequirement.id).a;
                            reqInfoResult.status = updatedRequirement.state;
                            reqInfoResult.payedAmount = updatedRequirement.paidAmount;
                            reqInfoResult.amount = updatedRequirement.amount;
                            reqInfoResult.paymentEndDate = updatedRequirement.paymentEndDate;
                            reqInfoResult.currentTransactionAmount = registrationResponse.currentTransactionAmounts.get(reqInfoResult.requirementId);
                            return reqInfoResult;
                        }).toList()
                );
            }

            result.requirements.addAll(zeroChangeRequirements);

            log.infof("adjustByPastDate: final state of requirements =%s", result.requirements);
            log.infof("adjustByPastDate: state of requirements journal =%s", journal.requirementJournalMap.values());

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

    @Override
    public void undo(AdjustByPastDateJournalDto journal) {
        log.infof("adjustByPastDateUndo:\n%s", journal);
        requirementService.restoreWithoutBbpCancelExecution(
            journal.requirementJournalMap.values().stream().toList(),
            journal.paymentIds, journal.relatedPaymentIds,
            journal.refundingPaymentIds, journal.requirementRefundingPaymentIds
        );
    }
}
