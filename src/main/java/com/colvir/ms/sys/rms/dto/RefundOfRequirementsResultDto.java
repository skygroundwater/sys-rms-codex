package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class RefundOfRequirementsResultDto {

    /** измененный массив требований */
    public List<RequirementStateInfoDto> requirementsInfo = new ArrayList<>();

    /** требования (список или одиночное значение) */
    public Object requirements;

    @Override
    public String toString() {
        return "RefundOfRequirementsResultDto{" +
            "requirementsInfo=" + requirementsInfo +
            ", requirements=" + requirements +
            '}';
    }
}
