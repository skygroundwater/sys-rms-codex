package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;

public class RefundOfPaymentDto {

    /** сумма возврата (в валюте платежа), если не передана, считаем, что возвращается полная сумма платежа */
    public BigDecimal refundAmount;

    /** возвращаемый платеж */
    public ReferenceDto payment;

    @Override
    public String toString() {
        return "RefundOfPaymentDto{" +
            "refundAmount=" + refundAmount +
            ", payment=" + payment +
            '}';
    }
}
