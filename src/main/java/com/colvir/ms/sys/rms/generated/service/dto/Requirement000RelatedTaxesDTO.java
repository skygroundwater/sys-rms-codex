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
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.Requirement000RelatedTaxes} entity.
 */
@Schema(name = "Requirement000RelatedTaxesDTO", description = "Объект-значение")
@RegisterForReflection
public class Requirement000RelatedTaxesDTO implements Serializable {

    public Long id;

    /**
     * сумма
     */
    @SchemaProperty(name = "amount", description = "сумма", nullable = true)
    public BigDecimal amount;

    /**
     * является удалённым объектом
     */
    @SchemaProperty(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * вид налога
     */
    @SchemaProperty(name = "typeOfTaxId", description = "вид налога", nullable = true)
    public Long typeOfTaxId;

    public Long requirementOfRelatedTaxesId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Requirement000RelatedTaxesDTO)) {
            return false;
        }

        return id != null && id.equals(((Requirement000RelatedTaxesDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "Requirement000RelatedTaxesDTO(" +
            "amount='" + amount + "', " +
            "isDeleted='" + isDeleted + "', " +
            "typeOfTaxId='" + typeOfTaxId + "'" +
            ")";
    }

    public Requirement000RelatedTaxesDTO() {
    }

    public Requirement000RelatedTaxesDTO(Long id) {
        this.id = id;
    }
}
