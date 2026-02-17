package com.colvir.ms.sys.rms.generated.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.RequirementRefundingPayment} entity.
 */
@Schema(name = "RequirementRefundingPaymentDTO", description = "Связанный возвратный платеж")
@RegisterForReflection
public class RequirementRefundingPaymentDTO implements Serializable {

    public Long id;

    /**
     * сумма распределения
     */
    @SchemaProperty(name = "distributionAmount", description = "сумма распределения", nullable = true)
    public BigDecimal distributionAmount;

    /**
     * является удалённым объектом
     */
    @SchemaProperty(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * сумма возвратного платежа
     */
    @SchemaProperty(name = "paymentAmount", description = "сумма возвратного платежа", nullable = true)
    public BigDecimal paymentAmount;

    public Long refundingPaymentId;
    public Long requirementOfRefundingPaymentsId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequirementRefundingPaymentDTO)) {
            return false;
        }

        return id != null && id.equals(((RequirementRefundingPaymentDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "RequirementRefundingPaymentDTO(" +
            "distributionAmount='" + distributionAmount + "', " +
            "isDeleted='" + isDeleted + "', " +
            "paymentAmount='" + paymentAmount + "'" +
            ")";
    }

    public RequirementRefundingPaymentDTO() {
    }

    public RequirementRefundingPaymentDTO(Long id) {
        this.id = id;
    }
}
