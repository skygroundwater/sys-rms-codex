package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;

public class AmountForIndicatorDto {

    /** расчетная категория */
    public ReferenceDto indicator;

    /** запрошенная сумма */
    public BigDecimal amount;

    @Override
    public String toString() {
        return "AmountForIndicatorDto{" +
            "indicator=" + indicator +
            ", amount=" + amount +
            '}';
    }
}
