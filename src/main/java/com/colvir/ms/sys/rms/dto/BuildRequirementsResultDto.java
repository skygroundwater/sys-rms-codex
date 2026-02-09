package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class BuildRequirementsResultDto {

    /**
     * требования по договору
     */
    private List<RequirementStateInfoDto> requirements = new ArrayList<>();

    @Override
    public String toString() {
        return "BuildRequirementsResultDto{" +
            "requirements=" + requirements +
            '}';
    }

    public List<RequirementStateInfoDto> getRequirements() {
        return requirements;
    }


}
