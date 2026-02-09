package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.common.router.DDCRouterUtil;
import com.colvir.ms.common.router.dto.DDCReadRequest;
import com.colvir.ms.sys.rms.dto.PaymentOwMassResultDto;
import com.colvir.ms.sys.rms.generated.domain.RelatedPayment;
import com.colvir.ms.sys.rms.manual.service.PaymentOwMassReportService;
import com.colvir.ms.sys.rms.manual.service.RouterService;
import com.colvir.ms.sys.rms.manual.util.RmsConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class PaymentOwMassReportServiceImpl implements PaymentOwMassReportService {

    private static final String KZ_ACCOUNT_CODE = "KZ836010013186000255";
    private static final Long CLIENT_CARD_WITHDRAWAL_TYPE_ID = 221282500634271744L; // Списание с карт клиентов

    @Inject
    Logger log;

    @Inject
    RouterService routerService;

    @Override
    public List<PaymentOwMassResultDto> getPaymentOwMassData(LocalDate date) {

        Instant startOfDay = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDay = date.plusDays(1)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant().minusNanos(1);
        log.infof("Payment Ow Mass Report for date between %s to %s", startOfDay, endOfDay);

        List<RelatedPayment> relatedPayments = RelatedPayment.list(
            "select rp from RelatedPayment rp where" +
                " rp.requirementOfRelatedPayments is not null and" +
                " rp.requirementOfRelatedPayments.isDeleted = false and" +
                " rp.requirementOfRelatedPayments.baseDocument like '%/BNK/LN/LoanContract:%' and" +
                " (rp.payment.createTime between :startOfDay and :endOfDay)" +
                " and rp.payment.withdrawalTypeId = :clientCardWithdrawalTypeId",
            Map.of(
                "startOfDay", startOfDay,
                "endOfDay", endOfDay,
                "clientCardWithdrawalTypeId", CLIENT_CARD_WITHDRAWAL_TYPE_ID
            )
        );

        Set<Long> requirementIds = relatedPayments.stream()
            .map(rp -> rp.requirementOfRelatedPayments.id)
            .collect(Collectors.toSet());

        log.infof("RelatedPayments for Payment Ow Mass Report: %s", relatedPayments.stream().map(r -> r.id).toList());
        log.infof("Requirements for Payment Ow Mass Report: %s", requirementIds);

        Map<String, Object> readRequestQuery = new HashMap<>();
        readRequestQuery.put("id", Map.of("in", requirementIds));
        DDCReadRequest ddcReadRequest = DDCRouterUtil.createDDCReadRequest(
            RmsConstants.SYS_RMS_NAMESPACE,
            "Requirement",
            List.of(
                "!*",
                "currency.!*",
                "currency.code",
                "baseDocument.!*",
                "baseDocument.code",
                "baseDocument.client.!*",
                "baseDocument.client.code",
                "baseDocument.client.taxId"
            ),
            readRequestQuery
        );

        List<ObjectNode> routerResponse = routerService.read(ddcReadRequest);

        Map<Long, PaymentOwMassResultDto> result = new HashMap<>();

        for (ObjectNode node : routerResponse) {
            Long requirementId = node.get("id").asLong();
            String currencyCode = node.get("currency").get("code").asText();
            String loanContractCode = node.get("baseDocument").get("code").asText();
            String clientCode = node.get("baseDocument").get("client").get("code").asText();

            Optional<JsonNode> taxIdOpt = Optional.of(node.get("baseDocument").get("client").path("taxId"));
            String taxId = taxIdOpt.isPresent() ? taxIdOpt.get().asText() : "";

            relatedPayments.stream()
                .filter(rp -> rp.requirementOfRelatedPayments.id.equals(requirementId))
                .map(rp -> rp.payment)
                .forEach(payment -> {
                    if (!result.containsKey(payment.id)) {
                        PaymentOwMassResultDto dto = new PaymentOwMassResultDto();
                        dto.accountCode = KZ_ACCOUNT_CODE;
                        dto.currencyCode = currencyCode;
                        dto.code = loanContractCode;
                        dto.name = clientCode;
                        dto.taxId = taxId;
                        dto.reference = payment.reference;
                        dto.amount = payment.amount;

                        result.put(payment.id, dto);
                    }
                });
        }
        log.infof("Result for Payment Ow Mass Report between %s and %s : %s", startOfDay, endOfDay, result);
        return result.values().stream().toList();
    }
}
