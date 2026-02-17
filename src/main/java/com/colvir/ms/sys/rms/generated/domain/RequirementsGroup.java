package com.colvir.ms.sys.rms.generated.domain;

import com.colvir.ms.common.generator.id.runtime.service.IdGeneratorService;
import com.colvir.ms.sys.rms.generated.domain.enumeration.GroupPaymentStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Группа требований
 */
@Entity
@Table(name = "requirements_group")
@RegisterForReflection
public class RequirementsGroup extends PanacheEntityBase implements Serializable {

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
     * стратегия оплаты в группе
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "group_payment_strategy")
    public GroupPaymentStrategy groupPaymentStrategy;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * номер версии
     */
    @Version
    @Column(name = "version")
    public Integer version = 0;

    @ManyToMany
    @JoinTable(
        name = "rel_requirements_group__members",
        joinColumns = @JoinColumn(name = "requirements_group_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "members_id", referencedColumnName = "id")
    )
    @JsonIgnore
    public Set<GroupMember> members = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequirementsGroup)) {
            return false;
        }
        return id != null && id.equals(((RequirementsGroup) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "RequirementsGroup(" +
            "id='" + id + "', " +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "groupPaymentStrategy='" + groupPaymentStrategy + "', " +
            "isDeleted='" + isDeleted + "', " +
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
     * @return the updated RequirementsGroup entity
     * @throws IllegalArgumentException if the given RequirementsGroup entity is null, or its id is null, or no existing entity with the same id is found
     */
    public RequirementsGroup update() {
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
     * @return the persisted or updated RequirementsGroup entity
     * @throws IllegalArgumentException if the given RequirementsGroup entity is null
     */
    public RequirementsGroup persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given RequirementsGroup entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirementsGroup the RequirementsGroup entity to be updated
     * @return the updated RequirementsGroup entity
     * @throws IllegalArgumentException if the given RequirementsGroup entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static RequirementsGroup update(RequirementsGroup requirementsGroup) {
        if (requirementsGroup == null) {
            throw new IllegalArgumentException("requirementsGroup can't be null");
        }
        if (requirementsGroup.id == null) {
            throw new IllegalArgumentException("requirementsGroup id must be specified");
        }
        if (RequirementsGroup.<RequirementsGroup>findById(requirementsGroup.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", requirementsGroup.id));
        }
        final RequirementsGroup merged = RequirementsGroup.getEntityManager().merge(requirementsGroup);
        RequirementsGroup.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given RequirementsGroup entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirementsGroup the RequirementsGroup entity to be persisted or updated
     * @return the persisted or updated RequirementsGroup entity
     * @throws IllegalArgumentException if the given RequirementsGroup entity is null
     */
    public static RequirementsGroup persistOrUpdate(RequirementsGroup requirementsGroup) {
        if (requirementsGroup == null) {
            throw new IllegalArgumentException("requirementsGroup can't be null");
        }
        RequirementsGroup entity = null;
        if (requirementsGroup.id != null) {
            entity = RequirementsGroup.<RequirementsGroup>findById(requirementsGroup.id);
        }
        if (requirementsGroup.id == null || entity == null) {
            persist(requirementsGroup);
            return requirementsGroup;
        } else {
            final RequirementsGroup merged = RequirementsGroup.getEntityManager().merge(requirementsGroup);
            RequirementsGroup.getEntityManager().flush();
            return merged;
        }
    }

    public static PanacheQuery<RequirementsGroup> findAllWithEagerRelationships() {
        return find("select distinct requirementsGroup from RequirementsGroup requirementsGroup left join fetch requirementsGroup.members");
    }

    public static Optional<RequirementsGroup> findOneWithEagerRelationships(Long id) {
        return find("select requirementsGroup from RequirementsGroup requirementsGroup left join fetch requirementsGroup.members where requirementsGroup.id =?1", id)
            .firstResultOptional();
    }
}
