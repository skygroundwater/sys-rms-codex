package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class UpdateHoldJournalDto extends JournalDto {

    public Long requirementId;

    public List<RequirementHoldInfoDto> holdResults = new ArrayList<>();

    public List<RequirementHoldJournalDto> holdJournal = new ArrayList<>();

    @Override
    public String toString() {
        return "UpdateHoldJournalDto{" +
            "isFirstRun=" + isFirstRun +
            ", requirementId=" + requirementId +
            ", holdResults=" + holdResults +
            ", holdJournal=" + holdJournal +
            '}';
    }
}
