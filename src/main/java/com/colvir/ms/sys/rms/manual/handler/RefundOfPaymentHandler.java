package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.BbpStateResult;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RefundJournalDto;
import com.colvir.ms.sys.rms.dto.RefundOfPaymentDto;
import com.colvir.ms.sys.rms.dto.RefundOfPaymentResultDto;
import com.colvir.ms.sys.rms.dto.RefundOfPaymentRunnerDto;
import com.colvir.ms.sys.rms.dto.RefundResponse;
import com.colvir.ms.sys.rms.generated.domain.Payment;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.dao.PaymentDao;
import com.colvir.ms.sys.rms.manual.service.RequirementPaymentService;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
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
import java.util.List;
import java.util.Map;

import static com.colvir.ms.sys.rms.manual.constant.RmsConstants.UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH;

@ApplicationScoped
public class RefundOfPaymentHandler extends AbstractStepRunnerHandler<RefundOfPaymentRunnerDto, RefundJournalDto, RefundOfPaymentResultDto>{

    RequirementPaymentService paymentService;

    StepCreatorService stepCreatorService;

    RequirementService requirementService;

    PaymentDao paymentDao;

    @Inject
    public RefundOfPaymentHandler(RequirementPaymentService paymentService,
                                  StepCreatorService stepCreatorService,
                                  RequirementService requirementService,
                                  PaymentDao paymentDao,
                                  Logger log) {
        super(StepsNames.SYS_RMS_REFUND_PAYMENT, log);
        this.paymentService = paymentService;
        this.stepCreatorService = stepCreatorService;
        this.requirementService = requirementService;
        this.paymentDao = paymentDao;
    }

    @Override
    public AggregationResult<RefundOfPaymentRunnerDto, RefundJournalDto, RefundOfPaymentResultDto> process(StepMethod.RequestItem.Request<RefundOfPaymentRunnerDto, RefundJournalDto> request) {
        RefundOfPaymentRunnerDto properties = request.getProperties();
        RefundJournalDto journal = request.getJournal();
        RefundOfPaymentResultDto result = new RefundOfPaymentResultDto();

        if (journal.isFirstRun) {
            journal.isFirstRun = false;
            JsonNode paymentNode = properties.payment;
            ReferenceDto paymentRef;
            if (paymentNode.isNull() || paymentNode.isMissingNode()) {
                log.infof("rms-refund-payment --- return on empty payment");
                return new AggregationResult<>(journal, result, List.of());

            } else {
                try {
                    paymentRef = ContextObjectMapper.get().treeToValue(paymentNode, ReferenceDto.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            RefundOfPaymentDto refundRequest = new RefundOfPaymentDto();
            refundRequest.refundAmount = properties.amount;
            refundRequest.payment = paymentRef;
            RefundResponse refundResponse = paymentService.refundOfPayment(refundRequest);

            ObjectNode changed = ContextObjectMapper.get().createObjectNode();
            changed.setAll((ObjectNode) paymentNode);
            if (refundResponse.refundResult != null) {
                if (refundResponse.refundResult.requirementsInfo != null && !refundResponse.refundResult.requirementsInfo.isEmpty()) {
                    result.requirementsInfo = refundResponse.refundResult.requirementsInfo;
                }
                if (refundResponse.refundResult.refundsInfo != null && !refundResponse.refundResult.refundsInfo.isEmpty()) {
                    Long paymentId = paymentRef.id;
                    Payment payment = paymentDao.findByIdOrThrow(paymentId);
                    changed.put("version", payment.version);
                    if (changed.hasNonNull("paymentRefunds")) {
                        ArrayNode paymentRefunds = ContextObjectMapper.get().createArrayNode();
                        if (payment.refundPayments != null && !payment.refundPayments.isEmpty()) {
                            payment.refundPayments.stream()
                                .filter(r -> !Boolean.TRUE.equals(r.isDeleted))
                                .forEach(
                                    r -> {
                                        ObjectNode refund = ContextObjectMapper.get().createObjectNode();
                                        refund.put("id", r.id);
                                        refund.put("__objectType", "/SYS/RMS/RefundingPayment");
                                        refund.put("amount", r.amount);
                                        refund.put("reference", r.reference);
                                        if (r.refundTime != null) {
                                            refund.put("refundTime", DateTimeFormatter.ISO_INSTANT.format(r.refundTime));
                                        }
                                        if (r.creationTime != null) {
                                            refund.put("creationTime", DateTimeFormatter.ISO_INSTANT.format(r.creationTime));
                                        }
                                        paymentRefunds.add(refund);
                                    }
                                );
                        }
                        changed.set("paymentRefunds", paymentRefunds);
                    }
                }
            }
            result.payment = changed;
            if (refundResponse.refundJournal != null) {
                journal.requirementJournal = refundResponse.refundJournal.requirementJournal;
                journal.createdPaymentRefunds = refundResponse.refundJournal.createdPaymentRefunds;
                journal.relatedPaymentsJournal = refundResponse.refundJournal.relatedPaymentsJournal;
                journal.intermediateRefundOfPaymentResult = result;
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

            return new AggregationResult<>(properties, journal, journal.intermediateRefundOfPaymentResult);
        }
    }

    @Override
    public void undo(RefundJournalDto journal) {
        paymentService.refundOfPaymentUndo(journal);
    }

}
