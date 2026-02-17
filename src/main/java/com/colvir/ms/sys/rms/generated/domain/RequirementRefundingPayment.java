package com.colvir.ms.sys.rms.generated.domain;

import com.colvir.ms.common.generator.id.runtime.service.IdGeneratorService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Связанный возвратный платеж
 */
@Entity
@Table(name = "requirement_refunding_payment")
@RegisterForReflection
public class RequirementRefundingPayment extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * сумма распределения
     */
    @Column(name = "distribution_amount")
    public BigDecimal distributionAmount;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * сумма возвратного платежа
     */
    @Column(name = "payment_amount")
    public BigDecimal paymentAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refunding_payment_id")
    @JsonIgnore
    public RefundingPayment refundingPayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_of_refunding_payments_id")
    @JsonIgnore
    public Requirement requirementOfRefundingPayments;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequirementRefundingPayment)) {
            return false;
        }
        return id != null && id.equals(((RequirementRefundingPayment) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "RequirementRefundingPayment(" +
            "id='" + id + "', " +
            "distributionAmount='" + distributionAmount + "', " +
            "isDeleted='" + isDeleted + "', " +
            "paymentAmount='" + paymentAmount + "'" +
            ")";
    }

    private static IdGeneratorService idGeneratorService__ = null;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            if (idGeneratorService__ == null) {
                idGeneratorService__ = CDI.current().select(IdGeneratorService.class).get();
            }
            this.id = idGeneratorService__.next();
        }
        isDeleted = isDeleted == null ? false : isDeleted;
    }

    /**
     * Updates the current state of the entity in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the current entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @return the updated RequirementRefundingPayment entity
     * @throws IllegalArgumentException if the given RequirementRefundingPayment entity is null, or its id is null, or no existing entity with the same id is found
     */
    public RequirementRefundingPayment update() {
        return update(this);
    }

    /**
     * Persists or updates the current state of the entity in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the current entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @return the persisted or updated RequirementRefundingPayment entity
     * @throws IllegalArgumentException if the given RequirementRefundingPayment entity is null
     */
    public RequirementRefundingPayment persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given RequirementRefundingPayment entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirementRefundingPayment the RequirementRefundingPayment entity to be updated
     * @return the updated RequirementRefundingPayment entity
     * @throws IllegalArgumentException if the given RequirementRefundingPayment entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static RequirementRefundingPayment update(RequirementRefundingPayment requirementRefundingPayment) {
        if (requirementRefundingPayment == null) {
            throw new IllegalArgumentException("requirementRefundingPayment can't be null");
        }
        if (requirementRefundingPayment.id == null) {
            throw new IllegalArgumentException("requirementRefundingPayment id must be specified");
        }
        if (RequirementRefundingPayment.<RequirementRefundingPayment>findById(requirementRefundingPayment.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", requirementRefundingPayment.id));
        }
        final RequirementRefundingPayment merged = RequirementRefundingPayment.getEntityManager().merge(requirementRefundingPayment);
        RequirementRefundingPayment.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given RequirementRefundingPayment entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirementRefundingPayment the RequirementRefundingPayment entity to be persisted or updated
     * @return the persisted or updated RequirementRefundingPayment entity
     * @throws IllegalArgumentException if the given RequirementRefundingPayment entity is null
     */
    public static RequirementRefundingPayment persistOrUpdate(RequirementRefundingPayment requirementRefundingPayment) {
        if (requirementRefundingPayment == null) {
            throw new IllegalArgumentException("requirementRefundingPayment can't be null");
        }
        RequirementRefundingPayment entity = null;
        if (requirementRefundingPayment.id != null) {
            entity = RequirementRefundingPayment.<RequirementRefundingPayment>findById(requirementRefundingPayment.id);
        }
        if (requirementRefundingPayment.id == null || entity == null) {
            persist(requirementRefundingPayment);
            return requirementRefundingPayment;
        } else {
            final RequirementRefundingPayment merged = RequirementRefundingPayment.getEntityManager().merge(requirementRefundingPayment);
            RequirementRefundingPayment.getEntityManager().flush();
            return merged;
        }
    }
}
