package com.colvir.ms.sys.rms.generated.service.dto;

import com.colvir.ms.sys.rms.generated.domain.enumeration.GroupPaymentStrategy;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.RequirementsGroup} entity.
 */
@Schema(name = "RequirementsGroupDTO", description = "Группа требований")
@RegisterForReflection
public class RequirementsGroupDTO implements Serializable {

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
     * стратегия оплаты в группе
     */
    @SchemaProperty(name = "groupPaymentStrategy", description = "стратегия оплаты в группе", nullable = true)
    public GroupPaymentStrategy groupPaymentStrategy;

    /**
     * является удалённым объектом
     */
    @SchemaProperty(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * номер версии
     */
    @SchemaProperty(name = "version", description = "номер версии", nullable = true)
    public Integer version;

    public Set<GroupMemberDTO> members;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequirementsGroupDTO)) {
            return false;
        }

        return id != null && id.equals(((RequirementsGroupDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "RequirementsGroupDTO(" +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "groupPaymentStrategy='" + groupPaymentStrategy + "', " +
            "isDeleted='" + isDeleted + "', " +
            "version='" + version + "'" +
            ")";
    }

    public RequirementsGroupDTO() {
    members = new HashSet<>();
    }

    public RequirementsGroupDTO(Long id) {
        this.id = id;
    }
}
