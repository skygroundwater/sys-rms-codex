package com.colvir.ms.sys.rms.generated.domain;

import com.colvir.ms.common.generator.id.runtime.service.IdGeneratorService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;

/**
 * Канал оплаты
 */
@Entity
@Table(name = "payment_channel")
@RegisterForReflection
public class PaymentChannel extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * используется
     */
    @Column(name = "active")
    public Boolean active;

    /**
     * обозначение
     */
    @NotNull
    @Size(max = 511)
    @Column(name = "code", nullable = false, unique = true)
    public String code;

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
     * исполнитель
     */
    @Column(name = "executor_id")
    public Long executorId;

    /**
     * возможна автоматическая оплата
     */
    @Column(name = "is_auto_payment")
    public Boolean isAutoPayment;

    /**
     * возможно создание платежа
     */
    @Column(name = "is_create_payment")
    public Boolean isCreatePayment;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * возможна установка холдов
     */
    @Column(name = "is_init_holds")
    public Boolean isInitHolds;

    /**
     * наименование
     */
    @NotNull
    @Size(max = 511)
    @Column(name = "name", nullable = false, unique = true)
    public String name;

    /**
     * класс платежей
     */
    @Size(max = 511)
    @Column(name = "payment_class")
    public String paymentClass;

    /**
     * номер версии
     */
    @Version
    @Column(name = "version")
    public Integer version = 0;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentChannel)) {
            return false;
        }
        return id != null && id.equals(((PaymentChannel) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "PaymentChannel(" +
            "id='" + id + "', " +
            "active='" + active + "', " +
            "code='" + code + "', " +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "isAutoPayment='" + isAutoPayment + "', " +
            "isCreatePayment='" + isCreatePayment + "', " +
            "isDeleted='" + isDeleted + "', " +
            "isInitHolds='" + isInitHolds + "', " +
            "name='" + name + "', " +
            "paymentClass='" + paymentClass + "', " +
            "version='" + version + "'" +
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
        active = active == null ? false : active;
        isAutoPayment = isAutoPayment == null ? false : isAutoPayment;
        isCreatePayment = isCreatePayment == null ? false : isCreatePayment;
        isDeleted = isDeleted == null ? false : isDeleted;
        isInitHolds = isInitHolds == null ? false : isInitHolds;
    }

    /**
     * Updates the current state of the entity in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the current entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @return the updated PaymentChannel entity
     * @throws IllegalArgumentException if the given PaymentChannel entity is null, or its id is null, or no existing entity with the same id is found
     */
    public PaymentChannel update() {
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
     * @return the persisted or updated PaymentChannel entity
     * @throws IllegalArgumentException if the given PaymentChannel entity is null
     */
    public PaymentChannel persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given PaymentChannel entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param paymentChannel the PaymentChannel entity to be updated
     * @return the updated PaymentChannel entity
     * @throws IllegalArgumentException if the given PaymentChannel entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static PaymentChannel update(PaymentChannel paymentChannel) {
        if (paymentChannel == null) {
            throw new IllegalArgumentException("paymentChannel can't be null");
        }
        if (paymentChannel.id == null) {
            throw new IllegalArgumentException("paymentChannel id must be specified");
        }
        if (PaymentChannel.<PaymentChannel>findById(paymentChannel.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", paymentChannel.id));
        }
        final PaymentChannel merged = PaymentChannel.getEntityManager().merge(paymentChannel);
        PaymentChannel.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given PaymentChannel entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param paymentChannel the PaymentChannel entity to be persisted or updated
     * @return the persisted or updated PaymentChannel entity
     * @throws IllegalArgumentException if the given PaymentChannel entity is null
     */
    public static PaymentChannel persistOrUpdate(PaymentChannel paymentChannel) {
        if (paymentChannel == null) {
            throw new IllegalArgumentException("paymentChannel can't be null");
        }
        PaymentChannel entity = null;
        if (paymentChannel.id != null) {
            entity = PaymentChannel.<PaymentChannel>findById(paymentChannel.id);
        }
        if (paymentChannel.id == null || entity == null) {
            persist(paymentChannel);
            return paymentChannel;
        } else {
            final PaymentChannel merged = PaymentChannel.getEntityManager().merge(paymentChannel);
            PaymentChannel.getEntityManager().flush();
            return merged;
        }
    }
}
