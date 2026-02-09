package com.colvir.ms.sys.rms.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

public class RefundOfRequirementsRunnerDto {

    /** сумма возврата (в валюте требования), если не передана, считаем, что возвращается полная сумма требований (по списку) */
    public BigDecimal amount;

    /** требования (список или одиночное значение, требования в списке должны быть в одной валюте) */
    public JsonNode requirements;

    @Override
    public String toString() {
        return "RefundOfRequirementsRunnerDto{" +
            "amount=" + amount +
            ", requirements=" + requirements +
            '}';
    }
}
