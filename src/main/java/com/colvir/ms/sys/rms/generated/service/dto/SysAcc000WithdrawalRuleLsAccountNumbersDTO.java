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
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.SysAcc000WithdrawalRuleLsAccountNumbers} entity.
 */
@RegisterForReflection
public class SysAcc000WithdrawalRuleLsAccountNumbersDTO implements Serializable {

    public Long id;

    /**
     * значение
     */
    @NotNull
    @Schema(name = "value", description = "значение")
    public String value;

    public Long ownerId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysAcc000WithdrawalRuleLsAccountNumbersDTO)) {
            return false;
        }

        return id != null && id.equals(((SysAcc000WithdrawalRuleLsAccountNumbersDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "SysAcc000WithdrawalRuleLsAccountNumbersDTO(" +
            "value='" + value + "'" +
            ")";
    }

    public SysAcc000WithdrawalRuleLsAccountNumbersDTO() {
    }

    public SysAcc000WithdrawalRuleLsAccountNumbersDTO(Long id) {
        this.id = id;
    }
}
