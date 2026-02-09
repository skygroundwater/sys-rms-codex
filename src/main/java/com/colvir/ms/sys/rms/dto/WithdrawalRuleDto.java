package com.colvir.ms.sys.rms.dto;

import com.colvir.ms.sys.rms.manual.domain.ClientAccountSelectionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawalRuleDto {

    public Long id;

    /** способ выбора счетов / карт клиента */
    public ClientAccountSelectionType accountSelectionType;

    /** очередность */
    public Integer priority;

    /** правило поиска счетов / карт */
    public String searchRule;

    /** вариант списания денег */
    public ReferenceDto withdrawalType;

    /** номера счетов */
    public List<String> accountNumbers;

    @Override
    public String toString() {
        return "WithdrawalRuleDto{" +
            "id=" + id +
            ", accountSelectionType=" + accountSelectionType +
            ", priority=" + priority +
            ", searchRule='" + searchRule + '\'' +
            ", withdrawalType=" + withdrawalType +
            ", accountNumbers=" + accountNumbers +
            '}';
    }

}
