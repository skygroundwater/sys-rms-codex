package com.colvir.ms.sys.rms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupMemberDto {

    public Long id;

    /** номер в группе */
    public Integer num;

    /** процент оплаты */
    public BigDecimal part;

    /** требование */
    public ReferenceDto requirement;

    @Override
    public String toString() {
        return "GroupMemberDto{" +
            "id=" + id +
            ", num=" + num +
            ", part=" + part +
            ", requirement=" + requirement +
            '}';
    }
}
