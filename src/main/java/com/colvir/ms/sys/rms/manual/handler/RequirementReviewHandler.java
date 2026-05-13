package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.common.Constants;
import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.BbpStateResult;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RequirementReviewDto;
import com.colvir.ms.sys.rms.dto.RequirementReviewJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementReviewResultDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementJournalDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementResponse;
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
import java.util.List;
import java.util.Map;

import static com.colvir.ms.sys.rms.manual.constant.RmsConstants.UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH;

@ApplicationScoped
public class RequirementReviewHandler extends AbstractStepRunnerHandler<RequirementReviewDto, RequirementReviewJournalDto, RequirementReviewResultDto>{

    RequirementService requirementService;
    RequirementPaymentService paymentService;
    StepCreatorService stepCreatorService;
    RequirementDao requirementDao;

    @Inject
    public RequirementReviewHandler(RequirementService requirementService,
                                    RequirementPaymentService paymentService,
                                    StepCreatorService stepCreatorService,
                                    RequirementDao requirementDao,
                                    Logger log) {
        super(StepsNames.SYS_RMS_REVIEW, log);
        this.requirementService = requirementService;
        this.paymentService = paymentService;
        this.stepCreatorService = stepCreatorService;
        this.requirementDao = requirementDao;
    }

    @Override
    public AggregationResult<RequirementReviewDto, RequirementReviewJournalDto, RequirementReviewResultDto> process(StepMethod.RequestItem.Request<RequirementReviewDto, RequirementReviewJournalDto> request) {
        RequirementReviewJournalDto journal = request.getJournal();
        RequirementReviewDto properties = request.getProperties();
        RequirementReviewResultDto result = new RequirementReviewResultDto();

        if (journal.isFirstRun) {
            journal.isFirstRun = false;
            JsonNode requirementNode = properties.requirement;
            ReferenceDto requirementRef;
            if (requirementNode.isNull() || requirementNode.isMissingNode()) {
                log.infof("rms-review --- return on empty requirement");
                return new AggregationResult<>(journal, List.of());

            } else {
                try {
                    requirementRef = ContextObjectMapper.get().treeToValue(requirementNode, ReferenceDto.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            ReviewRequirementDto reviewRequest = new ReviewRequirementDto();
            reviewRequest.requirement = requirementRef;
            reviewRequest.priority = properties.priority;
            reviewRequest.amount = properties.amount;
            ReviewRequirementResponse reviewResponse = requirementService.requirementReview(reviewRequest);

            ObjectNode changed = ContextObjectMapper.get().createObjectNode();
            changed.setAll((ObjectNode) requirementNode);

            if (reviewResponse.reviewResult != null && reviewResponse.reviewResult.requirementInfo != null) {
                Long requirementId = reviewResponse.reviewResult.requirementInfo.requirementId;
                if (requirementId != null) {
                    Requirement requirement = requirementDao.findByIdOrThrow(requirementId);
                    changed.put("version", requirement.version);
                    changed.put("amount", requirement.amount);
                    changed.put("unpaidAmount", requirement.unpaidAmount);
                    changed.put("paidAmount", requirement.paidAmount);
                    changed.put("state", requirement.state.toString());
                    changed.put("status", requirement.bbpState000StateCode);
                    if (requirement.actualPaymentDate != null) {
                        changed.put("actualPaymentDate", requirement.actualPaymentDate.format(DateTimeFormatter.ofPattern(Constants.LOCAL_DATE_FORMAT)));
                    }
                    changed.put("priority", requirement.priority);
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
                }
                result.requirementInfo = reviewResponse.reviewResult.requirementInfo;
            }
            result.requirement = changed;
            journal.refundJournal = reviewResponse.reviewJournal.refundJournal;
            journal.requirementJournal = reviewResponse.reviewJournal.requirementJournal;
            journal.intermediateResult = result;

            return new AggregationResult<>(journal,
                List.of(
                    stepCreatorService.createSysBbpPackStateSubStep(
                        List.of(new Pair<>(journal.requirementJournal, result.requirementInfo))
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
                List.of(journal.requirementJournal),
                bbpPackStateResult
            );

            return new AggregationResult<>(properties, journal, journal.intermediateResult);
        }
    }

    @Override
    public void undo(RequirementReviewJournalDto journal) {
        ReviewRequirementJournalDto undoRequest = new ReviewRequirementJournalDto();
        undoRequest.requirementJournal = journal.requirementJournal;
        undoRequest.refundJournal = journal.refundJournal;
        requirementService.requirementReviewUndo(undoRequest);
    }

}
