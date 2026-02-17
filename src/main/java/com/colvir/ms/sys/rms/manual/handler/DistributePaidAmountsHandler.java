package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.BbpStateResult;
import com.colvir.ms.sys.rms.dto.DistributePaidAmountsDto;
import com.colvir.ms.sys.rms.dto.DistributePaidAmountsJournalDto;
import com.colvir.ms.sys.rms.dto.DistributePaidAmountsResultDto;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentDto;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentResponse;
import com.colvir.ms.sys.rms.dto.RequirementIndicatorDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementDTO;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.service.RequirementPaymentService;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Functions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.antlr.v4.runtime.misc.Pair;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.colvir.ms.sys.rms.manual.constant.RmsConstants.SYS_RMS_REQUIREMENT_NAMESPACE;
import static com.colvir.ms.sys.rms.manual.constant.RmsConstants.UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH;

@ApplicationScoped
public class DistributePaidAmountsHandler extends AbstractStepRunnerHandler<DistributePaidAmountsDto, DistributePaidAmountsJournalDto, DistributePaidAmountsResultDto> {

    RequirementService requirementService;
    RequirementPaymentService paymentService;
    StepCreatorService stepCreatorService;

    @Inject
    public DistributePaidAmountsHandler(RequirementService requirementService,
                                        RequirementPaymentService paymentService,
                                        StepCreatorService stepCreatorService,
                                        Logger log) {
        super(StepsNames.SYS_RMS_DISTRIBUTE_PAID_AMOUNTS, log);
        this.requirementService = requirementService;
        this.paymentService = paymentService;
        this.stepCreatorService = stepCreatorService;
    }

