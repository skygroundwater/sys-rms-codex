package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;
import java.util.List;

public class RefundOfRequirementsDto {

    /** сумма возврата (в валюте требования), если не передана, считаем, что возвращается полная сумма требований (по списку) */
    public BigDecimal refundAmount;

    /** список требований (требования должны быть в одной валюте)*/
    public List<ReferenceDto> requirements;

    @Override
    public String toString() {
        return "RefundOfRequirementsDto{" +
            "refundAmount=" + refundAmount +
            ", requirements=" + requirements +
            '}';
    }
}
