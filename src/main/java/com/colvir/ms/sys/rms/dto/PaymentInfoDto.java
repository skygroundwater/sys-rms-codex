package com.colvir.ms.sys.rms.dto;

import com.colvir.ms.sys.rms.generated.service.dto.PaymentDTO;

import java.math.BigDecimal;

public class PaymentInfoDto {

    /** платеж */
    public PaymentDTO payment;

    /** является новым */
    public Boolean isNewPayment;

    /** использованная сумма в валюте требования (часть суммы платежа, связанная с другими требованиями) */
    public BigDecimal usedAmount;

    /** использованная сумма в валюте платежа (часть суммы платежа, связанная с другими требованиями) */
    public BigDecimal usedAmountOfPayment;


    @Override
    public String toString() {
        return "PaymentInfoDto{" +
            "payment=" + payment +
            ", isNewPayment=" + isNewPayment +
            ", usedAmount=" + usedAmount +
            ", usedAmountOfPayment=" + usedAmountOfPayment +
            '}';
    }
}
