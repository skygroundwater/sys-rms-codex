package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;

public class ReviewRequirementDto {

    /** требование к пересмотру */
    public ReferenceDto requirement;

    /** приоритет оплаты */
    public Integer priority;

    /** сумма требования */
    public BigDecimal amount;

    @Override
    public String toString() {
        return "ReviewRequirementDto{" +
            "requirement=" + requirement +
            ", priority=" + priority +
            ", amount=" + amount +
            '}';
    }
}
