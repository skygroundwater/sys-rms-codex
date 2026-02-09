package com.colvir.ms.sys.rms.dto;

import java.util.List;

public class DistributePaidAmountsDto {

    /**
     * исполненные платежи
     */
    public List<WithdrawalResultDto> payments;

    /**
     * требования по договору
     */
    public List<RequirementStateInfoDto> requirements;

    @Override
    public String toString() {
        return "DistributePaidAmountsDto{" +
            "payments=" + payments +
            ", requirements=" + requirements +
            '}';
    }
}
