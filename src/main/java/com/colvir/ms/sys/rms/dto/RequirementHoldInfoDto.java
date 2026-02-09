package com.colvir.ms.sys.rms.dto;

import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementHoldState;

import java.math.BigDecimal;

public class RequirementHoldInfoDto {

    /** вариант списания */
    public ReferenceDto withdrawalType;

    /** ссылка на холд */
    public ReferenceDto hold;

    /** референс холда / ссылка на холд */
    public String reference;

    /** номер счета */
    public String accountNumber;

    /** сумма холда (может отличаться от переданной суммы) */
    public BigDecimal amount;

    /** валюта холда (может отличаться от переданной валюты) */
    public ReferenceDto currency;

    /** статус холда */
    public RequirementHoldState holdState;

    @Override
    public String toString() {
        return "RequirementHoldInfoDto{" +
            "withdrawalType=" + withdrawalType +
            ", hold=" + hold +
            ", reference='" + reference + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", amount=" + amount +
            ", currency=" + currency +
            ", holdState=" + holdState +
            '}';
    }
}
