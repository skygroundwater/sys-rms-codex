package com.colvir.ms.sys.rms.generated.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import java.io.Serializable;

/**
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.Requirement000PayerDetails} entity.
 */
@Schema(name = "Requirement000PayerDetailsDTO", description = "Объект-значение")
@RegisterForReflection
public class Requirement000PayerDetailsDTO implements Serializable {

    public Long id;

    /**
     * online счет клиента
     */
    @SchemaProperty(name = "accountId", description = "online счет клиента", nullable = true)
    public Long accountId;

    /**
     * оплата активна
     */
    @SchemaProperty(name = "active", description = "оплата активна", nullable = true)
    public Boolean active;

    /**
     * Возможна автоматическая оплата
     */
    @SchemaProperty(name = "autoPay", description = "Возможна автоматическая оплата", nullable = true)
    public Boolean autoPay;

    /**
     * номер счета во внешней системе
     */
    @SchemaProperty(name = "externalAccount", description = "номер счета во внешней системе", nullable = true)
    public String externalAccount;

    /**
     * является удалённым объектом
     */
    @SchemaProperty(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * порядковый номер
     */
    @SchemaProperty(name = "num", description = "порядковый номер", nullable = true)
    public Long num;

    public Long paymentChannelId;
    public Long requirementOfPayerDetailsId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Requirement000PayerDetailsDTO)) {
            return false;
        }

        return id != null && id.equals(((Requirement000PayerDetailsDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "Requirement000PayerDetailsDTO(" +
            "accountId='" + accountId + "', " +
            "active='" + active + "', " +
            "autoPay='" + autoPay + "', " +
            "externalAccount='" + externalAccount + "', " +
            "isDeleted='" + isDeleted + "', " +
            "num='" + num + "'" +
            ")";
    }

    public Requirement000PayerDetailsDTO() {
    }

    public Requirement000PayerDetailsDTO(Long id) {
        this.id = id;
    }
}
