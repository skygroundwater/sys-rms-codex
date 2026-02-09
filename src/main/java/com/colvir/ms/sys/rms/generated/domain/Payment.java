package com.colvir.ms.sys.rms.generated.domain;

import com.colvir.ms.common.generator.id.runtime.service.IdGeneratorService;
import com.colvir.ms.sys.rms.generated.domain.enumeration.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Types;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import com.colvir.ms.generated.svc.base.config.hibernate.HibernatePeriodStringConverter;
import com.colvir.ms.generated.svc.base.config.hibernate.HibernateDurationStringConverter;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Платеж
 */
@Entity
@Table(name = "payment")
@RegisterForReflection
public class Payment extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * сумма
     */
    @Column(name = "amount")
    public BigDecimal amount;

    /**
     * время отмены
     */
    @Column(name = "cancel_time")
    public Instant cancelTime;

    /**
     * время создания
     */
    @Column(name = "create_time")
    public Instant createTime;

    /**
     * валюта
     */
    @Column(name = "currency_id")
    public Long currencyId;

    /**
     * дата редактирования
     */
    @Column(name = "edit_date")
    public Instant editDate;

    /**
     * редактор
     */
    @Column(name = "editor_id")
    public Long editorId;

    /**
     * время исполнения
     */
    @Column(name = "exec_time")
    public Instant execTime;

    /**
     * исполнитель
     */
    @Column(name = "executor_id")
    public Long executorId;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * платеж
     */
    @Size(max = 511)
    @Column(name = "payment")
    public String payment;

    /**
     * вид связи с платежом
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_link_type")
    public PaymentLinkType paymentLinkType;

    /**
     * результат оплаты
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_result")
    public PaymentResult paymentResult;

    /**
     * референс платежа
     */
    @Size(max = 511)
    @Column(name = "reference")
    public String reference;

    /**
     * дата валютирования
     */
    @Column(name = "val_date")
    public LocalDate valDate;

    /**
     * номер версии
     */
    @Version
    @Column(name = "version")
    public Integer version = 0;

    /**
     * вариант списания денег
     */
    @Column(name = "withdrawal_type_id")
    public Long withdrawalTypeId;

    @OneToMany(mappedBy = "paymentOfRefundPayments")
    public Set<RefundingPayment> refundPayments = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public Integer getVersion() {
        return this.version;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "Payment(" +
            "id='" + id + "', " +
            "amount='" + amount + "', " +
            "cancelTime='" + cancelTime + "', " +
            "createTime='" + createTime + "', " +
            "currencyId='" + currencyId + "', " +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "execTime='" + execTime + "', " +
            "executorId='" + executorId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "payment='" + payment + "', " +
            "paymentLinkType='" + paymentLinkType + "', " +
            "paymentResult='" + paymentResult + "', " +
            "reference='" + reference + "', " +
            "valDate='" + valDate + "', " +
            "version='" + version + "', " +
            "withdrawalTypeId='" + withdrawalTypeId + "'" +
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
     * @return the updated Payment entity
     * @throws IllegalArgumentException if the given Payment entity is null, or its id is null, or no existing entity with the same id is found
     */
    public Payment update() {
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
     * @return the persisted or updated Payment entity
     * @throws IllegalArgumentException if the given Payment entity is null
     */
    public Payment persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given Payment entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param payment the Payment entity to be updated
     * @return the updated Payment entity
     * @throws IllegalArgumentException if the given Payment entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static Payment update(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("payment can't be null");
        }
        if (payment.id == null) {
            throw new IllegalArgumentException("payment id must be specified");
        }
        if (Payment.<Payment>findById(payment.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", payment.id));
        }
        final Payment merged = Payment.getEntityManager().merge(payment);
        Payment.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given Payment entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param payment the Payment entity to be persisted or updated
     * @return the persisted or updated Payment entity
     * @throws IllegalArgumentException if the given Payment entity is null
     */
    public static Payment persistOrUpdate(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("payment can't be null");
        }
        Payment entity = null;
        if (payment.id != null) {
            entity = Payment.<Payment>findById(payment.id);
        }
        if (payment.id == null || entity == null) {
            persist(payment);
            return payment;
        } else {
            final Payment merged = Payment.getEntityManager().merge(payment);
            Payment.getEntityManager().flush();
            return merged;
        }
    }
}
