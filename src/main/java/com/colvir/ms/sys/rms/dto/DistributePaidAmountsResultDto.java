package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class DistributePaidAmountsResultDto {

    /**
     * требования по договору
     */
    public List<RequirementStateInfoDto> requirements = new ArrayList<>();

    @Override
    public String toString() {
        return "DistributePaidAmountsResultDto{" +
            "requirements=" + requirements +
            '}';
    }
}
