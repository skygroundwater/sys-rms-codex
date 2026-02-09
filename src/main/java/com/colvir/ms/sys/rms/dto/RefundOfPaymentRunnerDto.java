package com.colvir.ms.sys.rms.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

public class RefundOfPaymentRunnerDto {

    /** сумма возврата (в валюте платежа), если не передана, считаем, что возвращается полная сумма платежа */
    public BigDecimal amount;

    /** возвращаемый платеж */
    public JsonNode payment;

    @Override
    public String toString() {
        return "RefundOfPaymentRunnerDto{" +
            "amount=" + amount +
            ", payment=" + payment +
            '}';
    }
}
