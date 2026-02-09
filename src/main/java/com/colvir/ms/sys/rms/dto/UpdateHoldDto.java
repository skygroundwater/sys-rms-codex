package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;
import java.util.List;

public class UpdateHoldDto {

    /** требование */
    public ReferenceDto requirement;

    /** сумма холда (не обязательный) */
    public BigDecimal amount;

    /** валюта холда (не обязательный) */
    public ReferenceDto currency;

    @Override
    public String toString() {
        return "UpdateHoldDto{" +
                "requirement=" + requirement +
                ", amount=" + amount +
                ", currency=" + currency +
                '}';
    }
}
