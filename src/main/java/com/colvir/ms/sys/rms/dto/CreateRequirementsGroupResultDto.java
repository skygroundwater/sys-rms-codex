package com.colvir.ms.sys.rms.dto;

public class CreateRequirementsGroupResultDto {

    /** группа требований */
    public ReferenceDto requirementGroup;

    @Override
    public String toString() {
        return "CreateRequirementsGroupResultDto{" +
            "requirementGroup=" + requirementGroup +
            '}';
    }
}
