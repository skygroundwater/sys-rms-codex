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
 * Объект-значение
 */
@Entity
@Table(name = "requirement_000_related_taxes")
@RegisterForReflection
public class Requirement000RelatedTaxes extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * сумма
     */
    @Column(name = "amount")
    public BigDecimal amount;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * вид налога
     */
    @Column(name = "type_of_tax_id")
    public Long typeOfTaxId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_of_related_taxes_id")
    @JsonIgnore
    public Requirement requirementOfRelatedTaxes;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Requirement000RelatedTaxes)) {
            return false;
        }
        return id != null && id.equals(((Requirement000RelatedTaxes) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "Requirement000RelatedTaxes(" +
            "id='" + id + "', " +
            "amount='" + amount + "', " +
            "isDeleted='" + isDeleted + "', " +
            "typeOfTaxId='" + typeOfTaxId + "'" +
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
     * @return the updated Requirement000RelatedTaxes entity
     * @throws IllegalArgumentException if the given Requirement000RelatedTaxes entity is null, or its id is null, or no existing entity with the same id is found
     */
    public Requirement000RelatedTaxes update() {
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
     * @return the persisted or updated Requirement000RelatedTaxes entity
     * @throws IllegalArgumentException if the given Requirement000RelatedTaxes entity is null
     */
    public Requirement000RelatedTaxes persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given Requirement000RelatedTaxes entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirement000RelatedTaxes the Requirement000RelatedTaxes entity to be updated
     * @return the updated Requirement000RelatedTaxes entity
     * @throws IllegalArgumentException if the given Requirement000RelatedTaxes entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static Requirement000RelatedTaxes update(Requirement000RelatedTaxes requirement000RelatedTaxes) {
        if (requirement000RelatedTaxes == null) {
            throw new IllegalArgumentException("requirement000RelatedTaxes can't be null");
        }
        if (requirement000RelatedTaxes.id == null) {
            throw new IllegalArgumentException("requirement000RelatedTaxes id must be specified");
        }
        if (Requirement000RelatedTaxes.<Requirement000RelatedTaxes>findById(requirement000RelatedTaxes.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", requirement000RelatedTaxes.id));
        }
        final Requirement000RelatedTaxes merged = Requirement000RelatedTaxes.getEntityManager().merge(requirement000RelatedTaxes);
        Requirement000RelatedTaxes.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given Requirement000RelatedTaxes entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirement000RelatedTaxes the Requirement000RelatedTaxes entity to be persisted or updated
     * @return the persisted or updated Requirement000RelatedTaxes entity
     * @throws IllegalArgumentException if the given Requirement000RelatedTaxes entity is null
     */
    public static Requirement000RelatedTaxes persistOrUpdate(Requirement000RelatedTaxes requirement000RelatedTaxes) {
        if (requirement000RelatedTaxes == null) {
            throw new IllegalArgumentException("requirement000RelatedTaxes can't be null");
        }
        Requirement000RelatedTaxes entity = null;
        if (requirement000RelatedTaxes.id != null) {
            entity = Requirement000RelatedTaxes.<Requirement000RelatedTaxes>findById(requirement000RelatedTaxes.id);
        }
        if (requirement000RelatedTaxes.id == null || entity == null) {
            persist(requirement000RelatedTaxes);
            return requirement000RelatedTaxes;
        } else {
            final Requirement000RelatedTaxes merged = Requirement000RelatedTaxes.getEntityManager().merge(requirement000RelatedTaxes);
            Requirement000RelatedTaxes.getEntityManager().flush();
            return merged;
        }
    }
}
