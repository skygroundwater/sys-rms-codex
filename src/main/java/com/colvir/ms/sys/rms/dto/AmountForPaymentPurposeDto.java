package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;

public class AmountForPaymentPurposeDto {

    // Сумма в разрезе назначения платежа

    // сумма
    public BigDecimal amount;

    // очередность списания
    public Integer priority;

    // код
    public String code;

    @Override
    public String toString() {
        return "AmountForPaymentPurposeDto{" +
            "amount=" + amount +
            ", priority=" + priority +
            ", code='" + code + '\'' +
            '}';
    }
}
