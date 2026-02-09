package com.colvir.ms.sys.rms.dto;

import java.util.List;

public class QueueCheckDto {

    /** требование */
    public ReferenceDto requirement;

    /** правила списания */
    public List<WithdrawalRuleDto> withdrawalRules;

    @Override
    public String toString() {
        return "QueueCheckDto{" +
            "requirement=" + requirement +
            ", withdrawalRules=" + withdrawalRules +
            '}';
    }
}
