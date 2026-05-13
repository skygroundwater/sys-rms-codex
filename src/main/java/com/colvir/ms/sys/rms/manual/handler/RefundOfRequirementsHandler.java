package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.common.Constants;
import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.BbpStateResult;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RefundJournalDto;
import com.colvir.ms.sys.rms.dto.RefundOfRequirementsDto;
import com.colvir.ms.sys.rms.dto.RefundOfRequirementsResultDto;
import com.colvir.ms.sys.rms.dto.RefundOfRequirementsRunnerDto;
import com.colvir.ms.sys.rms.dto.RefundResponse;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.manual.dao.RequirementDao;
import com.colvir.ms.sys.rms.manual.service.RequirementPaymentService;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.antlr.v4.runtime.misc.Pair;
import org.jboss.logging.Logger;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.colvir.ms.sys.rms.manual.constant.RmsConstants.UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH;

@ApplicationScoped
public class RefundOfRequirementsHandler extends AbstractStepRunnerHandler<RefundOfRequirementsRunnerDto, RefundJournalDto, RefundOfRequirementsResultDto> {

    RequirementPaymentService paymentService;

    StepCreatorService stepCreatorService;

    RequirementService requirementService;

    RequirementDao requirementDao;

    @Inject
    public RefundOfRequirementsHandler(RequirementPaymentService paymentService,
                                       StepCreatorService stepCreatorService,
                                       RequirementService requirementService,
                                       RequirementDao requirementDao,
                                       Logger log) {
        super(StepsNames.SYS_RMS_REFUND_REQUIREMENTS, log);
        this.paymentService = paymentService;
        this.requirementService = requirementService;
        this.requirementDao = requirementDao;
        this.stepCreatorService = stepCreatorService;
    }

    @Override
    public AggregationResult<RefundOfRequirementsRunnerDto, RefundJournalDto, RefundOfRequirementsResultDto> process(StepMethod.RequestItem.Request<RefundOfRequirementsRunnerDto, RefundJournalDto> request) {
        RefundOfRequirementsResultDto result = new RefundOfRequirementsResultDto();
        RefundJournalDto journal = request.getJournal();
        RefundOfRequirementsRunnerDto properties = request.getProperties();

        if (journal.isFirstRun) {
            journal.isFirstRun = false;
            List<ReferenceDto> requirements = new ArrayList<>();
            boolean isArray = false;

            JsonNode requirementsNode = properties.requirements;
            if (requirementsNode.isNull() || requirementsNode.isMissingNode()) {
                log.infof("rms-refund-requirements --- return on empty requirements");
                return new AggregationResult<>(journal, List.of());
            } else {
                try {
                    if (!requirementsNode.isArray()) {
                        requirements.add(
                            ContextObjectMapper.get().treeToValue(requirementsNode, ReferenceDto.class)
                        );
                    } else {
                        for (var node : requirementsNode) {
                            requirements.add(
                                ContextObjectMapper.get().treeToValue(node, ReferenceDto.class)
                            );
                        }
                        isArray = true;
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            RefundOfRequirementsDto refundRequest = new RefundOfRequirementsDto();
            refundRequest.refundAmount = properties.amount;
            refundRequest.requirements = requirements;
            RefundResponse refundResponse = paymentService.refundOfPayment(refundRequest);

            if (refundResponse.refundResult != null) {
                if (refundResponse.refundResult.requirementsInfo != null && !refundResponse.refundResult.requirementsInfo.isEmpty()) {
                    result.requirementsInfo = refundResponse.refundResult.requirementsInfo;
                }
                if (refundResponse.refundResult.refundsInfo != null && !refundResponse.refundResult.refundsInfo.isEmpty()) {
                    // были возвраты сумм
                    if (isArray) {
                        List<ObjectNode> changedList = new ArrayList<>();
                        for (var node : requirementsNode) {
                            Long requirementId = node.get("id").asLong();
                            ObjectNode changed = fillChangedNode(node, requirementId);
                            changedList.add(changed);
                        }
                        result.requirements = changedList;

                    } else {
                        Long requirementId = requirementsNode.get("id").asLong();
                        result.requirements = fillChangedNode(requirementsNode, requirementId);
                    }
                } else {
                    ObjectNode changed = ContextObjectMapper.get().createObjectNode();
                    changed.setAll((ObjectNode) requirementsNode);
                    result.requirements = changed;
                }
            } else {
                ObjectNode changed = ContextObjectMapper.get().createObjectNode();
                changed.setAll((ObjectNode) requirementsNode);
                result.requirements = changed;
            }

            if (refundResponse.refundJournal != null) {
                journal.requirementJournal = refundResponse.refundJournal.requirementJournal;
                journal.createdPaymentRefunds = refundResponse.refundJournal.createdPaymentRefunds;
                journal.relatedPaymentsJournal = refundResponse.refundJournal.relatedPaymentsJournal;
                journal.intermediateRefundOfRequirementResult = result;
            }

            return new AggregationResult<>(journal,
                List.of(
                    stepCreatorService.createSysBbpPackStateSubStep(
                        result.requirementsInfo.stream().map(
                            newReqState -> new Pair<>(journal.requirementJournal.get(newReqState.requirementId), newReqState)
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
                journal.requirementJournal.values().stream().toList(),
                bbpPackStateResult
            );

            return new AggregationResult<>(properties, journal, journal.intermediateRefundOfRequirementResult);
        }
    }

    private ObjectNode fillChangedNode(JsonNode originalNode, Long requirementId) {
        ObjectNode changed = ContextObjectMapper.get().createObjectNode();
        changed.setAll((ObjectNode) originalNode);
        Requirement requirement = requirementDao.findById(requirementId);
        changed.put("version", requirement.version);
        changed.put("unpaidAmount", requirement.unpaidAmount);
        changed.put("paidAmount", requirement.paidAmount);
        changed.put("state", requirement.state.toString());
        changed.put("status", requirement.bbpState000StateCode);
        if (requirement.actualPaymentDate != null) {
            changed.put("actualPaymentDate", requirement.actualPaymentDate.format(DateTimeFormatter.ofPattern(Constants.LOCAL_DATE_FORMAT)));
        }
        if (changed.hasNonNull("relatedPayments")) {
            ArrayNode relatedPayments = ContextObjectMapper.get().createArrayNode();
            if (requirement.relatedPayments != null && !requirement.relatedPayments.isEmpty()) {
                requirement.relatedPayments.stream()
                    .filter(p -> !Boolean.TRUE.equals(p.isDeleted))
                    .forEach(
                        p -> {
                            ObjectNode related = ContextObjectMapper.get().createObjectNode();
                            related.put("id", p.id);
                            related.put("__objectType", "/SYS/RMS/RelatedPayment");
                            related.put("amount", p.amount);
                            related.put("amountOfPayment", p.amountOfPayment);
                            ObjectNode payment = ContextObjectMapper.get().createObjectNode();
                            payment.put("id", p.payment.id);
                            payment.put("__objectType", "/SYS/RMS/Payment");
                            related.set("payment", payment);
                            relatedPayments.add(related);
                        }
                    );
            }
            changed.set("relatedPayments", relatedPayments);
        }
        return changed;
    }

    @Override
    public void undo(RefundJournalDto journal) {
        paymentService.refundOfPaymentUndo(journal);
    }

}
