package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NonPaidRequirementsResultDto {

    /**
     * неоплаченные требования по договору
     */
    public List<RequirementStateInfoDto> requirements = new ArrayList<>();

    /**
     * общая сумма к оплате
     */
    public BigDecimal unpaidAmount = BigDecimal.ZERO;

    @Override
    public String toString() {
        return "NonPaidRequirementsResultDto{" +
            "requirements=" + requirements +
            ", unpaidAmount=" + unpaidAmount +
            '}';
    }
}
