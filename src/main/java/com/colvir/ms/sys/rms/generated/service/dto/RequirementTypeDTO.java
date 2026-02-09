package com.colvir.ms.sys.rms.generated.service.dto;

import com.colvir.ms.sys.rms.generated.domain.enumeration.*;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.RequirementType} entity.
 */
@Schema(name = "RequirementTypeDTO", description = "Вид требования")
@RegisterForReflection
public class RequirementTypeDTO implements Serializable {

    public Long id;

    /**
     * обозначение
     */
    @NotNull
    @Size(min = 1, max = 30)
    @SchemaProperty(name = "code", description = "обозначение")
    public String code;

    /**
     * описание
     */
    @SchemaProperty(name = "description", description = "описание", nullable = true)
    public String description;

    /**
     * дата редактирования
     */
    @SchemaProperty(name = "editDate", description = "дата редактирования", nullable = true)
    public Instant editDate;

    /**
     * редактор
     */
    @SchemaProperty(name = "editorId", description = "редактор", nullable = true)
    public Long editorId;

    /**
     * исполнитель
     */
    @SchemaProperty(name = "executorId", description = "исполнитель", nullable = true)
    public Long executorId;

    /**
     * требование банка
     */
    @NotNull
    @SchemaProperty(name = "isBankIssued", description = "требование банка")
    public Boolean isBankIssued;

    /**
     * является удалённым объектом
     */
    @SchemaProperty(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * разрешена оплата по частям
     */
    @NotNull
    @SchemaProperty(name = "isPartialPayable", description = "разрешена оплата по частям")
    public Boolean isPartialPayable;

    /**
     * наименование
     */
    @NotNull
    @Size(min = 0, max = 255)
    @SchemaProperty(name = "name", description = "наименование")
    public String name;

    /**
     * приоритет
     */
    @SchemaProperty(name = "priority", description = "приоритет", nullable = true)
    public Integer priority;

    /**
     * разрешено использование заемных средств
     */
    @NotNull
    @SchemaProperty(name = "useLoanFunds", description = "разрешено использование заемных средств")
    public Boolean useLoanFunds;

    /**
     * номер версии
     */
    @SchemaProperty(name = "version", description = "номер версии", nullable = true)
    public Integer version;


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequirementTypeDTO)) {
            return false;
        }

        return id != null && id.equals(((RequirementTypeDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "RequirementTypeDTO(" +
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

    public RequirementTypeDTO() {
    }

    public RequirementTypeDTO(Long id) {
        this.id = id;
    }
}
