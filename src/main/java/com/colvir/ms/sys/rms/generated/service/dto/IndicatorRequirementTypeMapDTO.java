package com.colvir.ms.sys.rms.generated.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.IndicatorRequirementTypeMap} entity.
 */
@Schema(name = "IndicatorRequirementTypeMapDTO", description = "Связь вида требования с расчетной категорией")
@RegisterForReflection
public class IndicatorRequirementTypeMapDTO implements Serializable {

    public Long id;

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
     * локализация
     */
    @SchemaProperty(name = "localeId", description = "локализация", nullable = true)
    public Long localeId;

    /**
     * правило определения вида требования
     */
    @SchemaProperty(name = "requirementTypeRule", description = "правило определения вида требования", nullable = true)
    @Lob
    public String requirementTypeRule;

    /**
     * номер версии
     */
    @SchemaProperty(name = "version", description = "номер версии", nullable = true)
    public Integer version;


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndicatorRequirementTypeMapDTO)) {
            return false;
        }

        return id != null && id.equals(((IndicatorRequirementTypeMapDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "IndicatorRequirementTypeMapDTO(" +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "localeId='" + localeId + "', " +
            "requirementTypeRule='" + requirementTypeRule + "', " +
            "version='" + version + "'" +
            ")";
    }

    public IndicatorRequirementTypeMapDTO() {
    }

    public IndicatorRequirementTypeMapDTO(Long id) {
        this.id = id;
    }
}
