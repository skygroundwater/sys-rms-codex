package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.common.Constants;
import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.BbpStateResult;
import com.colvir.ms.sys.rms.dto.PaymentRegistrationDto;
import com.colvir.ms.sys.rms.dto.PaymentRegistrationJournalDto;
import com.colvir.ms.sys.rms.dto.PaymentRegistrationResultDto;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentDto;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentResponse;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementDTO;
import com.colvir.ms.sys.rms.manual.service.RequirementPaymentService;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.colvir.ms.sys.rms.manual.util.RequirementMapperUtils;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.antlr.v4.runtime.misc.Pair;
import org.jboss.logging.Logger;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.colvir.ms.sys.rms.manual.util.RmsConstants.UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH;

@ApplicationScoped
public class PaymentRegistrationHandler extends AbstractStepRunnerHandler<PaymentRegistrationDto, PaymentRegistrationJournalDto, PaymentRegistrationResultDto> {

    RequirementPaymentService paymentService;

    RequirementService requirementService;

    StepCreatorService stepCreatorService;

    @Inject
    public PaymentRegistrationHandler(RequirementService requirementService,
                                      RequirementPaymentService paymentService,
                                      StepCreatorService stepCreatorService,
                                      Logger log) {
        super(StepsNames.SYS_RMS_PAYMENT_REGISTRATION, log);
        this.requirementService = requirementService;
        this.paymentService = paymentService;
        this.stepCreatorService = stepCreatorService;
    }

    @Override
    public AggregationResult<PaymentRegistrationDto, PaymentRegistrationJournalDto, PaymentRegistrationResultDto> process(StepMethod.RequestItem.Request<PaymentRegistrationDto, PaymentRegistrationJournalDto> request) {
        PaymentRegistrationResultDto result = new PaymentRegistrationResultDto();
        PaymentRegistrationJournalDto journal = request.getJournal();
        PaymentRegistrationDto properties = request.getProperties();

        if (journal.isFirstRun) {
            journal.isFirstRun = false;
            if (properties.payments == null || properties.payments.isEmpty()) {
                log.infof("rms-payment-registration --- return on empty payments");
                return new AggregationResult<>(properties, journal, result);
            }
            List<ReferenceDto> requirements = new ArrayList<>();
            boolean isArray = false;
            JsonNode requirementsNode = properties.requirements;
            if (requirementsNode.isNull() || requirementsNode.isMissingNode()) {
                log.infof("rms-payment-registration --- return on empty requirements");
                return new AggregationResult<>(properties, journal, result);
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

            RegistrationOfPaymentDto registrationRequest = new RegistrationOfPaymentDto();
            registrationRequest.payments = properties.payments;
            registrationRequest.requirements = requirements;
            RegistrationOfPaymentResponse registrationResponse = paymentService.registrationOfPayment(registrationRequest);

            if (isArray) {
                Map<Long, RequirementDTO> returnedMap = registrationResponse.requirements.stream()
                    .collect(Collectors.toMap(r -> r.id, Function.identity()));
                List<ObjectNode> changedList = new ArrayList<>();
                for (var node : requirementsNode) {
                    Long requirementId = node.get("id").asLong();
                    RequirementDTO returned = returnedMap.get(requirementId);
                    ObjectNode changed = ContextObjectMapper.get().createObjectNode();
                    changed.setAll((ObjectNode) node);
                    changed.put("version", returned.version);
                    changed.put("paidAmount", returned.paidAmount);
                    changed.put("unpaidAmount", returned.unpaidAmount);
                    if (returned.actualPaymentDate != null) {
                        changed.put("actualPaymentDate", returned.actualPaymentDate.format(DateTimeFormatter.ofPattern(Constants.LOCAL_DATE_FORMAT)));
                    }
                    changed.put("state", returned.state.toString());
                    changed.put("status", returned.bbpState000StateCode);
                    changed.put("currentTransactionAmount", registrationResponse.currentTransactionAmounts.get(requirementId));
                    changedList.add(changed);
                }
                result.requirements = changedList;

            } else {
                RequirementDTO returned = registrationResponse.requirements.getFirst();
                ObjectNode changed = ContextObjectMapper.get().createObjectNode();
                changed.setAll((ObjectNode) requirementsNode);
                changed.put("version", returned.version);
                changed.put("paidAmount", returned.paidAmount);
                changed.put("unpaidAmount", returned.unpaidAmount);
                if (returned.actualPaymentDate != null) {
                    changed.put("actualPaymentDate", returned.actualPaymentDate.format(DateTimeFormatter.ofPattern(Constants.LOCAL_DATE_FORMAT)));
                }
                changed.put("state", returned.state.toString());
                changed.put("status", returned.bbpState000StateCode);
                changed.put("currentTransactionAmount", registrationResponse.currentTransactionAmounts.get(returned.id));
                result.requirements = changed;
            }
            journal.createdPayments.addAll(registrationResponse.journal.createdPayments);
            journal.createdRelatedPayments.addAll(registrationResponse.journal.createdRelatedPayments);
            journal.requirementJournalMap = registrationResponse.journal.requirementJournal.stream().collect(
                Collectors.toMap(reqJournal -> reqJournal.id, requirementJournalDto -> requirementJournalDto)
            );
            journal.intermediateResult = result;

            return new AggregationResult<>(journal, result, List.of(
                stepCreatorService.createSysBbpPackStateSubStep(
                    registrationResponse.requirements.stream()
                        .map(requirementDTO -> new Pair<>(
                            journal.requirementJournalMap.get(requirementDTO.id),
                            RequirementMapperUtils.fillRequirementInfo(
                            requirementDTO.id,
                            RequirementAction.UPDATE,
                            requirementDTO.state,
                            requirementDTO.amount,
                            requirementDTO.paidAmount,
                            requirementDTO.priority,
                            requirementDTO.indicatorId
                        ))).toList()
                )
            ));

        } else {
            if (journal.intermediateResult != null) {
                result = journal.intermediateResult;
            }
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

            return new AggregationResult<>(properties, journal, result);
        }
    }

    @Override
    public void undo(PaymentRegistrationJournalDto journal) {
        requirementService.restoreWithoutBbpCancelExecution(
            journal.requirementJournalMap.values().stream().toList(), journal.createdPayments,
            journal.createdRelatedPayments, List.of(), List.of()
        );
    }
}