    @Override
    public void validateProperties(DistributePaidAmountsDto properties) {
        // что делать с требованием = сохранить
        List<RequirementStateInfoDto> incorrectRequirementData = properties.requirements.stream()
            .filter(r -> !RequirementAction.SAVE.equals(r.action) || r.requirementId == null)
            .toList();
        if (!incorrectRequirementData.isEmpty()) {
            throw new RuntimeException(String.format("Incorrect Requirements: %s", incorrectRequirementData));
        }
        // общая сумма по атрибуту "оплаченная сумма" из массива "исполненные платежи"
        // должна быть меньше или равна общей сумме по атрибуту "сумма к оплате" из массива "измененные требования по договору"
        // WithdrawalResultDto.amount - сумма в валюте договора/требования
        BigDecimal paymentsTotalAmount = properties.payments.stream()
            .map(p -> p.amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // все требования создаются в одной валюте (по крайней мере, сейчас это так)
        BigDecimal requirementsTotalAmount = properties.requirements.stream()
            .map(r -> r.amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.infof("paymentsTotalAmount = %s", paymentsTotalAmount);
        log.infof("requirementsTotalAmount = %s", requirementsTotalAmount);

        if (paymentsTotalAmount.compareTo(requirementsTotalAmount) > 0) {
            throw new RuntimeException(String.format("Total amount by payments (%s) is more than requirements total amount (%s)",
                paymentsTotalAmount, requirementsTotalAmount));
        }
    }

    @Override
    public AggregationResult<DistributePaidAmountsDto, DistributePaidAmountsJournalDto, DistributePaidAmountsResultDto> process(StepMethod.RequestItem.Request<DistributePaidAmountsDto, DistributePaidAmountsJournalDto> request) {
        DistributePaidAmountsResultDto result = new DistributePaidAmountsResultDto();
        DistributePaidAmountsJournalDto journal = request.getJournal();
        DistributePaidAmountsDto properties = request.getProperties();

        if (journal.isFirstRun) {
            journal.isFirstRun = false;
            // метод является частью общего метода "Изменение требований по договору"
            if (properties.requirements == null || properties.requirements.isEmpty()) {
                result.requirements = List.of();
                return new AggregationResult<>(properties, journal, result);
            }
            if (properties.payments == null || properties.payments.isEmpty()) {
                result.requirements = properties.requirements;
                return new AggregationResult<>(properties, journal, result);
            }

            // регистрируем оплату (распределяем суммы платежей, меняем статус ББП)
            RegistrationOfPaymentDto registrationRequest = new RegistrationOfPaymentDto();
            registrationRequest.payments = properties.payments;
            registrationRequest.requirements.addAll(
                properties.requirements.stream()
                    .map(r -> requirementService.getRequirementById(r.requirementId))
                    .sorted(Comparator
                        .comparing((Requirement r) -> r.priority)
                        .thenComparing((Requirement r) -> r.startPaymentDate)
                    )
                    .map((Requirement r) -> new ReferenceDto(r.id, SYS_RMS_REQUIREMENT_NAMESPACE))
                    .toList()
            );
            RegistrationOfPaymentResponse registrationResponse = paymentService.registrationOfPayment(registrationRequest);
            // заполняем журнал
            journal.createdPayments.addAll(registrationResponse.journal.createdPayments);
            journal.createdRelatedPayments.addAll(registrationResponse.journal.createdRelatedPayments);
            journal.requirementJournalMap = registrationResponse.journal.requirementJournal
                .stream().collect(Collectors.toMap(reqJournal -> reqJournal.id, requirementJournalDto -> requirementJournalDto));

            Map<Long, RequirementDTO> updatedRequirementsMap = registrationResponse.requirements.stream()
                .collect(Collectors.toMap(r -> r.id, Functions.identity()));

            // копируем весь массив информации о требованиях в результат
            properties.requirements.forEach(r -> {
                RequirementStateInfoDto requirementInfo = new RequirementStateInfoDto();
                requirementInfo.priority = r.priority;
                RequirementIndicatorDto indicator = new RequirementIndicatorDto();
                if (r.indicator != null) {
                    indicator.id = r.indicator.id;
                    indicator.__objectType = r.indicator.__objectType;
                    indicator.code = r.indicator.code;
                    if (r.indicator.indicatorDescr != null) {
                        indicator.indicatorDescr = new ReferenceDto(r.indicator.indicatorDescr.id, r.indicator.indicatorDescr.__objectType);
                    }
                }
                requirementInfo.indicator = indicator;
                requirementInfo.requirementId = r.requirementId;
                requirementInfo.amount = Objects.requireNonNullElse(r.amount, BigDecimal.ZERO);
                requirementInfo.action = r.action;
                requirementInfo.currentTransactionAmount = registrationResponse.currentTransactionAmounts.get(r.requirementId);
                RequirementDTO updatedRequirement = updatedRequirementsMap.getOrDefault(r.requirementId, null);
                if (updatedRequirement != null) {
                    requirementInfo.payedAmount = Objects.requireNonNullElse(updatedRequirement.paidAmount, BigDecimal.ZERO);
                    requirementInfo.status = updatedRequirement.unpaidAmount.compareTo(BigDecimal.ZERO) > 0 ? RequirementStatus.WAIT : RequirementStatus.PAID;
                } else {
                    requirementInfo.payedAmount = Objects.requireNonNullElse(r.payedAmount, BigDecimal.ZERO);
                    requirementInfo.status = r.status;
                }
                result.requirements.add(requirementInfo);
            });

            journal.intermediateResult = result;

            return new AggregationResult<>(journal, result, List.of(
                stepCreatorService.createSysBbpPackStateSubStep(
                    result.requirements.stream().map(
                        newReqState -> new Pair<>(journal.requirementJournalMap.get(newReqState.requirementId), newReqState)
                    ).toList()
                )
            ));

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
    public void undo(DistributePaidAmountsJournalDto journal) {
        requirementService.restoreWithoutBbpCancelExecution(
            journal.requirementJournalMap.values().stream().toList(), journal.createdPayments, journal.createdRelatedPayments,
            Collections.emptyList(), Collections.emptyList()
        );
    }
}
