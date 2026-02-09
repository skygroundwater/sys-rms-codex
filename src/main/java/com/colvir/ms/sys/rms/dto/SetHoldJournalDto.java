package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetHoldJournalDto extends JournalDto {

    public Long requirementId;

    public Set<Long> createdHolds = new HashSet<>();

    public List<RequirementHoldInfoDto> holdResults = new ArrayList<>();

    @Override
    public String toString() {
        return "SetHoldJournalDto{" +
                "isFirstRun=" + isFirstRun +
                ", requirementId=" + requirementId +
                ", createdHolds=" + createdHolds +
                ", holdResults=" + holdResults +
                '}';
    }
}
