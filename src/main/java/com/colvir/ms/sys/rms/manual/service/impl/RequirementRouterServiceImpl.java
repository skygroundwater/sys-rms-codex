package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.common.router.DDCRouterUtil;
import com.colvir.ms.common.router.dto.DDCModifyRequest;
import com.colvir.ms.common.router.dto.DDCReadRequest;
import com.colvir.ms.sys.rms.dto.CreateRequirementDto;
import com.colvir.ms.sys.rms.dto.RelatedPaymentsJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementIndicatorDto;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.generated.domain.Payment;
import com.colvir.ms.sys.rms.generated.domain.RefundingPayment;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementTypeDTO;
import com.colvir.ms.sys.rms.manual.dao.RequirementDao;
import com.colvir.ms.sys.rms.manual.service.RequirementRouterService;
import com.colvir.ms.sys.rms.manual.service.RequirementTypeService;
import com.colvir.ms.sys.rms.manual.service.RouterService;
import com.colvir.ms.sys.rms.manual.util.CacheInvalidateServiceImpl;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.colvir.ms.sys.rms.manual.util.RmsConstants;
import com.colvir.ms.sys.rms.manual.util.SystemParameterService;
import com.colvir.ms.sys.rms.manual.web.dto.BaseProcessResultDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.colvir.ms.sys.rms.manual.service.impl.RequirementServiceImpl.LOCAL_DATE_FORMATTER;

@ApplicationScoped
public class RequirementRouterServiceImpl implements RequirementRouterService {

    private static final String REQUIREMENT = "Requirement";
    private static final String PAYMENT = "Payment";
    private static final String REFUNDING_PAYMENT = "RefundingPayment";

    @ConfigProperty(name = "application.domain", defaultValue = "/SYS/RMS")
    String applicationDomain;

    private final RequirementTypeService requirementTypeService;
    private final RequirementDao requirementDao;
    private final RouterService routerService;
    private final ObjectMapper objectMapper;
    private final SystemParameterService systemParameterService;
    private final Logger log;

    public RequirementRouterServiceImpl(RequirementTypeService requirementTypeService, RequirementDao requirementDao,
                                        RouterService routerService, ObjectMapper objectMapper,
                                        SystemParameterService systemParameterService, Logger log) {
        this.requirementTypeService = requirementTypeService;
        this.requirementDao = requirementDao;
        this.routerService = routerService;
        this.objectMapper = objectMapper;
        this.systemParameterService = systemParameterService;
        this.log = log;
    }

