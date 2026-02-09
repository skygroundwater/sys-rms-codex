package com.colvir.ms.sys.rms.dto;

import java.util.List;

public class CheckQueueDto {

    public Long requirementId;

    public List<WithdrawalRuleDto> withdrawalRules;

    @Override
    public String toString() {
        return "CheckQueueDto{" +
            "requirementId=" + requirementId +
            ", withdrawalRules=" + withdrawalRules +
            '}';
    }
}
