package com.colvir.ms.sys.rms.generated.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.RequirementHold} entity.
 */
@Schema(name = "RequirementHoldDTO", description = "Холд по требованию")
@RegisterForReflection
public class RequirementHoldDTO implements Serializable {

    public Long id;

    /**
     * номер счета
     */
    @SchemaProperty(name = "accountNumber", description = "номер счета", nullable = true)
    public String accountNumber;

    /**
     * сумма
     */
    @NotNull
    @SchemaProperty(name = "amount", description = "сумма")
    public BigDecimal amount;

    /**
     * валюта
     */
    @SchemaProperty(name = "currencyId", description = "валюта", nullable = true)
    public Long currencyId;

    /**
     * холд по online счету
     */
    @SchemaProperty(name = "holdId", description = "холд по online счету", nullable = true)
    public Long holdId;

    /**
     * является удалённым объектом
     */
    @SchemaProperty(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * рефернс холда во внешней системе
     */
    @SchemaProperty(name = "reference", description = "рефернс холда во внешней системе", nullable = true)
    public String reference;

    /**
     * вариант списания денег
     */
    @SchemaProperty(name = "withdrawalTypeId", description = "вариант списания денег", nullable = true)
    public Long withdrawalTypeId;

    public Long requirementOfAssignedHoldsId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequirementHoldDTO)) {
            return false;
        }

        return id != null && id.equals(((RequirementHoldDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "RequirementHoldDTO(" +
            "accountNumber='" + accountNumber + "', " +
            "amount='" + amount + "', " +
            "currencyId='" + currencyId + "', " +
            "holdId='" + holdId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "reference='" + reference + "', " +
            "withdrawalTypeId='" + withdrawalTypeId + "'" +
            ")";
    }

    public RequirementHoldDTO() {
    }

    public RequirementHoldDTO(Long id) {
        this.id = id;
    }
}
