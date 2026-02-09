package com.colvir.ms.sys.rms.dto;

import com.colvir.ms.sys.rms.generated.service.dto.RequirementDTO;

public class RequirementWriteOffResponse {

    /** измененное требование */
    public RequirementDTO requirement;

    /** атрибуты требования до изменений */
    public RequirementJournalDto requirementJournal;

    @Override
    public String toString() {
        return "RequirementWriteOffResponse{" +
            "requirement=" + requirement +
            ", requirementJournal=" + requirementJournal +
            '}';
    }
}
