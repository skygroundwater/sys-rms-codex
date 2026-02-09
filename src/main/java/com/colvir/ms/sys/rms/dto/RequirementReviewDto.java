package com.colvir.ms.sys.rms.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

public class RequirementReviewDto {

    /** требование к пересмотру */
    public JsonNode requirement;

    /** приоритет оплаты */
    public Integer priority;

    /** сумма требования */
    public BigDecimal amount;

    @Override
    public String toString() {
        return "RequirementReviewDto{" +
            "requirement=" + requirement +
            ", priority=" + priority +
            ", amount=" + amount +
            '}';
    }

}
