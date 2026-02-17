package com.colvir.ms.sys.rms.generated.domain;

import com.colvir.ms.common.generator.id.runtime.service.IdGeneratorService;
import com.colvir.ms.sys.rms.generated.domain.enumeration.PaymentLinkType;
import com.colvir.ms.sys.rms.generated.domain.enumeration.PaymentResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Возвратный платеж
 */
@Entity
@Table(name = "refunding_payment")
@RegisterForReflection
public class RefundingPayment extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * сумма
     */
    @Column(name = "amount")
    public BigDecimal amount;

    /**
     * дата и время создания
     */
    @Column(name = "creation_time")
    public Instant creationTime;

    /**
     * валюта
     */
    @Column(name = "currency_id")
    public Long currencyId;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * вид связи с платежом
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_link_type", nullable = false)
    public PaymentLinkType paymentLinkType;

    /**
     * результат оплаты
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_result", nullable = false)
    public PaymentResult paymentResult;

    /**
     * вариант выплаты
     */
    @Column(name = "provision_method_id")
    public Long provisionMethodId;

    /**
     * референс платежа
     */
    @Size(max = 511)
    @Column(name = "reference")
    public String reference;

    /**
     * дата и время возврата
     */
    @Column(name = "refund_time")
    public Instant refundTime;

    /**
     * дата валютирования
     */
    @Column(name = "value_date")
    public LocalDate valueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_of_refund_payments_id")
    @JsonIgnore
    public Payment paymentOfRefundPayments;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefundingPayment)) {
            return false;
        }
        return id != null && id.equals(((RefundingPayment) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "RefundingPayment(" +
            "id='" + id + "', " +
            "amount='" + amount + "', " +
            "creationTime='" + creationTime + "', " +
            "currencyId='" + currencyId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "paymentLinkType='" + paymentLinkType + "', " +
            "paymentResult='" + paymentResult + "', " +
            "provisionMethodId='" + provisionMethodId + "', " +
            "reference='" + reference + "', " +
            "refundTime='" + refundTime + "', " +
            "valueDate='" + valueDate + "'" +
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
        paymentLinkType = paymentLinkType == null ? PaymentLinkType.AUTO : paymentLinkType;
        paymentResult = paymentResult == null ? PaymentResult.CANCELED : paymentResult;
    }

    /**
     * Updates the current state of the entity in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the current entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @return the updated RefundingPayment entity
     * @throws IllegalArgumentException if the given RefundingPayment entity is null, or its id is null, or no existing entity with the same id is found
     */
    public RefundingPayment update() {
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
     * @return the persisted or updated RefundingPayment entity
     * @throws IllegalArgumentException if the given RefundingPayment entity is null
     */
    public RefundingPayment persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given RefundingPayment entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param refundingPayment the RefundingPayment entity to be updated
     * @return the updated RefundingPayment entity
     * @throws IllegalArgumentException if the given RefundingPayment entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static RefundingPayment update(RefundingPayment refundingPayment) {
        if (refundingPayment == null) {
            throw new IllegalArgumentException("refundingPayment can't be null");
        }
        if (refundingPayment.id == null) {
            throw new IllegalArgumentException("refundingPayment id must be specified");
        }
        if (RefundingPayment.<RefundingPayment>findById(refundingPayment.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", refundingPayment.id));
        }
        final RefundingPayment merged = RefundingPayment.getEntityManager().merge(refundingPayment);
        RefundingPayment.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given RefundingPayment entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param refundingPayment the RefundingPayment entity to be persisted or updated
     * @return the persisted or updated RefundingPayment entity
     * @throws IllegalArgumentException if the given RefundingPayment entity is null
     */
    public static RefundingPayment persistOrUpdate(RefundingPayment refundingPayment) {
        if (refundingPayment == null) {
            throw new IllegalArgumentException("refundingPayment can't be null");
        }
        RefundingPayment entity = null;
        if (refundingPayment.id != null) {
            entity = RefundingPayment.<RefundingPayment>findById(refundingPayment.id);
        }
        if (refundingPayment.id == null || entity == null) {
            persist(refundingPayment);
            return refundingPayment;
        } else {
            final RefundingPayment merged = RefundingPayment.getEntityManager().merge(refundingPayment);
            RefundingPayment.getEntityManager().flush();
            return merged;
        }
    }
}
