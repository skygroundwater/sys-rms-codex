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
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.Requirement000PayerDetails} entity.
 */
@Schema(name = "Requirement000PayerDetailsDTO", description = "Объект-значение")
@RegisterForReflection
public class Requirement000PayerDetailsDTO implements Serializable {

    public Long id;

    /**
     * online счет клиента
     */
    @Schema(name = "accountId", description = "online счет клиента", nullable = true)
    public Long accountId;

    /**
     * оплата активна
     */
    @Schema(name = "active", description = "оплата активна", nullable = true)
    public Boolean active;

    /**
     * Возможна автоматическая оплата
     */
    @Schema(name = "autoPay", description = "Возможна автоматическая оплата", nullable = true)
    public Boolean autoPay;

    /**
     * номер счета во внешней системе
     */
    @Schema(name = "externalAccount", description = "номер счета во внешней системе", nullable = true)
    public String externalAccount;

    /**
     * является удалённым объектом
     */
    @Schema(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * порядковый номер
     */
    @Schema(name = "num", description = "порядковый номер", nullable = true)
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
