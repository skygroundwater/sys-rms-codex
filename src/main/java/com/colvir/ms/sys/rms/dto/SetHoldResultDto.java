package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class SetHoldResultDto {

    /** созданные холды */
    public List<RequirementHoldInfoDto> holdResult = new ArrayList<>();

    @Override
    public String toString() {
        return "SetHoldResultDto{" +
            "holdResult=" + holdResult +
            '}';
    }
}
