package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;

public class PaymentOwMassResultDto {

    public String accountCode;

    public String currencyCode;

    public String code;

    public String name;

    public String taxId;

    public String reference;

    public BigDecimal amount;

    @Override
    public String toString() {
        return "PaymentOwMassResultDto{" +
            "accountCode='" + accountCode + '\'' +
            ", currencyCode='" + currencyCode + '\'' +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", taxId='" + taxId + '\'' +
            ", reference='" + reference + '\'' +
            ", amount=" + amount +
            '}';
    }
}
