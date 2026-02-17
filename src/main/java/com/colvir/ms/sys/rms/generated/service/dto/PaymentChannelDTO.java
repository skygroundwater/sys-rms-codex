package com.colvir.ms.sys.rms.generated.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import java.io.Serializable;
import java.time.Instant;

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
    @SchemaProperty(name = "active", description = "используется", nullable = true)
    public Boolean active;

    /**
     * обозначение
     */
    @NotNull
    @SchemaProperty(name = "code", description = "обозначение")
    public String code;

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
     * исполнитель
     */
    @SchemaProperty(name = "executorId", description = "исполнитель", nullable = true)
    public Long executorId;

    /**
     * возможна автоматическая оплата
     */
    @SchemaProperty(name = "isAutoPayment", description = "возможна автоматическая оплата", nullable = true)
    public Boolean isAutoPayment;

    /**
     * возможно создание платежа
     */
    @SchemaProperty(name = "isCreatePayment", description = "возможно создание платежа", nullable = true)
    public Boolean isCreatePayment;

    /**
     * является удалённым объектом
     */
    @SchemaProperty(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * возможна установка холдов
     */
    @SchemaProperty(name = "isInitHolds", description = "возможна установка холдов", nullable = true)
    public Boolean isInitHolds;

    /**
     * наименование
     */
    @NotNull
    @SchemaProperty(name = "name", description = "наименование")
    public String name;

    /**
     * класс платежей
     */
    @SchemaProperty(name = "paymentClass", description = "класс платежей", nullable = true)
    public String paymentClass;

    /**
     * номер версии
     */
    @SchemaProperty(name = "version", description = "номер версии", nullable = true)
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
