package com.colvir.ms.sys.rms.generated.service.dto;

import com.colvir.ms.sys.rms.generated.domain.enumeration.*;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
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
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.Payment} entity.
 */
@Schema(name = "PaymentDTO", description = "Платеж")
@RegisterForReflection
public class PaymentDTO implements Serializable {

    public Long id;

    /**
     * сумма
     */
    @SchemaProperty(name = "amount", description = "сумма", nullable = true)
    public BigDecimal amount;

    /**
     * время отмены
     */
    @SchemaProperty(name = "cancelTime", description = "время отмены", nullable = true)
    public Instant cancelTime;

    /**
     * время создания
     */
    @SchemaProperty(name = "createTime", description = "время создания", nullable = true)
    public Instant createTime;

    /**
     * валюта
     */
    @SchemaProperty(name = "currencyId", description = "валюта", nullable = true)
    public Long currencyId;

    /**
     * дата редактирования
     */
    @SchemaProperty(name = "editDate", description = "дата редактирования", nullable = true)
    public Instant editDate;

    /**
     * редактор
     */
    @SchemaProperty(name = "editorId", description = "редактор", nullable = true)
    public Long editorId;

    /**
     * время исполнения
     */
    @SchemaProperty(name = "execTime", description = "время исполнения", nullable = true)
    public Instant execTime;

    /**
     * исполнитель
     */
    @SchemaProperty(name = "executorId", description = "исполнитель", nullable = true)
    public Long executorId;

    /**
     * является удалённым объектом
     */
    @SchemaProperty(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * платеж
     */
    @SchemaProperty(name = "payment", description = "платеж", nullable = true)
    public String payment;

    /**
     * вид связи с платежом
     */
    @SchemaProperty(name = "paymentLinkType", description = "вид связи с платежом", nullable = true)
    public PaymentLinkType paymentLinkType;

    /**
     * результат оплаты
     */
    @SchemaProperty(name = "paymentResult", description = "результат оплаты", nullable = true)
    public PaymentResult paymentResult;

    /**
     * референс платежа
     */
    @SchemaProperty(name = "reference", description = "референс платежа", nullable = true)
    public String reference;

    /**
     * дата валютирования
     */
    @SchemaProperty(name = "valDate", description = "дата валютирования", nullable = true)
    public LocalDate valDate;

    /**
     * номер версии
     */
    @SchemaProperty(name = "version", description = "номер версии", nullable = true)
    public Integer version;

    /**
     * вариант списания денег
     */
    @SchemaProperty(name = "withdrawalTypeId", description = "вариант списания денег", nullable = true)
    public Long withdrawalTypeId;


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        return id != null && id.equals(((PaymentDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "PaymentDTO(" +
            "amount='" + amount + "', " +
            "cancelTime='" + cancelTime + "', " +
            "createTime='" + createTime + "', " +
            "currencyId='" + currencyId + "', " +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "execTime='" + execTime + "', " +
            "executorId='" + executorId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "payment='" + payment + "', " +
            "paymentLinkType='" + paymentLinkType + "', " +
            "paymentResult='" + paymentResult + "', " +
            "reference='" + reference + "', " +
            "valDate='" + valDate + "', " +
            "version='" + version + "', " +
            "withdrawalTypeId='" + withdrawalTypeId + "'" +
            ")";
    }

    public PaymentDTO() {
    }

    public PaymentDTO(Long id) {
        this.id = id;
    }
}
