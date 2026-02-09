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
 * Холд по требованию
 */
@Entity
@Table(name = "requirement_hold")
@RegisterForReflection
public class RequirementHold extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * номер счета
     */
    @Size(max = 511)
    @Column(name = "account_number")
    public String accountNumber;

    /**
     * сумма
     */
    @NotNull
    @Column(name = "amount", nullable = false)
    public BigDecimal amount;

    /**
     * валюта
     */
    @Column(name = "currency_id")
    public Long currencyId;

    /**
     * холд по online счету
     */
    @Column(name = "hold_id")
    public Long holdId;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * рефернс холда во внешней системе
     */
    @Size(max = 511)
    @Column(name = "reference")
    public String reference;

    /**
     * вариант списания денег
     */
    @Column(name = "withdrawal_type_id")
    public Long withdrawalTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_of_assigned_holds_id")
    @JsonIgnore
    public Requirement requirementOfAssignedHolds;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequirementHold)) {
            return false;
        }
        return id != null && id.equals(((RequirementHold) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "RequirementHold(" +
            "id='" + id + "', " +
            "accountNumber='" + accountNumber + "', " +
            "amount='" + amount + "', " +
            "currencyId='" + currencyId + "', " +
            "holdId='" + holdId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "reference='" + reference + "', " +
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
     * @return the updated RequirementHold entity
     * @throws IllegalArgumentException if the given RequirementHold entity is null, or its id is null, or no existing entity with the same id is found
     */
    public RequirementHold update() {
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
     * @return the persisted or updated RequirementHold entity
     * @throws IllegalArgumentException if the given RequirementHold entity is null
     */
    public RequirementHold persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given RequirementHold entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirementHold the RequirementHold entity to be updated
     * @return the updated RequirementHold entity
     * @throws IllegalArgumentException if the given RequirementHold entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static RequirementHold update(RequirementHold requirementHold) {
        if (requirementHold == null) {
            throw new IllegalArgumentException("requirementHold can't be null");
        }
        if (requirementHold.id == null) {
            throw new IllegalArgumentException("requirementHold id must be specified");
        }
        if (RequirementHold.<RequirementHold>findById(requirementHold.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", requirementHold.id));
        }
        final RequirementHold merged = RequirementHold.getEntityManager().merge(requirementHold);
        RequirementHold.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given RequirementHold entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirementHold the RequirementHold entity to be persisted or updated
     * @return the persisted or updated RequirementHold entity
     * @throws IllegalArgumentException if the given RequirementHold entity is null
     */
    public static RequirementHold persistOrUpdate(RequirementHold requirementHold) {
        if (requirementHold == null) {
            throw new IllegalArgumentException("requirementHold can't be null");
        }
        RequirementHold entity = null;
        if (requirementHold.id != null) {
            entity = RequirementHold.<RequirementHold>findById(requirementHold.id);
        }
        if (requirementHold.id == null || entity == null) {
            persist(requirementHold);
            return requirementHold;
        } else {
            final RequirementHold merged = RequirementHold.getEntityManager().merge(requirementHold);
            RequirementHold.getEntityManager().flush();
            return merged;
        }
    }
}
