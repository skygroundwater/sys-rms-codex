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
 * Связанный платеж
 */
@Entity
@Table(name = "related_payment")
@RegisterForReflection
public class RelatedPayment extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * сумма исполнения
     */
    @Column(name = "amount")
    public BigDecimal amount;

    /**
     * сумма платежа
     */
    @Column(name = "amount_of_payment")
    public BigDecimal amountOfPayment;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_of_related_payments_id")
    @JsonIgnore
    public Requirement requirementOfRelatedPayments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @JsonIgnore
    public Payment payment;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RelatedPayment)) {
            return false;
        }
        return id != null && id.equals(((RelatedPayment) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "RelatedPayment(" +
            "id='" + id + "', " +
            "amount='" + amount + "', " +
            "amountOfPayment='" + amountOfPayment + "', " +
            "isDeleted='" + isDeleted + "'" +
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
     * @return the updated RelatedPayment entity
     * @throws IllegalArgumentException if the given RelatedPayment entity is null, or its id is null, or no existing entity with the same id is found
     */
    public RelatedPayment update() {
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
     * @return the persisted or updated RelatedPayment entity
     * @throws IllegalArgumentException if the given RelatedPayment entity is null
     */
    public RelatedPayment persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given RelatedPayment entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param relatedPayment the RelatedPayment entity to be updated
     * @return the updated RelatedPayment entity
     * @throws IllegalArgumentException if the given RelatedPayment entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static RelatedPayment update(RelatedPayment relatedPayment) {
        if (relatedPayment == null) {
            throw new IllegalArgumentException("relatedPayment can't be null");
        }
        if (relatedPayment.id == null) {
            throw new IllegalArgumentException("relatedPayment id must be specified");
        }
        if (RelatedPayment.<RelatedPayment>findById(relatedPayment.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", relatedPayment.id));
        }
        final RelatedPayment merged = RelatedPayment.getEntityManager().merge(relatedPayment);
        RelatedPayment.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given RelatedPayment entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param relatedPayment the RelatedPayment entity to be persisted or updated
     * @return the persisted or updated RelatedPayment entity
     * @throws IllegalArgumentException if the given RelatedPayment entity is null
     */
    public static RelatedPayment persistOrUpdate(RelatedPayment relatedPayment) {
        if (relatedPayment == null) {
            throw new IllegalArgumentException("relatedPayment can't be null");
        }
        RelatedPayment entity = null;
        if (relatedPayment.id != null) {
            entity = RelatedPayment.<RelatedPayment>findById(relatedPayment.id);
        }
        if (relatedPayment.id == null || entity == null) {
            persist(relatedPayment);
            return relatedPayment;
        } else {
            final RelatedPayment merged = RelatedPayment.getEntityManager().merge(relatedPayment);
            RelatedPayment.getEntityManager().flush();
            return merged;
        }
    }
}
