package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class RequirementAmountResponse {

    /** запрошенные суммы в разбивке по расчетным категориям */
    public List<AmountForIndicatorDto> amounts = new ArrayList<>();

    @Override
    public String toString() {
        return "RequirementAmountResponse{" +
            "amounts=" + amounts +
            '}';
    }
}
