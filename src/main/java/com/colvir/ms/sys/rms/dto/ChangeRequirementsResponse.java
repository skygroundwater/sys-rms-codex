package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class ChangeRequirementsResponse {

    /** измененный массив требований по договору */
    public List<RequirementStateInfoDto> requirements = new ArrayList<>();

    /** информация для отката изменений */
    public ChangeRequirementsJournalDto journal = new ChangeRequirementsJournalDto();

    @Override
    public String toString() {
        return "ChangeRequirementsResponse{" +
            "requirements=" + requirements +
            ", journal=" + journal +
            '}';
    }
}
