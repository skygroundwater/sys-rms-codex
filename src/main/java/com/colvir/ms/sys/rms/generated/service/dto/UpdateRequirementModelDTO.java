package com.colvir.ms.sys.rms.generated.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.UpdateRequirementModel} entity.
 */
@Schema(name = "UpdateRequirementModelDTO", description = "Модель обновления требований")
@RegisterForReflection
public class UpdateRequirementModelDTO implements Serializable {

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
     * список параметров требований
     */
    @SchemaProperty(name = "requirementList", description = "список параметров требований", nullable = true)
    public String requirementList;

    /**
     * номер версии
     */
    @SchemaProperty(name = "version", description = "номер версии", nullable = true)
    public Integer version;


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UpdateRequirementModelDTO)) {
            return false;
        }

        return id != null && id.equals(((UpdateRequirementModelDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "UpdateRequirementModelDTO(" +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "requirementList='" + requirementList + "', " +
            "version='" + version + "'" +
            ")";
    }

    public UpdateRequirementModelDTO() {
    }

    public UpdateRequirementModelDTO(Long id) {
        this.id = id;
    }
}
