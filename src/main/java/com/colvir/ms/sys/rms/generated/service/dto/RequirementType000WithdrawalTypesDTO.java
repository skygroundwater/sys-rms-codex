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
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.RequirementType000WithdrawalTypes} entity.
 */
@Schema(name = "RequirementType000WithdrawalTypesDTO", description = "Вид требования: варианты списания")
@RegisterForReflection
public class RequirementType000WithdrawalTypesDTO implements Serializable {

    public Long id;

    /**
     * варианты списания
     */
    @NotNull
    @SchemaProperty(name = "referenceId", description = "варианты списания")
    public Long referenceId;

    public Long requirementTypeId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequirementType000WithdrawalTypesDTO)) {
            return false;
        }

        return id != null && id.equals(((RequirementType000WithdrawalTypesDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "RequirementType000WithdrawalTypesDTO(" +
            "referenceId='" + referenceId + "'" +
            ")";
    }

    public RequirementType000WithdrawalTypesDTO() {
    }

    public RequirementType000WithdrawalTypesDTO(Long id) {
        this.id = id;
    }
}
