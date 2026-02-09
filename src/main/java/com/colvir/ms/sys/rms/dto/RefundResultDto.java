package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class RefundResultDto {

    /** измененный массив требований */
    public List<RequirementStateInfoDto> requirementsInfo = new ArrayList<>();

    /** информация по суммам возврата */
    public List<PaymentRefundInfoDto> refundsInfo = new ArrayList<>();

    @Override
    public String toString() {
        return "RefundResultDto{" +
            "requirementsInfo=" + requirementsInfo +
            ", refundsInfo=" + refundsInfo +
            '}';
    }
}
