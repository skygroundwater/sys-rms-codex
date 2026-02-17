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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A SysAcc000WithdrawalRuleLsAccountNumbers.
 */
@Entity
@Table(name = "sys_acc_000_withdrawal_rule_ls_account_numbers")
@RegisterForReflection
public class SysAcc000WithdrawalRuleLsAccountNumbers extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * значение
     */
    @NotNull
    @Size(max = 511)
    @Column(name = "value", nullable = false)
    public String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    public SysAcc000WithdrawalRule owner;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysAcc000WithdrawalRuleLsAccountNumbers)) {
            return false;
        }
        return id != null && id.equals(((SysAcc000WithdrawalRuleLsAccountNumbers) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "SysAcc000WithdrawalRuleLsAccountNumbers(" +
            "id='" + id + "', " +
            "value='" + value + "'" +
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
    }

    /**
     * Updates the current state of the entity in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the current entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @return the updated SysAcc000WithdrawalRuleLsAccountNumbers entity
     * @throws IllegalArgumentException if the given SysAcc000WithdrawalRuleLsAccountNumbers entity is null, or its id is null, or no existing entity with the same id is found
     */
    public SysAcc000WithdrawalRuleLsAccountNumbers update() {
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
     * @return the persisted or updated SysAcc000WithdrawalRuleLsAccountNumbers entity
     * @throws IllegalArgumentException if the given SysAcc000WithdrawalRuleLsAccountNumbers entity is null
     */
    public SysAcc000WithdrawalRuleLsAccountNumbers persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given SysAcc000WithdrawalRuleLsAccountNumbers entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param sysAcc000WithdrawalRuleLsAccountNumbers the SysAcc000WithdrawalRuleLsAccountNumbers entity to be updated
     * @return the updated SysAcc000WithdrawalRuleLsAccountNumbers entity
     * @throws IllegalArgumentException if the given SysAcc000WithdrawalRuleLsAccountNumbers entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static SysAcc000WithdrawalRuleLsAccountNumbers update(SysAcc000WithdrawalRuleLsAccountNumbers sysAcc000WithdrawalRuleLsAccountNumbers) {
        if (sysAcc000WithdrawalRuleLsAccountNumbers == null) {
            throw new IllegalArgumentException("sysAcc000WithdrawalRuleLsAccountNumbers can't be null");
        }
        if (sysAcc000WithdrawalRuleLsAccountNumbers.id == null) {
            throw new IllegalArgumentException("sysAcc000WithdrawalRuleLsAccountNumbers id must be specified");
        }
        if (SysAcc000WithdrawalRuleLsAccountNumbers.<SysAcc000WithdrawalRuleLsAccountNumbers>findById(sysAcc000WithdrawalRuleLsAccountNumbers.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", sysAcc000WithdrawalRuleLsAccountNumbers.id));
        }
        final SysAcc000WithdrawalRuleLsAccountNumbers merged = SysAcc000WithdrawalRuleLsAccountNumbers.getEntityManager().merge(sysAcc000WithdrawalRuleLsAccountNumbers);
        SysAcc000WithdrawalRuleLsAccountNumbers.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given SysAcc000WithdrawalRuleLsAccountNumbers entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param sysAcc000WithdrawalRuleLsAccountNumbers the SysAcc000WithdrawalRuleLsAccountNumbers entity to be persisted or updated
     * @return the persisted or updated SysAcc000WithdrawalRuleLsAccountNumbers entity
     * @throws IllegalArgumentException if the given SysAcc000WithdrawalRuleLsAccountNumbers entity is null
     */
    public static SysAcc000WithdrawalRuleLsAccountNumbers persistOrUpdate(SysAcc000WithdrawalRuleLsAccountNumbers sysAcc000WithdrawalRuleLsAccountNumbers) {
        if (sysAcc000WithdrawalRuleLsAccountNumbers == null) {
            throw new IllegalArgumentException("sysAcc000WithdrawalRuleLsAccountNumbers can't be null");
        }
        SysAcc000WithdrawalRuleLsAccountNumbers entity = null;
        if (sysAcc000WithdrawalRuleLsAccountNumbers.id != null) {
            entity = SysAcc000WithdrawalRuleLsAccountNumbers.<SysAcc000WithdrawalRuleLsAccountNumbers>findById(sysAcc000WithdrawalRuleLsAccountNumbers.id);
        }
        if (sysAcc000WithdrawalRuleLsAccountNumbers.id == null || entity == null) {
            persist(sysAcc000WithdrawalRuleLsAccountNumbers);
            return sysAcc000WithdrawalRuleLsAccountNumbers;
        } else {
            final SysAcc000WithdrawalRuleLsAccountNumbers merged = SysAcc000WithdrawalRuleLsAccountNumbers.getEntityManager().merge(sysAcc000WithdrawalRuleLsAccountNumbers);
            SysAcc000WithdrawalRuleLsAccountNumbers.getEntityManager().flush();
            return merged;
        }
    }

    public static Set<SysAcc000WithdrawalRuleLsAccountNumbers> updateDifference(
        SysAcc000WithdrawalRule owner,
        Collection<SysAcc000WithdrawalRuleLsAccountNumbers> newItems,
        Collection<SysAcc000WithdrawalRuleLsAccountNumbers> oldItems
    ) {
        if (newItems != null) {
            newItems.forEach(item -> {
                item.owner = owner;
                persistOrUpdate(item);
            });
            if (oldItems != null) {
                oldItems.stream()
                    .filter(item -> !newItems.contains(item) && item.isPersistent())
                    .forEach(PanacheEntityBase::delete);
            }
            return new HashSet<>(newItems);
        } else {
            if (oldItems != null) {
                oldItems.forEach(PanacheEntityBase::delete);
            }
            return new HashSet<>();
        }
    }
}
