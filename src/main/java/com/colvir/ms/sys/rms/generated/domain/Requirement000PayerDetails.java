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
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * Объект-значение
 */
@Entity
@Table(name = "requirement_000_payer_details")
@RegisterForReflection
public class Requirement000PayerDetails extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * online счет клиента
     */
    @Column(name = "account_id")
    public Long accountId;

    /**
     * оплата активна
     */
    @Column(name = "active")
    public Boolean active;

    /**
     * Возможна автоматическая оплата
     */
    @Column(name = "auto_pay")
    public Boolean autoPay;

    /**
     * номер счета во внешней системе
     */
    @Size(max = 511)
    @Column(name = "external_account")
    public String externalAccount;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * порядковый номер
     */
    @Column(name = "num")
    public Long num;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_channel_id")
    @JsonIgnore
    public PaymentChannel paymentChannel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_of_payer_details_id")
    @JsonIgnore
    public Requirement requirementOfPayerDetails;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Requirement000PayerDetails)) {
            return false;
        }
        return id != null && id.equals(((Requirement000PayerDetails) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "Requirement000PayerDetails(" +
            "id='" + id + "', " +
            "accountId='" + accountId + "', " +
            "active='" + active + "', " +
            "autoPay='" + autoPay + "', " +
            "externalAccount='" + externalAccount + "', " +
            "isDeleted='" + isDeleted + "', " +
            "num='" + num + "'" +
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
        autoPay = autoPay == null ? false : autoPay;
        isDeleted = isDeleted == null ? false : isDeleted;
    }

    /**
     * Updates the current state of the entity in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the current entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @return the updated Requirement000PayerDetails entity
     * @throws IllegalArgumentException if the given Requirement000PayerDetails entity is null, or its id is null, or no existing entity with the same id is found
     */
    public Requirement000PayerDetails update() {
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
     * @return the persisted or updated Requirement000PayerDetails entity
     * @throws IllegalArgumentException if the given Requirement000PayerDetails entity is null
     */
    public Requirement000PayerDetails persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given Requirement000PayerDetails entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirement000PayerDetails the Requirement000PayerDetails entity to be updated
     * @return the updated Requirement000PayerDetails entity
     * @throws IllegalArgumentException if the given Requirement000PayerDetails entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static Requirement000PayerDetails update(Requirement000PayerDetails requirement000PayerDetails) {
        if (requirement000PayerDetails == null) {
            throw new IllegalArgumentException("requirement000PayerDetails can't be null");
        }
        if (requirement000PayerDetails.id == null) {
            throw new IllegalArgumentException("requirement000PayerDetails id must be specified");
        }
        if (Requirement000PayerDetails.<Requirement000PayerDetails>findById(requirement000PayerDetails.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", requirement000PayerDetails.id));
        }
        final Requirement000PayerDetails merged = Requirement000PayerDetails.getEntityManager().merge(requirement000PayerDetails);
        Requirement000PayerDetails.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given Requirement000PayerDetails entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirement000PayerDetails the Requirement000PayerDetails entity to be persisted or updated
     * @return the persisted or updated Requirement000PayerDetails entity
     * @throws IllegalArgumentException if the given Requirement000PayerDetails entity is null
     */
    public static Requirement000PayerDetails persistOrUpdate(Requirement000PayerDetails requirement000PayerDetails) {
        if (requirement000PayerDetails == null) {
            throw new IllegalArgumentException("requirement000PayerDetails can't be null");
        }
        Requirement000PayerDetails entity = null;
        if (requirement000PayerDetails.id != null) {
            entity = Requirement000PayerDetails.<Requirement000PayerDetails>findById(requirement000PayerDetails.id);
        }
        if (requirement000PayerDetails.id == null || entity == null) {
            persist(requirement000PayerDetails);
            return requirement000PayerDetails;
        } else {
            final Requirement000PayerDetails merged = Requirement000PayerDetails.getEntityManager().merge(requirement000PayerDetails);
            Requirement000PayerDetails.getEntityManager().flush();
            return merged;
        }
    }
}
