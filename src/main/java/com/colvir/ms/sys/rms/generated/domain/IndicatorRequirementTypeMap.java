package com.colvir.ms.sys.rms.generated.domain;

import com.colvir.ms.common.generator.id.runtime.service.IdGeneratorService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.sql.Types;
import java.time.Instant;

/**
 * Связь вида требования с расчетной категорией
 */
@Entity
@Table(name = "indicator_requirement_type_map")
@RegisterForReflection
public class IndicatorRequirementTypeMap extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

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
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * локализация
     */
    @Column(name = "locale_id")
    public Long localeId;

    /**
     * правило определения вида требования
     */
    @Lob
    @JdbcTypeCode(Types.LONGVARCHAR)
    @Column(name = "requirement_type_rule")
    public String requirementTypeRule;

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
        if (!(o instanceof IndicatorRequirementTypeMap)) {
            return false;
        }
        return id != null && id.equals(((IndicatorRequirementTypeMap) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "IndicatorRequirementTypeMap(" +
            "id='" + id + "', " +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "localeId='" + localeId + "', " +
            "requirementTypeRule='" + requirementTypeRule + "', " +
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
        isDeleted = isDeleted == null ? false : isDeleted;
    }

    /**
     * Updates the current state of the entity in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the current entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @return the updated IndicatorRequirementTypeMap entity
     * @throws IllegalArgumentException if the given IndicatorRequirementTypeMap entity is null, or its id is null, or no existing entity with the same id is found
     */
    public IndicatorRequirementTypeMap update() {
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
     * @return the persisted or updated IndicatorRequirementTypeMap entity
     * @throws IllegalArgumentException if the given IndicatorRequirementTypeMap entity is null
     */
    public IndicatorRequirementTypeMap persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given IndicatorRequirementTypeMap entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param indicatorRequirementTypeMap the IndicatorRequirementTypeMap entity to be updated
     * @return the updated IndicatorRequirementTypeMap entity
     * @throws IllegalArgumentException if the given IndicatorRequirementTypeMap entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static IndicatorRequirementTypeMap update(IndicatorRequirementTypeMap indicatorRequirementTypeMap) {
        if (indicatorRequirementTypeMap == null) {
            throw new IllegalArgumentException("indicatorRequirementTypeMap can't be null");
        }
        if (indicatorRequirementTypeMap.id == null) {
            throw new IllegalArgumentException("indicatorRequirementTypeMap id must be specified");
        }
        if (IndicatorRequirementTypeMap.<IndicatorRequirementTypeMap>findById(indicatorRequirementTypeMap.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", indicatorRequirementTypeMap.id));
        }
        final IndicatorRequirementTypeMap merged = IndicatorRequirementTypeMap.getEntityManager().merge(indicatorRequirementTypeMap);
        IndicatorRequirementTypeMap.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given IndicatorRequirementTypeMap entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param indicatorRequirementTypeMap the IndicatorRequirementTypeMap entity to be persisted or updated
     * @return the persisted or updated IndicatorRequirementTypeMap entity
     * @throws IllegalArgumentException if the given IndicatorRequirementTypeMap entity is null
     */
    public static IndicatorRequirementTypeMap persistOrUpdate(IndicatorRequirementTypeMap indicatorRequirementTypeMap) {
        if (indicatorRequirementTypeMap == null) {
            throw new IllegalArgumentException("indicatorRequirementTypeMap can't be null");
        }
        IndicatorRequirementTypeMap entity = null;
        if (indicatorRequirementTypeMap.id != null) {
            entity = IndicatorRequirementTypeMap.<IndicatorRequirementTypeMap>findById(indicatorRequirementTypeMap.id);
        }
        if (indicatorRequirementTypeMap.id == null || entity == null) {
            persist(indicatorRequirementTypeMap);
            return indicatorRequirementTypeMap;
        } else {
            final IndicatorRequirementTypeMap merged = IndicatorRequirementTypeMap.getEntityManager().merge(indicatorRequirementTypeMap);
            IndicatorRequirementTypeMap.getEntityManager().flush();
            return merged;
        }
    }
}
