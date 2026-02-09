package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;

public class PaymentRefundInfoDto {

    /** ссылка на платеж */
    public Long paymentId;

    /** возвращаемая сумма платежа */
    public BigDecimal amount;

    /** валюта платежа */
    public Long currencyId;

    @Override
    public String toString() {
        return "PaymentRefundInfoDto{" +
            "paymentId=" + paymentId +
            ", amount=" + amount +
            ", currencyId=" + currencyId +
            '}';
    }
}
