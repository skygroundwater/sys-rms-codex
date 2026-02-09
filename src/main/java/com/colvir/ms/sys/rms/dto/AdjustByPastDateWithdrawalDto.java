package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;

public class AdjustByPastDateWithdrawalDto {

    // вариант списания денег
    public ReferenceDto withdrawalType;

    // референс платежа / транзакции
    public String reference;

    // сумма
    public BigDecimal amount;

    // код назначения платежа
    public String code;

    @Override
    public String toString() {
        return "WithdrawalAdjustByPastDateDto{" +
            "withdrawalType=" + withdrawalType +
            ", reference='" + reference + '\'' +
            ", amount=" + amount +
            ", code='" + code + '\'' +
            '}';
    }
}
