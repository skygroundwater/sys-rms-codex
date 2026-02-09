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
 * Правило списания денег
 */
@Entity
@Table(name = "sys_acc_000_withdrawal_rule")
@RegisterForReflection
public class SysAcc000WithdrawalRule extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * способ выбора счетов / карт клиента
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_selection_type", nullable = false)
    public com.colvir.ms.sys.acc.generated.domain.enumeration.ClientAccountSelectionType accountSelectionType;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * очередность
     */
    @NotNull
    @Column(name = "priority", nullable = false)
    public Integer priority;

    /**
     * шаблон назначения платежа
     */
    @Column(name = "purpose_template_id")
    public Long purposeTemplateId;

    /**
     * правило поиска счетов / карт
     */
    @Lob
    @JdbcTypeCode(Types.LONGVARCHAR)
    @Column(name = "search_rule")
    public String searchRule;

    /**
     * вариант списания денег
     */
    @Column(name = "withdrawal_type_id")
    public Long withdrawalTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_of_withdrawal_rules_id")
    @JsonIgnore
    public Requirement requirementOfWithdrawalRules;

    @OneToMany(mappedBy = "owner")
    public Set<SysAcc000WithdrawalRuleLsAccountNumbers> accountNumbers = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysAcc000WithdrawalRule)) {
            return false;
        }
        return id != null && id.equals(((SysAcc000WithdrawalRule) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "SysAcc000WithdrawalRule(" +
            "id='" + id + "', " +
            "accountSelectionType='" + accountSelectionType + "', " +
            "isDeleted='" + isDeleted + "', " +
            "priority='" + priority + "', " +
            "purposeTemplateId='" + purposeTemplateId + "', " +
            "searchRule='" + searchRule + "', " +
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
        accountSelectionType = accountSelectionType == null ? com.colvir.ms.sys.acc.generated.domain.enumeration.ClientAccountSelectionType.SPECIFIED : accountSelectionType;
        isDeleted = isDeleted == null ? false : isDeleted;
        priority = priority == null ? 1 : priority;
    }

    /**
     * Updates the current state of the entity in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the current entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @return the updated SysAcc000WithdrawalRule entity
     * @throws IllegalArgumentException if the given SysAcc000WithdrawalRule entity is null, or its id is null, or no existing entity with the same id is found
     */
    public SysAcc000WithdrawalRule update() {
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
     * @return the persisted or updated SysAcc000WithdrawalRule entity
     * @throws IllegalArgumentException if the given SysAcc000WithdrawalRule entity is null
     */
    public SysAcc000WithdrawalRule persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given SysAcc000WithdrawalRule entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param sysAcc000WithdrawalRule the SysAcc000WithdrawalRule entity to be updated
     * @return the updated SysAcc000WithdrawalRule entity
     * @throws IllegalArgumentException if the given SysAcc000WithdrawalRule entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static SysAcc000WithdrawalRule update(SysAcc000WithdrawalRule sysAcc000WithdrawalRule) {
        if (sysAcc000WithdrawalRule == null) {
            throw new IllegalArgumentException("sysAcc000WithdrawalRule can't be null");
        }
        if (sysAcc000WithdrawalRule.id == null) {
            throw new IllegalArgumentException("sysAcc000WithdrawalRule id must be specified");
        }
        if (SysAcc000WithdrawalRule.<SysAcc000WithdrawalRule>findById(sysAcc000WithdrawalRule.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", sysAcc000WithdrawalRule.id));
        }
        final SysAcc000WithdrawalRule merged = SysAcc000WithdrawalRule.getEntityManager().merge(sysAcc000WithdrawalRule);
        SysAcc000WithdrawalRule.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given SysAcc000WithdrawalRule entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param sysAcc000WithdrawalRule the SysAcc000WithdrawalRule entity to be persisted or updated
     * @return the persisted or updated SysAcc000WithdrawalRule entity
     * @throws IllegalArgumentException if the given SysAcc000WithdrawalRule entity is null
     */
    public static SysAcc000WithdrawalRule persistOrUpdate(SysAcc000WithdrawalRule sysAcc000WithdrawalRule) {
        if (sysAcc000WithdrawalRule == null) {
            throw new IllegalArgumentException("sysAcc000WithdrawalRule can't be null");
        }
        SysAcc000WithdrawalRule entity = null;
        if (sysAcc000WithdrawalRule.id != null) {
            entity = SysAcc000WithdrawalRule.<SysAcc000WithdrawalRule>findById(sysAcc000WithdrawalRule.id);
        }
        if (sysAcc000WithdrawalRule.id == null || entity == null) {
            persist(sysAcc000WithdrawalRule);
            SysAcc000WithdrawalRuleLsAccountNumbers.updateDifference(sysAcc000WithdrawalRule, sysAcc000WithdrawalRule.accountNumbers, null);
            return sysAcc000WithdrawalRule;
        } else {
            sysAcc000WithdrawalRule.accountNumbers =
                SysAcc000WithdrawalRuleLsAccountNumbers.updateDifference(sysAcc000WithdrawalRule, sysAcc000WithdrawalRule.accountNumbers, entity.accountNumbers);
            final SysAcc000WithdrawalRule merged = SysAcc000WithdrawalRule.getEntityManager().merge(sysAcc000WithdrawalRule);
            SysAcc000WithdrawalRule.getEntityManager().flush();
            return merged;
        }
    }
}
