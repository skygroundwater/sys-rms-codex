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
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.SysAcc000WithdrawalRule} entity.
 */
@Schema(name = "SysAcc000WithdrawalRuleDTO", description = "Правило списания денег")
@RegisterForReflection
public class SysAcc000WithdrawalRuleDTO implements Serializable {

    public Long id;

    /**
     * способ выбора счетов / карт клиента
     */
    @NotNull
    @Schema(name = "accountSelectionType", description = "способ выбора счетов / карт клиента")
    public com.colvir.ms.sys.acc.generated.domain.enumeration.ClientAccountSelectionType accountSelectionType;

    /**
     * является удалённым объектом
     */
    @Schema(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * очередность
     */
    @NotNull
    @Schema(name = "priority", description = "очередность")
    public Integer priority;

    /**
     * шаблон назначения платежа
     */
    @Schema(name = "purposeTemplateId", description = "шаблон назначения платежа", nullable = true)
    public Long purposeTemplateId;

    /**
     * правило поиска счетов / карт
     */
    @Schema(name = "searchRule", description = "правило поиска счетов / карт", nullable = true)
    @Lob
    public String searchRule;

    /**
     * вариант списания денег
     */
    @Schema(name = "withdrawalTypeId", description = "вариант списания денег", nullable = true)
    public Long withdrawalTypeId;

    public Long requirementOfWithdrawalRulesId;
    public Set<SysAcc000WithdrawalRuleLsAccountNumbersDTO> accountNumbers;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SysAcc000WithdrawalRuleDTO)) {
            return false;
        }

        return id != null && id.equals(((SysAcc000WithdrawalRuleDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "SysAcc000WithdrawalRuleDTO(" +
            "accountSelectionType='" + accountSelectionType + "', " +
            "isDeleted='" + isDeleted + "', " +
            "priority='" + priority + "', " +
            "purposeTemplateId='" + purposeTemplateId + "', " +
            "searchRule='" + searchRule + "', " +
            "withdrawalTypeId='" + withdrawalTypeId + "'" +
            ")";
    }

    public SysAcc000WithdrawalRuleDTO() {
    }

    public SysAcc000WithdrawalRuleDTO(Long id) {
        this.id = id;
    }
}
