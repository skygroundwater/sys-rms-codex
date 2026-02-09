package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.common.router.DDCRouterUtil;
import com.colvir.ms.common.router.dto.DDCModifyRequest;
import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.BbpStateResult;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.WriteOffDto;
import com.colvir.ms.sys.rms.dto.WriteOffJournalDto;
import com.colvir.ms.sys.rms.dto.WriteOffResultDto;
import com.colvir.ms.sys.rms.generated.domain.Payment;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.PaymentResult;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.generated.service.mapper.RequirementMapper;
import com.colvir.ms.sys.rms.manual.service.BaseProcessService;
import com.colvir.ms.sys.rms.manual.service.RequirementRouterService;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.RouterService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.colvir.ms.sys.rms.manual.util.RequirementMapperUtils;
import com.colvir.ms.sys.rms.manual.util.RmsConstants;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import org.antlr.v4.runtime.misc.Pair;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.colvir.ms.sys.rms.manual.util.RmsConstants.UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH;

@ApplicationScoped
public class WriteOffHandler extends AbstractStepRunnerHandler<WriteOffDto, WriteOffJournalDto, WriteOffResultDto>{

    RequirementService requirementService;
    BaseProcessService baseProcessService;
    RequirementRouterService requirementRouterService;
    RequirementMapper requirementMapper;
    StepCreatorService stepCreatorService;

    public WriteOffHandler(RequirementService requirementService,
                           RequirementMapper requirementMapper,
                           BaseProcessService baseProcessService,
                           RequirementRouterService requirementRouterService,
                           StepCreatorService stepCreatorService,
                           Logger log) {
        super(StepsNames.SYS_RMS_WRITE_OFF, log);
        this.requirementRouterService = requirementRouterService;
        this.requirementService = requirementService;
        this.baseProcessService = baseProcessService;
        this.stepCreatorService = stepCreatorService;
        this.requirementMapper = requirementMapper;
    }

    @Override
    public AggregationResult<WriteOffDto, WriteOffJournalDto, WriteOffResultDto> process(StepMethod.RequestItem.Request<WriteOffDto, WriteOffJournalDto> request) {
        WriteOffResultDto result = new WriteOffResultDto();
        WriteOffDto properties = request.getProperties();
        WriteOffJournalDto journal = request.getJournal();

        if (journal.isFirstRun) {
            journal.isFirstRun = false;
            JsonNode requirementNode = properties.requirement;
            ReferenceDto requirementReference;
            if (requirementNode.isNull() || requirementNode.isMissingNode()) {
                log.infof("rms-write-off --- return on empty requirement");
                return new AggregationResult<>(journal, result, List.of());
            } else {
                try {
                    requirementReference = ContextObjectMapper.get().treeToValue(requirementNode, ReferenceDto.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            Requirement requirement = requirementService.getRequirementById(requirementReference.id);
            // сохраняем данные до изменения
            journal.requirementJournal = RequirementMapperUtils.fillRequirementJournal(requirement);

            // проверяем состояние требования
            String requirementBbpState = requirement.bbpState000StateCode;
            if (RequirementStatus.WRITTEN_OFF.equals(requirement.state) && RmsConstants.WRITE_OFF_STATE.equals(requirementBbpState)) {
                return new AggregationResult<>(journal, result, List.of());
            }
            if ((!RequirementStatus.WAIT.equals(requirement.state) && !RequirementStatus.SUSPENDED.equals(requirement.state)) ||
                (!RmsConstants.WAIT_PAY_STATUS.equals(requirementBbpState) && !RmsConstants.PART_PAID_STATUS.equals(requirementBbpState) &&
                    !RmsConstants.SUSPENDED_STATE.equals(requirementBbpState))) {
                throw new RuntimeException(String.format("Withdrawal is not allowed for Requirement (id=%s) in state (%s)", requirementReference.id, requirement.state));
            }

            if (requirement.relatedPayments != null && !requirement.relatedPayments.isEmpty()) {
                // проверяем что нет незавершенных связанных платежей (результат оплаты = "не оплачен")
                Set<Payment> notPaidPayments = requirement.relatedPayments.stream()
                    .filter(r -> !Boolean.TRUE.equals(r.isDeleted))
                    .map(r -> r.payment)
                    .filter(p -> PaymentResult.NOT_PAID.equals(p.paymentResult))
                    .collect(Collectors.toSet()
                    );
                if (!notPaidPayments.isEmpty()) {
                    throw new RuntimeException(String.format("Withdrawal is not allowed for Requirement (id=%s). Found pending payments: %s", requirement.id, notPaidPayments));
                }
            }

            requirement.writeOffAmount = requirement.writeOffAmount.add(requirement.unpaidAmount);
            requirement.unpaidAmount = BigDecimal.ZERO;
            requirement.state = RequirementStatus.WRITTEN_OFF;
            requirement.version = requirement.version + 1;
            requirement.update();

            ObjectNode changed = ContextObjectMapper.get().createObjectNode();
            changed.setAll((ObjectNode) requirementNode);
            changed.put("version", requirement.version);
            changed.put("unpaidAmount", requirement.unpaidAmount);
            changed.put("writeOffAmount", requirement.writeOffAmount);
            changed.put("state", requirement.state.toString());
            changed.put("status", requirement.bbpState000StateCode);

            result.requirement = changed;

            journal.intermediateResult = result;

            return new AggregationResult<>(journal, List.of(
                stepCreatorService.createSysBbpPackStateSubStep(
                    List.of(new Pair<>(journal.requirementJournal, RequirementMapperUtils.fillRequirementInfo(requirement, RequirementAction.UPDATE, requirementRouterService.getRequirementIndicator(requirement.indicatorId))))
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
                List.of(journal.requirementJournal),
                bbpPackStateResult
            );

            return new AggregationResult<>(properties, journal, journal.intermediateResult);
        }

    }

    @Override
    public void undo(WriteOffJournalDto journal) {
        if (journal.requirementJournal != null) {
            requirementService.restoreWithoutBbpCancelExecution(
                List.of(journal.requirementJournal), null, null, null, null);
        }
    }
}
