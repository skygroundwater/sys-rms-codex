package com.colvir.ms.sys.rms.dto;

public class RequirementIndicatorDto extends ReferenceDto {

    public String code;
    public ReferenceDto indicatorDescr;

    @Override
    public String toString() {
        return "RequirementIndicatorDto{" +
            "code='" + code + '\'' +
            ", indicatorDescr=" + indicatorDescr +
            '}';
    }
}
