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
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.RelatedPayment} entity.
 */
@Schema(name = "RelatedPaymentDTO", description = "Связанный платеж ")
@RegisterForReflection
public class RelatedPaymentDTO implements Serializable {

    public Long id;

    /**
     * сумма исполнения
     */
    @Schema(name = "amount", description = "сумма исполнения", nullable = true)
    public BigDecimal amount;

    /**
     * сумма платежа
     */
    @Schema(name = "amountOfPayment", description = "сумма платежа", nullable = true)
    public BigDecimal amountOfPayment;

    /**
     * является удалённым объектом
     */
    @Schema(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    public Long requirementOfRelatedPaymentsId;
    public Long paymentId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RelatedPaymentDTO)) {
            return false;
        }

        return id != null && id.equals(((RelatedPaymentDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "RelatedPaymentDTO(" +
            "amount='" + amount + "', " +
            "amountOfPayment='" + amountOfPayment + "', " +
            "isDeleted='" + isDeleted + "'" +
            ")";
    }

    public RelatedPaymentDTO() {
    }

    public RelatedPaymentDTO(Long id) {
        this.id = id;
    }
}
