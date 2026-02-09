package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;
import java.util.List;

public class AdjustRefundPaymentResultDto {

    // вариант выплаты
    public ReferenceDto paymentType;

    // референс платежа / транзакции
    public String reference;

    // сумма
    public BigDecimal amount;

    // сумма в валюте платежа
    public BigDecimal amountPaid;

    // валюта
    public ReferenceDto currency;

    //код назначения платежа
    public String paymentPurposeCode;

    // разбивка по классификации платежей
    public List<AmountForPaymentPurposeDto> distribution;

    @Override
    public String toString() {
        return "AdjustRefundPaymentResultDto{" +
            "paymentType=" + paymentType +
            ", reference='" + reference + '\'' +
            ", amount=" + amount +
            ", amountPaid=" + amountPaid +
            ", currency=" + currency +
            ", paymentPurposeCode='" + paymentPurposeCode + '\'' +
            ", distribution=" + distribution +
            '}';
    }
}
