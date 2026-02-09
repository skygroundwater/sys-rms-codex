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
 * Участник группы
 */
@Entity
@Table(name = "group_member")
@RegisterForReflection
public class GroupMember extends PanacheEntityBase implements Serializable {

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
     * номер в группе
     */
    @Column(name = "num")
    public Integer num;

    /**
     * процент оплаты
     */
    @Column(name = "part")
    public BigDecimal part;

    /**
     * номер версии
     */
    @Version
    @Column(name = "version")
    public Integer version = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_id")
    @JsonIgnore
    public Requirement requirement;

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    public Set<RequirementsGroup> requirementsGroupOfMembers = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupMember)) {
            return false;
        }
        return id != null && id.equals(((GroupMember) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "GroupMember(" +
            "id='" + id + "', " +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "num='" + num + "', " +
            "part='" + part + "', " +
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
     * @return the updated GroupMember entity
     * @throws IllegalArgumentException if the given GroupMember entity is null, or its id is null, or no existing entity with the same id is found
     */
    public GroupMember update() {
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
     * @return the persisted or updated GroupMember entity
     * @throws IllegalArgumentException if the given GroupMember entity is null
     */
    public GroupMember persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given GroupMember entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param groupMember the GroupMember entity to be updated
     * @return the updated GroupMember entity
     * @throws IllegalArgumentException if the given GroupMember entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static GroupMember update(GroupMember groupMember) {
        if (groupMember == null) {
            throw new IllegalArgumentException("groupMember can't be null");
        }
        if (groupMember.id == null) {
            throw new IllegalArgumentException("groupMember id must be specified");
        }
        if (GroupMember.<GroupMember>findById(groupMember.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", groupMember.id));
        }
        final GroupMember merged = GroupMember.getEntityManager().merge(groupMember);
        GroupMember.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given GroupMember entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param groupMember the GroupMember entity to be persisted or updated
     * @return the persisted or updated GroupMember entity
     * @throws IllegalArgumentException if the given GroupMember entity is null
     */
    public static GroupMember persistOrUpdate(GroupMember groupMember) {
        if (groupMember == null) {
            throw new IllegalArgumentException("groupMember can't be null");
        }
        GroupMember entity = null;
        if (groupMember.id != null) {
            entity = GroupMember.<GroupMember>findById(groupMember.id);
        }
        if (groupMember.id == null || entity == null) {
            persist(groupMember);
            return groupMember;
        } else {
            final GroupMember merged = GroupMember.getEntityManager().merge(groupMember);
            GroupMember.getEntityManager().flush();
            return merged;
        }
    }
}
