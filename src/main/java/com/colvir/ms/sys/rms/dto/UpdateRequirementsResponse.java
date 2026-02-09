package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class UpdateRequirementsResponse {

    /**
     * измеменный массив информации о требованиях по договору
     */
    public List<RequirementStateInfoDto> requirements = new ArrayList<>();

    /**
     * атрибуты требований до изменений
     */
    public List<RequirementJournalDto> requirementJournal = new ArrayList<>();

    @Override
    public String toString() {
        return "UpdateRequirementsResponse{" +
            "requirements=" + requirements +
            ", requirementJournal=" + requirementJournal +
            '}';
    }
}