    @Override
    public Long createRequirement(CreateRequirementDto createRequirementDto) {
        return createRequirements(List.of(createRequirementDto))
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("The router response does not contain correct result"));
    }

    @Override
    public List<Long> createRequirements(Collection<CreateRequirementDto> createRequirementDtos) {
        List<DDCModifyRequest> requests = createRequirementDtos
            .stream()
            .map(this::createRequestBody)
            .map(body -> DDCRouterUtil.createDDCCreateRequest(applicationDomain, REQUIREMENT, body))
            .toList();
        log.debugf("createRequirements request: %s", requests);
        return routerService.modify(requests)
            .stream()
            .map(routerService::extractIdFromResult)
            .toList();
    }

    @Override
    @CacheResult(cacheName = CacheInvalidateServiceImpl.REQUIREMENT_INDICATOR_CACHE)
    public RequirementIndicatorDto getRequirementIndicator(Long indicatorId) {
        if (indicatorId == null) {
            throw new RuntimeException("Indicator id is null");
        }
        Map<String, Object> readRequestQuery = new HashMap<>();
        readRequestQuery.put("id", Map.of("equals", indicatorId.toString()));
        DDCReadRequest readRequest = DDCRouterUtil.createDDCReadRequest(
            "/SYS/PROD",
            "Indicator",
            new ArrayList<>(),
            readRequestQuery
        );
        List<ObjectNode> indicatorsList = routerService.read(readRequest);
        if (indicatorsList.isEmpty()) {
            throw new RuntimeException(String.format("Indicator with id=%s not found", indicatorId));
        }
        return objectMapper.convertValue(indicatorsList.getFirst(), RequirementIndicatorDto.class);
    }

    @Override
    public ObjectNode deleteRequirement(Long id) {
        return Optional.ofNullable(id)
            .map(i -> deleteRequirements(List.of(i)))
            .flatMap(l -> l.stream().findFirst())
            .orElse(null);
    }

    @Override
    public List<ObjectNode> deleteRequirements(Collection<Long> ids) {
        List<DDCModifyRequest> requests = Optional.ofNullable(ids)
            .stream()
            .flatMap(Collection::stream)
            .map(requirementDao::findById)
            .filter(Objects::nonNull)
            .filter(requirement -> BooleanUtils.isNotTrue(requirement.isDeleted))
            .map(requirement -> {
                ObjectNode attributes = objectMapper.createObjectNode();
                attributes.put("id", requirement.id);
                attributes.put("version", requirement.version);
                return DDCRouterUtil.createDDCDeleteRequest(applicationDomain, REQUIREMENT, attributes);
            }).toList();
        log.debugf("deleteRequirements request: %s", requests);
        return routerService.modify(requests);
    }

    @Override
    public void restoreRequirementWithoutBbpCancel(Requirement requirement,
                                                   RequirementJournalDto journal,
                                                   List<Long> createdRelatedPayments,
                                                   List<Long> createdReqRefundingPayments) {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("id", journal.id);
        body.put("version", requirement.version);
        body.put("unpaidAmount", journal.unpaidAmount);
        body.put("paidAmount", journal.paidAmount);
        body.put("writeOffAmount", journal.writeOffAmount);
        body.put("amount", journal.amount);
        body.put("priority", journal.priority);
        body.put("bbpState000StateCode", journal.bbpStateCode);
        body.put("bbpState000ProcessId", journal.bbpProcessId);
        body.put("bbpState000JournalId", journal.bbpJournalId);
        body.put("state", journal.state.toString());
        if (journal.indicator != null) {
            body.set("indicator", objectMapper.createObjectNode().put("id", journal.indicator.id));
        }
        if (journal.actualPaymentDate != null) {
            body.put("actualPaymentDate", journal.actualPaymentDate.format(LOCAL_DATE_FORMATTER));
        } else {
            body.putNull("actualPaymentDate");
        }
        if (journal.paymentEndDate != null) {
            body.put("paymentEndDate", journal.paymentEndDate.format(LOCAL_DATE_FORMATTER));
        } else {
            body.putNull("paymentEndDate");
        }
        appendRelatedDeletes(body, requirement, createdRelatedPayments);
        appendRefundingDeletes(body, requirement, createdReqRefundingPayments);
        routerService.modify(RmsConstants.SYS_RMS_NAMESPACE, REQUIREMENT, body);
    }

    @Override
    public void restoreRequirementForRefundUndo(Requirement requirement,
                                                RequirementJournalDto journal,
                                                Map<Long, RelatedPaymentsJournalDto> relatedToUpdate) {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("id", journal.id);
        body.put("version", requirement.version);
        body.put("bbpState000StateCode", journal.bbpStateCode);
        body.put("bbpState000JournalId", journal.bbpJournalId);
        body.put("state", journal.state.toString());
        body.put("unpaidAmount", journal.unpaidAmount);
        body.put("paidAmount", journal.paidAmount);
        body.put("writeOffAmount", journal.writeOffAmount);
        body.put("amount", journal.amount);
        body.put("priority", journal.priority);
        if (journal.actualPaymentDate != null) {
            body.put("actualPaymentDate", journal.actualPaymentDate.format(LOCAL_DATE_FORMATTER));
        } else {
            body.putNull("actualPaymentDate");
        }
        if (relatedToUpdate != null && !relatedToUpdate.isEmpty() && requirement.relatedPayments != null && !requirement.relatedPayments.isEmpty()) {
            ArrayNode relatedPayments = objectMapper.createArrayNode();
            body.set("relatedPayments", relatedPayments);
            requirement.relatedPayments.stream()
                .filter(p -> relatedToUpdate.containsKey(p.id))
                .forEach(p -> {
                    RelatedPaymentsJournalDto paymentJournal = relatedToUpdate.get(p.id);
                    ObjectNode related = objectMapper.createObjectNode();
                    related.put("id", p.id);
                    related.put("amount", paymentJournal.amount);
                    related.put("amountOfPayment", paymentJournal.amountOfPayment);
                    relatedPayments.add(related);
                });
        }
        routerService.modify(RmsConstants.SYS_RMS_NAMESPACE, REQUIREMENT, body);
    }

    @Override
    public void deletePayment(Payment payment) {
        ObjectNode paymentBody = objectMapper.createObjectNode();
        paymentBody.put("id", payment.id);
        paymentBody.put("version", payment.version);
        DDCModifyRequest deleteRequest = DDCRouterUtil.createDDCDeleteRequest(RmsConstants.SYS_RMS_NAMESPACE, PAYMENT, paymentBody);
        log.infof("deleteRequest: \r\n %s", objectMapper.convertValue(deleteRequest, ObjectNode.class));
        ObjectNode response = routerService.modify(deleteRequest).getFirst();
        log.infof("deleteResponse: \r\n%s", response);
    }

    @Override
    public void deleteRefundingPayment(RefundingPayment refundingPayment) {
        ObjectNode paymentBody = objectMapper.createObjectNode();
        paymentBody.put("id", refundingPayment.id);
        DDCModifyRequest deleteRequest = DDCRouterUtil.createDDCDeleteRequest(RmsConstants.SYS_RMS_NAMESPACE, REFUNDING_PAYMENT, paymentBody);
        log.infof("deleteRequest: \r\n %s", objectMapper.convertValue(deleteRequest, ObjectNode.class));
        ObjectNode response = routerService.modify(deleteRequest).getFirst();
        log.infof("deleteResponse: \r\n%s", response);
    }

    @Override
    public void deletePaymentRefundLink(RefundingPayment paymentRefund, Long refundId) {
        ObjectNode paymentBody = objectMapper.createObjectNode();
        paymentBody.put("id", paymentRefund.paymentOfRefundPayments.id);
        paymentBody.put("version", paymentRefund.paymentOfRefundPayments.version);
        ArrayNode paymentRefunds = objectMapper.createArrayNode();
        ObjectNode refund = objectMapper.createObjectNode();
        refund.put("id", refundId);
        refund.put("__state", "D");
        paymentRefunds.add(refund);
        paymentBody.set("paymentRefunds", paymentRefunds);
        routerService.modify(RmsConstants.SYS_RMS_NAMESPACE, PAYMENT, paymentBody);
    }

    private void appendRelatedDeletes(ObjectNode body, Requirement requirement, List<Long> createdRelatedPayments) {
        if (createdRelatedPayments == null || createdRelatedPayments.isEmpty() || requirement.relatedPayments == null || requirement.relatedPayments.isEmpty()) {
            return;
        }
        Set<Long> relatedToDelete = Set.copyOf(createdRelatedPayments);
        ArrayNode relatedPayments = objectMapper.createArrayNode();
        requirement.relatedPayments.stream()
            .filter(p -> relatedToDelete.contains(p.id))
            .forEach(p -> {
                ObjectNode related = objectMapper.createObjectNode();
                related.put("id", p.id);
                related.put("__state", "D");
                relatedPayments.add(related);
            });
        if (!relatedPayments.isEmpty()) {
            body.set("relatedPayments", relatedPayments);
        }
    }

    private void appendRefundingDeletes(ObjectNode body, Requirement requirement, List<Long> createdReqRefundingPayments) {
        if (createdReqRefundingPayments == null || createdReqRefundingPayments.isEmpty() || requirement.refundingPayments == null || requirement.refundingPayments.isEmpty()) {
            return;
        }
        Set<Long> refundingToDelete = Set.copyOf(createdReqRefundingPayments);
        ArrayNode refundingPayments = objectMapper.createArrayNode();
        requirement.refundingPayments.stream()
            .filter(p -> refundingToDelete.contains(p.id))
            .forEach(p -> {
                ObjectNode related = objectMapper.createObjectNode();
                related.put("id", p.id);
                related.put("__state", "D");
                refundingPayments.add(related);
            });
        if (!refundingPayments.isEmpty()) {
            body.set("refundingPayments", refundingPayments);
        }
    }

    private ObjectNode createRequestBody(CreateRequirementDto createRequirementDto) {
        Long systemLocale = systemParameterService.getSystemLocale(RmsConstants.SYSTEM_LOCALE_PARAM);
        RequirementStateInfoDto paymentData = createRequirementDto.getPaymentData();
        if (systemLocale == null) {
            throw new RuntimeException("SystemLocale parameter is not define");
        }

        String initialBbpState = createRequirementDto.getInitialBbpState();
        log.infof("initialBbpState : %s", initialBbpState);

        RequirementTypeDTO requirementType = requirementTypeService.getRequirementType(paymentData.indicator.indicatorDescr, systemLocale);

        BigDecimal integerPriority = new BigDecimal(requirementType.priority);
        BigDecimal decimalPriority = new BigDecimal(paymentData.priority).divide(new BigDecimal("100"), 2, RoundingMode.UP);
        BigDecimal priority = integerPriority.add(decimalPriority);

        ObjectNode body = objectMapper.createObjectNode();
        if (paymentData.requirementId != null) {
            body.put("id", paymentData.requirementId);
        }

        BaseProcessResultDto bbpState;
        try {
            bbpState = ContextObjectMapper.get().readValue(initialBbpState, BaseProcessResultDto.class);
            if (bbpState.stateCode == null || bbpState.stateCode.isBlank()) {
                JsonNode root = ContextObjectMapper.get().readTree(initialBbpState);
                body.put("bbpState000StateCode", root.path("state").asText("wait_pay"));
            } else {
                body.put("bbpState000StateCode", bbpState.stateCode);
            }
            body.put("bbpState000ProcessId", bbpState.processId);
            body.put("bbpState000JournalId", String.valueOf(bbpState.journalId));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Не удалось прочесть состояние бизнес процесса. bbState=%s для класса ", initialBbpState));
        }

        body.put("state", RequirementStatus.WAIT.toString());
        body.put("unpaidAmount", paymentData.amount);
        body.put("paidAmount", BigDecimal.ZERO);
        body.put("writeOffAmount", BigDecimal.ZERO);
        body.put("amount", paymentData.amount);
        body.set("currency", objectMapper.createObjectNode().put("id", createRequirementDto.getCurrency().id).put("__objectType", "/SYS/CUR/Currency"));
        body.set("indicator", objectMapper.createObjectNode().put("id", paymentData.indicator.id));
        body.put("date", createRequirementDto.getBusinessDate().format(LOCAL_DATE_FORMATTER));
        body.put("startPaymentDate", createRequirementDto.getBusinessDate().format(LOCAL_DATE_FORMATTER));
        body.put("paymentEndDate", createRequirementDto.getBusinessDate().format(LOCAL_DATE_FORMATTER));
        body.put("isContractBound", true);
        body.set("baseDocument", objectMapper.createObjectNode().put("id", createRequirementDto.getContract().id).put("__objectType", createRequirementDto.getContract().__objectType));
        body.set("client", objectMapper.createObjectNode().put("id", createRequirementDto.getClient().id).put("__objectType", createRequirementDto.getClient().__objectType));
        body.set("requirementType", objectMapper.createObjectNode().put("id", requirementType.id));
        body.put("priority", priority);

        ObjectNode paymentStrategy = objectMapper.createObjectNode();
        paymentStrategy.put("allowAutoPay", requirementType.isPartialPayable);
        paymentStrategy.put("allowOverdraft", requirementType.useLoanFunds);
        body.set("paymentStrategy", paymentStrategy);
        return body;
    }
}
