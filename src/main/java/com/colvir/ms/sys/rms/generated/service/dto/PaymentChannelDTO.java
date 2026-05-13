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
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.PaymentChannel} entity.
 */
@Schema(name = "PaymentChannelDTO", description = "Канал оплаты")
@RegisterForReflection
public class PaymentChannelDTO implements Serializable {

    public Long id;

    /**
     * используется
     */
    @Schema(name = "active", description = "используется", nullable = true)
    public Boolean active;

    /**
     * обозначение
     */
    @NotNull
    @Schema(name = "code", description = "обозначение")
    public String code;

    /**
     * дата редактирования
     */
    @Schema(name = "editDate", description = "дата редактирования", nullable = true)
    public Instant editDate;

    /**
     * редактор
     */
    @Schema(name = "editorId", description = "редактор", nullable = true)
    public Long editorId;

    /**
     * исполнитель
     */
    @Schema(name = "executorId", description = "исполнитель", nullable = true)
    public Long executorId;

    /**
     * возможна автоматическая оплата
     */
    @Schema(name = "isAutoPayment", description = "возможна автоматическая оплата", nullable = true)
    public Boolean isAutoPayment;

    /**
     * возможно создание платежа
     */
    @Schema(name = "isCreatePayment", description = "возможно создание платежа", nullable = true)
    public Boolean isCreatePayment;

    /**
     * является удалённым объектом
     */
    @Schema(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * возможна установка холдов
     */
    @Schema(name = "isInitHolds", description = "возможна установка холдов", nullable = true)
    public Boolean isInitHolds;

    /**
     * наименование
     */
    @NotNull
    @Schema(name = "name", description = "наименование")
    public String name;

    /**
     * класс платежей
     */
    @Schema(name = "paymentClass", description = "класс платежей", nullable = true)
    public String paymentClass;

    /**
     * номер версии
     */
    @Schema(name = "version", description = "номер версии", nullable = true)
    public Integer version;


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentChannelDTO)) {
            return false;
        }

        return id != null && id.equals(((PaymentChannelDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "PaymentChannelDTO(" +
            "active='" + active + "', " +
            "code='" + code + "', " +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "isAutoPayment='" + isAutoPayment + "', " +
            "isCreatePayment='" + isCreatePayment + "', " +
            "isDeleted='" + isDeleted + "', " +
            "isInitHolds='" + isInitHolds + "', " +
            "name='" + name + "', " +
            "paymentClass='" + paymentClass + "', " +
            "version='" + version + "'" +
            ")";
    }

    public PaymentChannelDTO() {
    }

    public PaymentChannelDTO(Long id) {
        this.id = id;
    }
}
