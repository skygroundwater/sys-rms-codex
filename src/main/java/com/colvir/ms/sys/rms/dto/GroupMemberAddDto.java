package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;

public class GroupMemberAddDto {

    /** группа требований */
    public ReferenceDto requirementGroup;

    /** требование */
    public ReferenceDto requirement;

    /** номер в группе */
    public Integer num;

    /** процент оплаты */
    public BigDecimal part;

    @Override
    public String toString() {
        return "GroupMemberAddDto{" +
            "requirementGroup=" + requirementGroup +
            ", requirement=" + requirement +
            ", num=" + num +
            ", part=" + part +
            '}';
    }
}
