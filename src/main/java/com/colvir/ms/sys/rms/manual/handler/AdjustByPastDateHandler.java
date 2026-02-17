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
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.service.RequirementPaymentService;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.colvir.ms.sys.rms.manual.util.RequirementMapperUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.antlr.v4.runtime.misc.Pair;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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


            List<Pair<RequirementStateInfoDto, ReferenceDto>> overpaidRequirements = new ArrayList<>();
            List<Pair<RequirementStateInfoDto, ReferenceDto>> underpaidRequirements = new ArrayList<>();
            List<Pair<RequirementStateInfoDto, ReferenceDto>> equalRequirements = new ArrayList<>();

            for (RequirementStateInfoDto req : properties.requirements) {
                Requirement dbRequirement = requirementService.getRequirementById(req.requirementId);
                journal.requirementJournalMap.putIfAbsent(dbRequirement.id, RequirementMapperUtils.fillRequirementJournal(dbRequirement));
                BigDecimal dbPaidAmount = Objects.requireNonNullElse(dbRequirement.paidAmount, BigDecimal.ZERO);
                BigDecimal newAmount = Objects.requireNonNullElse(req.amount, BigDecimal.ZERO);

                Pair<RequirementStateInfoDto, ReferenceDto> reqRef = new Pair<>(req, new ReferenceDto(req.requirementId, SYS_RMS_REQUIREMENT_NAMESPACE));
                if (dbPaidAmount.compareTo(newAmount) > 0) {
                    overpaidRequirements.add(reqRef);
                } else if (dbPaidAmount.compareTo(newAmount) < 0) {
                    underpaidRequirements.add(reqRef);
                } else {
                    equalRequirements.add(reqRef);
                }
            }

            List<Pair<RequirementStateInfoDto, ReferenceDto>> refundCandidates = new ArrayList<>(overpaidRequirements);
            refundCandidates.addAll(underpaidRequirements);
            refundCandidates.addAll(equalRequirements);

            if (properties.outgoingPayments != null && !properties.outgoingPayments.isEmpty()) {
                log.infof("adjustByPastDate: processing outgoing payments=%s", properties.outgoingPayments);
                paymentService.processRefundingPayment(properties.outgoingPayments, refundCandidates, journal, result);
            }

            Map<Long, Pair<RequirementStateInfoDto, ReferenceDto>> increasingRequirements = new HashMap<>();
            for (RequirementStateInfoDto req : properties.requirements) {
                Requirement dbRequirement = requirementService.getRequirementById(req.requirementId);
                BigDecimal dbPaidAmount = Objects.requireNonNullElse(dbRequirement.paidAmount, BigDecimal.ZERO);
                BigDecimal newAmount = Objects.requireNonNullElse(req.amount, BigDecimal.ZERO);
                if (dbPaidAmount.compareTo(newAmount) < 0) {
                    increasingRequirements.put(req.requirementId, new Pair<>(req, new ReferenceDto(req.requirementId, SYS_RMS_REQUIREMENT_NAMESPACE)));
                }
            }

            if (properties.incomingPayments != null && !properties.incomingPayments.isEmpty() && !increasingRequirements.isEmpty()) {
                RegistrationOfPaymentDto registrationRequest = new RegistrationOfPaymentDto();
                registrationRequest.payments = properties.incomingPayments;
                registrationRequest.requirements = increasingRequirements.values().stream()
                    .sorted(Comparator.comparingInt(pair -> Objects.requireNonNullElse(pair.a.priority, Integer.MAX_VALUE)))
                    .map(pair -> pair.b)
                    .toList();

                RegistrationOfPaymentResponse registrationResponse = paymentService.registrationOfPayment(registrationRequest);

                registrationResponse.journal.requirementJournal.forEach(reqJournal -> journal.requirementJournalMap.put(reqJournal.id, reqJournal));
                journal.paymentIds.addAll(registrationResponse.journal.createdPayments);
                journal.relatedPaymentIds.addAll(registrationResponse.journal.createdRelatedPayments);

                result.requirements.addAll(
                    registrationResponse.requirements.stream().map(updatedRequirement -> {
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

            for (RequirementStateInfoDto req : properties.requirements) {
                if (result.requirements.stream().noneMatch(r -> Objects.equals(r.requirementId, req.requirementId))) {
                    Requirement dbRequirement = requirementService.getRequirementById(req.requirementId);
                    req.payedAmount = Objects.requireNonNullElse(dbRequirement.paidAmount, BigDecimal.ZERO);
                    req.status = dbRequirement.state == null ? RequirementStatus.WAIT : dbRequirement.state;
                    req.paymentEndDate = dbRequirement.paymentEndDate;
                    req.currentTransactionAmount = BigDecimal.ZERO;
                    result.requirements.add(req);
                }
            }

            log.infof("adjustByPastDate: final state of requirements =%s", result.requirements);

            journal.intermediateResult = result;

            return new AggregationResult<>(journal, result,
                List.of(stepCreatorService.createSysBbpPackStateSubStep(
                    result.requirements.stream()
                        .map(newReqState -> new Pair<>(journal.requirementJournalMap.get(newReqState.requirementId), newReqState))
                        .toList()
                ))
            );
        }

        Map<String, BbpStateResult> bbpPackStateResult =
            ContextObjectMapper.get().convertValue(
                request.getContextMapper().findNode(UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH),
                new TypeReference<>() {}
            );

        requirementService.updateRequirementBbpStates(journal.requirementJournalMap.values().stream().toList(), bbpPackStateResult);

        return new AggregationResult<>(properties, journal, journal.intermediateResult);
    }

    @Override
    public void undo(AdjustByPastDateJournalDto journal) {
        log.infof("adjustByPastDateUndo:\n%s", journal);
        requirementService.restoreWithoutBbpCancelExecution(
            journal.requirementJournalMap.values().stream().toList(),
            journal.paymentIds,
            journal.relatedPaymentIds,
            journal.refundingPaymentIds,
            journal.requirementRefundingPaymentIds
        );
    }
}
