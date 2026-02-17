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

import java.io.Serializable;

/**
 * Вид требования: варианты списания
 */
@Entity
@Table(name = "requirement_type_000_withdrawal_types")
@RegisterForReflection
public class RequirementType000WithdrawalTypes extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * варианты списания
     */
    @NotNull
    @Column(name = "reference_id", nullable = false)
    public Long referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_type_id")
    @JsonIgnore
    public RequirementType requirementType;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequirementType000WithdrawalTypes)) {
            return false;
        }
        return id != null && id.equals(((RequirementType000WithdrawalTypes) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "RequirementType000WithdrawalTypes(" +
            "id='" + id + "', " +
            "referenceId='" + referenceId + "'" +
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
     * @return the updated RequirementType000WithdrawalTypes entity
     * @throws IllegalArgumentException if the given RequirementType000WithdrawalTypes entity is null, or its id is null, or no existing entity with the same id is found
     */
    public RequirementType000WithdrawalTypes update() {
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
     * @return the persisted or updated RequirementType000WithdrawalTypes entity
     * @throws IllegalArgumentException if the given RequirementType000WithdrawalTypes entity is null
     */
    public RequirementType000WithdrawalTypes persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given RequirementType000WithdrawalTypes entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirementType000WithdrawalTypes the RequirementType000WithdrawalTypes entity to be updated
     * @return the updated RequirementType000WithdrawalTypes entity
     * @throws IllegalArgumentException if the given RequirementType000WithdrawalTypes entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static RequirementType000WithdrawalTypes update(RequirementType000WithdrawalTypes requirementType000WithdrawalTypes) {
        if (requirementType000WithdrawalTypes == null) {
            throw new IllegalArgumentException("requirementType000WithdrawalTypes can't be null");
        }
        if (requirementType000WithdrawalTypes.id == null) {
            throw new IllegalArgumentException("requirementType000WithdrawalTypes id must be specified");
        }
        if (RequirementType000WithdrawalTypes.<RequirementType000WithdrawalTypes>findById(requirementType000WithdrawalTypes.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", requirementType000WithdrawalTypes.id));
        }
        final RequirementType000WithdrawalTypes merged = RequirementType000WithdrawalTypes.getEntityManager().merge(requirementType000WithdrawalTypes);
        RequirementType000WithdrawalTypes.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given RequirementType000WithdrawalTypes entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirementType000WithdrawalTypes the RequirementType000WithdrawalTypes entity to be persisted or updated
     * @return the persisted or updated RequirementType000WithdrawalTypes entity
     * @throws IllegalArgumentException if the given RequirementType000WithdrawalTypes entity is null
     */
    public static RequirementType000WithdrawalTypes persistOrUpdate(RequirementType000WithdrawalTypes requirementType000WithdrawalTypes) {
        if (requirementType000WithdrawalTypes == null) {
            throw new IllegalArgumentException("requirementType000WithdrawalTypes can't be null");
        }
        RequirementType000WithdrawalTypes entity = null;
        if (requirementType000WithdrawalTypes.id != null) {
            entity = RequirementType000WithdrawalTypes.<RequirementType000WithdrawalTypes>findById(requirementType000WithdrawalTypes.id);
        }
        if (requirementType000WithdrawalTypes.id == null || entity == null) {
            persist(requirementType000WithdrawalTypes);
            return requirementType000WithdrawalTypes;
        } else {
            final RequirementType000WithdrawalTypes merged = RequirementType000WithdrawalTypes.getEntityManager().merge(requirementType000WithdrawalTypes);
            RequirementType000WithdrawalTypes.getEntityManager().flush();
            return merged;
        }
    }
}
