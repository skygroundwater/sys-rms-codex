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
 * Вид требования
 */
@Entity
@Table(name = "requirement_type")
@RegisterForReflection
public class RequirementType extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * обозначение
     */
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "code", length = 30, nullable = false, unique = true)
    public String code;

    /**
     * описание
     */
    @Size(max = 511)
    @Column(name = "description")
    public String description;

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
     * требование банка
     */
    @NotNull
    @Column(name = "is_bank_issued", nullable = false)
    public Boolean isBankIssued;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * разрешена оплата по частям
     */
    @NotNull
    @Column(name = "is_partial_payable", nullable = false)
    public Boolean isPartialPayable;

    /**
     * наименование
     */
    @NotNull
    @Size(min = 0, max = 255)
    @Column(name = "name", length = 255, nullable = false, unique = true)
    public String name;

    /**
     * приоритет
     */
    @Column(name = "priority")
    public Integer priority;

    /**
     * разрешено использование заемных средств
     */
    @NotNull
    @Column(name = "use_loan_funds", nullable = false)
    public Boolean useLoanFunds;

    /**
     * номер версии
     */
    @Version
    @Column(name = "version")
    public Integer version = 0;

    @OneToMany(mappedBy = "requirementType")
    public Set<RequirementType000WithdrawalTypes> withdrawalTypes = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequirementType)) {
            return false;
        }
        return id != null && id.equals(((RequirementType) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "RequirementType(" +
            "id='" + id + "', " +
            "code='" + code + "', " +
            "description='" + description + "', " +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "isBankIssued='" + isBankIssued + "', " +
            "isDeleted='" + isDeleted + "', " +
            "isPartialPayable='" + isPartialPayable + "', " +
            "name='" + name + "', " +
            "priority='" + priority + "', " +
            "useLoanFunds='" + useLoanFunds + "', " +
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
        isBankIssued = isBankIssued == null ? true : isBankIssued;
        isDeleted = isDeleted == null ? false : isDeleted;
        isPartialPayable = isPartialPayable == null ? false : isPartialPayable;
        useLoanFunds = useLoanFunds == null ? false : useLoanFunds;
    }

    /**
     * Updates the current state of the entity in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the current entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @return the updated RequirementType entity
     * @throws IllegalArgumentException if the given RequirementType entity is null, or its id is null, or no existing entity with the same id is found
     */
    public RequirementType update() {
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
     * @return the persisted or updated RequirementType entity
     * @throws IllegalArgumentException if the given RequirementType entity is null
     */
    public RequirementType persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given RequirementType entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirementType the RequirementType entity to be updated
     * @return the updated RequirementType entity
     * @throws IllegalArgumentException if the given RequirementType entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static RequirementType update(RequirementType requirementType) {
        if (requirementType == null) {
            throw new IllegalArgumentException("requirementType can't be null");
        }
        if (requirementType.id == null) {
            throw new IllegalArgumentException("requirementType id must be specified");
        }
        if (RequirementType.<RequirementType>findById(requirementType.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", requirementType.id));
        }
        final RequirementType merged = RequirementType.getEntityManager().merge(requirementType);
        RequirementType.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given RequirementType entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirementType the RequirementType entity to be persisted or updated
     * @return the persisted or updated RequirementType entity
     * @throws IllegalArgumentException if the given RequirementType entity is null
     */
    public static RequirementType persistOrUpdate(RequirementType requirementType) {
        if (requirementType == null) {
            throw new IllegalArgumentException("requirementType can't be null");
        }
        RequirementType entity = null;
        if (requirementType.id != null) {
            entity = RequirementType.<RequirementType>findById(requirementType.id);
        }
        if (requirementType.id == null || entity == null) {
            persist(requirementType);
            return requirementType;
        } else {
            final RequirementType merged = RequirementType.getEntityManager().merge(requirementType);
            RequirementType.getEntityManager().flush();
            return merged;
        }
    }
}
