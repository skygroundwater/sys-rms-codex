package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;

public class RelatedPaymentsJournalDto {

    public Long relationId;

    public BigDecimal amount;

    public BigDecimal amountOfPayment;

    public Long requirementId;

    public Long paymentId;

    @Override
    public String toString() {
        return "RelatedPaymentsJournalDto{" +
            "relationId=" + relationId +
            ", amount=" + amount +
            ", amountOfPayment=" + amountOfPayment +
            ", requirementId=" + requirementId +
            ", paymentId=" + paymentId +
            '}';
    }
}
