package com.colvir.ms.sys.rms.generated.service.dto;

import com.colvir.ms.sys.rms.generated.domain.enumeration.PaymentLinkType;
import com.colvir.ms.sys.rms.generated.domain.enumeration.PaymentResult;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.RefundingPayment} entity.
 */
@Schema(name = "RefundingPaymentDTO", description = "Возвратный платеж")
@RegisterForReflection
public class RefundingPaymentDTO implements Serializable {

    public Long id;

    /**
     * сумма
     */
    @SchemaProperty(name = "amount", description = "сумма", nullable = true)
    public BigDecimal amount;

    /**
     * дата и время создания
     */
    @SchemaProperty(name = "creationTime", description = "дата и время создания", nullable = true)
    public Instant creationTime;

    /**
     * валюта
     */
    @SchemaProperty(name = "currencyId", description = "валюта", nullable = true)
    public Long currencyId;

    /**
     * является удалённым объектом
     */
    @SchemaProperty(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * вид связи с платежом
     */
    @NotNull
    @SchemaProperty(name = "paymentLinkType", description = "вид связи с платежом")
    public PaymentLinkType paymentLinkType;

    /**
     * результат оплаты
     */
    @NotNull
    @SchemaProperty(name = "paymentResult", description = "результат оплаты")
    public PaymentResult paymentResult;

    /**
     * вариант выплаты
     */
    @SchemaProperty(name = "provisionMethodId", description = "вариант выплаты", nullable = true)
    public Long provisionMethodId;

    /**
     * референс платежа
     */
    @SchemaProperty(name = "reference", description = "референс платежа", nullable = true)
    public String reference;

    /**
     * дата и время возврата
     */
    @SchemaProperty(name = "refundTime", description = "дата и время возврата", nullable = true)
    public Instant refundTime;

    /**
     * дата валютирования
     */
    @SchemaProperty(name = "valueDate", description = "дата валютирования", nullable = true)
    public LocalDate valueDate;

    public Long paymentOfRefundPaymentsId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefundingPaymentDTO)) {
            return false;
        }

        return id != null && id.equals(((RefundingPaymentDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "RefundingPaymentDTO(" +
            "amount='" + amount + "', " +
            "creationTime='" + creationTime + "', " +
            "currencyId='" + currencyId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "paymentLinkType='" + paymentLinkType + "', " +
            "paymentResult='" + paymentResult + "', " +
            "provisionMethodId='" + provisionMethodId + "', " +
            "reference='" + reference + "', " +
            "refundTime='" + refundTime + "', " +
            "valueDate='" + valueDate + "'" +
            ")";
    }

    public RefundingPaymentDTO() {
    }

    public RefundingPaymentDTO(Long id) {
        this.id = id;
    }
}
