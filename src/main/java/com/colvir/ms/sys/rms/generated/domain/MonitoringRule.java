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
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.JdbcTypeCode;

import java.io.Serializable;
import java.sql.Types;
import java.time.Instant;

/**
 * Правило мониторинга
 */
@Entity
@Table(name = "monitoring_rule")
@RegisterForReflection
public class MonitoringRule extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * атрибут даты
     */
    @Size(max = 511)
    @Column(name = "attr_date")
    public String attrDate;

    /**
     * атрибут подразделения
     */
    @Size(max = 511)
    @Column(name = "attr_dep")
    public String attrDep;

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
     * правило отбора платежей
     */
    @Lob
    @JdbcTypeCode(Types.LONGVARCHAR)
    @Column(name = "rule_find")
    public String ruleFind;

    /**
     * правило соответствия
     */
    @Lob
    @JdbcTypeCode(Types.LONGVARCHAR)
    @Column(name = "rule_match")
    public String ruleMatch;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitoringRule)) {
            return false;
        }
        return id != null && id.equals(((MonitoringRule) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "MonitoringRule(" +
            "id='" + id + "', " +
            "attrDate='" + attrDate + "', " +
            "attrDep='" + attrDep + "', " +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "ruleFind='" + ruleFind + "', " +
            "ruleMatch='" + ruleMatch + "', " +
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
     * @return the updated MonitoringRule entity
     * @throws IllegalArgumentException if the given MonitoringRule entity is null, or its id is null, or no existing entity with the same id is found
     */
    public MonitoringRule update() {
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
     * @return the persisted or updated MonitoringRule entity
     * @throws IllegalArgumentException if the given MonitoringRule entity is null
     */
    public MonitoringRule persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given MonitoringRule entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param monitoringRule the MonitoringRule entity to be updated
     * @return the updated MonitoringRule entity
     * @throws IllegalArgumentException if the given MonitoringRule entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static MonitoringRule update(MonitoringRule monitoringRule) {
        if (monitoringRule == null) {
            throw new IllegalArgumentException("monitoringRule can't be null");
        }
        if (monitoringRule.id == null) {
            throw new IllegalArgumentException("monitoringRule id must be specified");
        }
        if (MonitoringRule.<MonitoringRule>findById(monitoringRule.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", monitoringRule.id));
        }
        final MonitoringRule merged = MonitoringRule.getEntityManager().merge(monitoringRule);
        MonitoringRule.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given MonitoringRule entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param monitoringRule the MonitoringRule entity to be persisted or updated
     * @return the persisted or updated MonitoringRule entity
     * @throws IllegalArgumentException if the given MonitoringRule entity is null
     */
    public static MonitoringRule persistOrUpdate(MonitoringRule monitoringRule) {
        if (monitoringRule == null) {
            throw new IllegalArgumentException("monitoringRule can't be null");
        }
        MonitoringRule entity = null;
        if (monitoringRule.id != null) {
            entity = MonitoringRule.<MonitoringRule>findById(monitoringRule.id);
        }
        if (monitoringRule.id == null || entity == null) {
            persist(monitoringRule);
            return monitoringRule;
        } else {
            final MonitoringRule merged = MonitoringRule.getEntityManager().merge(monitoringRule);
            MonitoringRule.getEntityManager().flush();
            return merged;
        }
    }
}
