package com.colvir.ms.sys.rms.dto;

public class RequirementReviewResultDto {

    /** информация по требованию (для работы из договоров) */
    public RequirementStateInfoDto requirementInfo;

    /** измененное требование */
    public Object requirement;

    @Override
    public String toString() {
        return "RequirementReviewResultDto{" +
            "requirementInfo=" + requirementInfo +
            ", requirement=" + requirement +
            '}';
    }
}
