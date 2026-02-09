package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;

public class PaymentInfoExtendedDto extends PaymentInfoDto {

    /** сумма платежа в валюте требования */
    public BigDecimal amount;

    @Override
    public String toString() {
        return "PaymentInfoExtendedDto{" +
            "amount=" + amount +
            '}';
    }
}
