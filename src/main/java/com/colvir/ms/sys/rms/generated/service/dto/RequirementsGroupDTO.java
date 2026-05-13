package com.colvir.ms.sys.rms.generated.service.dto;

import com.colvir.ms.sys.rms.generated.domain.enumeration.*;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
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
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.RequirementsGroup} entity.
 */
@Schema(name = "RequirementsGroupDTO", description = "Группа требований")
@RegisterForReflection
public class RequirementsGroupDTO implements Serializable {

    public Long id;

    /**
     * дата редактирования
     */
    @Schema(name = "editDate", description = "дата редактирования", nullable = true)
    public Instant editDate;

    /**
     * редактор
     */
    @Schema(name = "editorId", description = "редактор", nullable = true)
    public Long editorId;

    /**
     * исполнитель
     */
    @Schema(name = "executorId", description = "исполнитель", nullable = true)
    public Long executorId;

    /**
     * стратегия оплаты в группе
     */
    @Schema(name = "groupPaymentStrategy", description = "стратегия оплаты в группе", nullable = true)
    public GroupPaymentStrategy groupPaymentStrategy;

    /**
     * является удалённым объектом
     */
    @Schema(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * номер версии
     */
    @Schema(name = "version", description = "номер версии", nullable = true)
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
