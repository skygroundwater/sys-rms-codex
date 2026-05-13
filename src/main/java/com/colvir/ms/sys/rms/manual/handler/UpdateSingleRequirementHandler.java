package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.BbpStateResult;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.dto.UpdateSingleRequirementDto;
import com.colvir.ms.sys.rms.dto.UpdateSingleRequirementJournalDto;
import com.colvir.ms.sys.rms.dto.UpdateSingleRequirementResultDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.colvir.ms.sys.rms.manual.util.RequirementMapperUtils;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.antlr.v4.runtime.misc.Pair;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.colvir.ms.sys.rms.manual.service.impl.RequirementServiceImpl.LOCAL_DATE_FORMATTER;
import static com.colvir.ms.sys.rms.manual.constant.RmsConstants.UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH;

@ApplicationScoped
public class UpdateSingleRequirementHandler extends AbstractStepRunnerHandler<UpdateSingleRequirementDto, UpdateSingleRequirementJournalDto, UpdateSingleRequirementResultDto> {

    RequirementService requirementService;

    StepCreatorService stepCreatorService;

    @Inject
    public UpdateSingleRequirementHandler(RequirementService requirementService,
                                          StepCreatorService stepCreatorService,
                                          Logger log) {
        super(StepsNames.SYS_RMS_UPDATE_SINGLE_REQUIREMENT, log);
        this.requirementService = requirementService;
        this.stepCreatorService = stepCreatorService;
    }

    @Override
    public AggregationResult<UpdateSingleRequirementDto, UpdateSingleRequirementJournalDto, UpdateSingleRequirementResultDto> process(StepMethod.RequestItem.Request<UpdateSingleRequirementDto, UpdateSingleRequirementJournalDto> request) {
        UpdateSingleRequirementResultDto result = new UpdateSingleRequirementResultDto();
        UpdateSingleRequirementJournalDto journal = request.getJournal();
        UpdateSingleRequirementDto properties = request.getProperties();
        RequirementStateInfoDto newRequirementState = properties.getPaymentData();

        if (journal.isFirstRun()) {
            journal.setFirstRun(false);

            RequirementStateInfoDto requirementInfo = RequirementMapperUtils.mapRequirementStateInfoDto(
                newRequirementState, newRequirementState.requirementId, RequirementAction.SAVE, newRequirementState.status
            );

            result.setRequirement(requirementInfo);

            Requirement requirement = requirementService.getRequirementById(newRequirementState.requirementId);

            // сохраняем первоначальные значения для отката
            RequirementJournalDto requirementJournal = RequirementMapperUtils.fillRequirementJournal(requirement);
            journal.setRequirementJournal(requirementJournal);

            if (newRequirementState.paymentEndDate != null) {
                requirement.paymentEndDate = LocalDate.parse(newRequirementState.paymentEndDate.format(LOCAL_DATE_FORMATTER));
            }

            // приоритет, меняется только дробная часть := "приоритет оплаты" из записи массива
            BigInteger integerPriority = requirement.priority.toBigInteger();
            BigDecimal decimalPriority = new BigDecimal(newRequirementState.priority).divide(new BigDecimal("100"), 2, RoundingMode.UP);
            requirement.priority = new BigDecimal(integerPriority).add(decimalPriority);

            // TODO непонятно, нужен ли здесь возврат платежа и пересмотр сумм связанных платежей
            if (requirement.amount.compareTo(newRequirementState.amount) != 0) {
                if (newRequirementState.status == null) {
                    throw new RuntimeException("PaymentData status should not be empty");
                }
                // можем выполнять пересмотр сумм только для требований, ожидающих оплату (сейчас или в результате смены состояния)
                // при аннулировании требования сумма тоже может меняться (обнуляться)
                if (!RequirementStatus.WAIT.equals(requirement.state) && !RequirementStatus.WAIT.equals(newRequirementState.status)
                    && !RequirementStatus.CANCELED.equals(newRequirementState.status)
                ) {
                    throw new RuntimeException(String.format("It is forbidden to change the amount of the Requirement (id=%s) in state %s (next state %s)",
                        requirement.id, requirement.state, newRequirementState.status));
                }
                // невозможно просто уменьшить сумму требования, если по нему уже оплачена или списана более крупная сумма
                if (requirement.paidAmount.compareTo(newRequirementState.amount) > 0) {
                    throw new RuntimeException(String.format("Requirement (id=%s) paid amount (%s) cannot be greater than new Requirement amount (%s)",
                        requirement.id, requirement.paidAmount, newRequirementState.amount));
                }
                if (requirement.writeOffAmount.compareTo(newRequirementState.amount) > 0) {
                    throw new RuntimeException(String.format("Requirement (id=%s) writeOff amount (%s) cannot be greater than new Requirement amount (%s)",
                        requirement.id, requirement.writeOffAmount, newRequirementState.amount));
                }

                requirement.amount = newRequirementState.amount;
                BigDecimal unpaidAmount = requirement.amount.subtract(requirement.paidAmount);
                if (requirement.unpaidAmount.compareTo(BigDecimal.ZERO) > 0 && unpaidAmount.compareTo(BigDecimal.ZERO) == 0) {
                    // оплатить
                    requirement.state = RequirementStatus.PAID;
                } else {
                    requirement.state = newRequirementState.status;
                }
                requirement.unpaidAmount = unpaidAmount;
            }

            requirement.indicatorId = newRequirementState.indicator.id;

            // смена состояния ББП
            if (RequirementStatus.WRITTEN_OFF.equals(requirement.state)) {
                // если будет выполняться "списание"
                requirement.writeOffAmount = requirement.writeOffAmount.add(requirement.unpaidAmount);
                requirement.unpaidAmount = BigDecimal.ZERO;
            }
            requirement.update();
            journal.setIntermediateResult(result);

            return new AggregationResult<>(journal, result, List.of(
                stepCreatorService
                    .createSysBbpPackStateSubStep(
                        List.of(new Pair<>(journal.getRequirementJournal(), result.getRequirement()))
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
                List.of(journal.getRequirementJournal()),
                bbpPackStateResult
            );

            result.setRequirement(journal.getIntermediateResult().getRequirement());
            return new AggregationResult<>(journal, result, List.of());
        }

    }

    @Override
    public void undo(UpdateSingleRequirementJournalDto journal) {
        requirementService.updateRequirementsUndo(List.of(journal.getRequirementJournal()));
    }

}
