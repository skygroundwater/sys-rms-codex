package com.colvir.ms.sys.rms.dto;

import java.util.List;

public class AdjustByPastDateDto {

    /**
     * требования по договору
     */
    public List<RequirementStateInfoDto> requirements;

    /**
     * входящие платежи
     */
    public List<WithdrawalResultDto> incomingPayments;

    /**
     * исходящие платежи
     */
    public List<AdjustRefundPaymentResultDto> outgoingPayments;

    @Override
    public String toString() {
        return "AdjustByPastDateDto{" +
            "requirements=" + requirements +
            ", incomingPayments=" + incomingPayments +
            ", outgoingPayments=" + outgoingPayments +
            '}';
    }
}
