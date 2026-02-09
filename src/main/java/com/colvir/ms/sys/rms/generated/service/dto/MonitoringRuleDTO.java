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
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.MonitoringRule} entity.
 */
@Schema(name = "MonitoringRuleDTO", description = "Правило мониторинга")
@RegisterForReflection
public class MonitoringRuleDTO implements Serializable {

    public Long id;

    /**
     * атрибут даты
     */
    @SchemaProperty(name = "attrDate", description = "атрибут даты", nullable = true)
    public String attrDate;

    /**
     * атрибут подразделения
     */
    @SchemaProperty(name = "attrDep", description = "атрибут подразделения", nullable = true)
    public String attrDep;

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
     * является удалённым объектом
     */
    @SchemaProperty(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * правило отбора платежей
     */
    @SchemaProperty(name = "ruleFind", description = "правило отбора платежей", nullable = true)
    @Lob
    public String ruleFind;

    /**
     * правило соответствия
     */
    @SchemaProperty(name = "ruleMatch", description = "правило соответствия", nullable = true)
    @Lob
    public String ruleMatch;

    /**
     * номер версии
     */
    @SchemaProperty(name = "version", description = "номер версии", nullable = true)
    public Integer version;

    /**
     * вариант списания денег
     */
    @SchemaProperty(name = "withdrawalTypeId", description = "вариант списания денег", nullable = true)
    public Long withdrawalTypeId;


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitoringRuleDTO)) {
            return false;
        }

        return id != null && id.equals(((MonitoringRuleDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "MonitoringRuleDTO(" +
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

    public MonitoringRuleDTO() {
    }

    public MonitoringRuleDTO(Long id) {
        this.id = id;
    }
}
