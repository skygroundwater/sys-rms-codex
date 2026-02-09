package com.colvir.ms.sys.rms.dto;


import java.util.ArrayList;
import java.util.List;

public class RefundOfPaymentResultDto {

    /** измененный массив требований */
    public List<RequirementStateInfoDto> requirementsInfo = new ArrayList<>();

    /** возвращаемый платеж */
    public Object payment;

    @Override
    public String toString() {
        return "RefundOfPaymentResultDto{" +
            "requirementsInfo=" + requirementsInfo +
            ", payment=" + payment +
            '}';
    }
}
