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
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.GroupMember} entity.
 */
@Schema(name = "GroupMemberDTO", description = "Участник группы")
@RegisterForReflection
public class GroupMemberDTO implements Serializable {

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
     * номер в группе
     */
    @SchemaProperty(name = "num", description = "номер в группе", nullable = true)
    public Integer num;

    /**
     * процент оплаты
     */
    @SchemaProperty(name = "part", description = "процент оплаты", nullable = true)
    public BigDecimal part;

    /**
     * номер версии
     */
    @SchemaProperty(name = "version", description = "номер версии", nullable = true)
    public Integer version;

    public Long requirementId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupMemberDTO)) {
            return false;
        }

        return id != null && id.equals(((GroupMemberDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "GroupMemberDTO(" +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "isDeleted='" + isDeleted + "', " +
            "num='" + num + "', " +
            "part='" + part + "', " +
            "version='" + version + "'" +
            ")";
    }

    public GroupMemberDTO() {
    }

    public GroupMemberDTO(Long id) {
        this.id = id;
    }
}
