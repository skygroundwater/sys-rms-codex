package com.colvir.ms.sys.rms.generated.service.dto;

import com.colvir.ms.sys.rms.generated.domain.enumeration.*;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    @Schema(name = "amount", description = "сумма", nullable = true)
    public BigDecimal amount;

    /**
     * дата и время создания
     */
    @Schema(name = "creationTime", description = "дата и время создания", nullable = true)
    public Instant creationTime;

    /**
     * валюта
     */
    @Schema(name = "currencyId", description = "валюта", nullable = true)
    public Long currencyId;

    /**
     * является удалённым объектом
     */
    @Schema(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * вид связи с платежом
     */
    @NotNull
    @Schema(name = "paymentLinkType", description = "вид связи с платежом")
    public PaymentLinkType paymentLinkType;

    /**
     * результат оплаты
     */
    @NotNull
    @Schema(name = "paymentResult", description = "результат оплаты")
    public PaymentResult paymentResult;

    /**
     * вариант выплаты
     */
    @Schema(name = "provisionMethodId", description = "вариант выплаты", nullable = true)
    public Long provisionMethodId;

    /**
     * референс платежа
     */
    @Schema(name = "reference", description = "референс платежа", nullable = true)
    public String reference;

    /**
     * дата и время возврата
     */
    @Schema(name = "refundTime", description = "дата и время возврата", nullable = true)
    public Instant refundTime;

    /**
     * дата валютирования
     */
    @Schema(name = "valueDate", description = "дата валютирования", nullable = true)
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
