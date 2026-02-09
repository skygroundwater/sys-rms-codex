package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;
import java.util.List;

public class SetHoldDto {

    /** требование */
    public ReferenceDto requirement;

    /** правила списания */
    public List<WithdrawalRuleDto> withdrawalRules;

    /** клиент */
    public ReferenceDto client;

    /** договор */
    public ReferenceDto contract;

    /** сумма холда */
    public BigDecimal amount;

    /** валюта холда */
    public ReferenceDto currency;

    /** вид холда */
    public ReferenceDto holdType;

    @Override
    public String toString() {
        return "SetHoldDto{" +
            "requirement=" + requirement +
            ", withdrawalRules=" + withdrawalRules +
            ", client=" + client +
            ", contract=" + contract +
            ", amount=" + amount +
            ", currency=" + currency +
            ", holdType=" + holdType +
            '}';
    }
}
