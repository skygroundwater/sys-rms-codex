package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class UpdateRequirementsResultDto {

    /**
     * измеменный массив информации о требованиях по договору
     */
    public List<RequirementStateInfoDto> requirements = new ArrayList<>();

    @Override
    public String toString() {
        return "UpdateRequirementsResultDto{" +
            "requirements=" + requirements +
            '}';
    }
}
