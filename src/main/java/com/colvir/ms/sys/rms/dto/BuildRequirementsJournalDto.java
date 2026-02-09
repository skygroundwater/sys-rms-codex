package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class BuildRequirementsJournalDto extends JournalDto {

    private List<String> processStateIds = new ArrayList<>();

    private List<Long> requirementIdList = new ArrayList<>();

    public boolean isFirstRun() {
        return isFirstRun;
    }

    public BuildRequirementsJournalDto setFirstRun(boolean firstRun) {
        isFirstRun = firstRun;
        return this;
    }

    public void setRequirementIdList(List<Long> requirementIdList) {
        this.requirementIdList = requirementIdList;
    }

    public List<Long> getRequirementIdList() {
        return requirementIdList;
    }

    public List<String> getProcessStateIds() {
        return processStateIds;
    }

    @Override
    public String toString() {
        return "BuildRequirementsJournalDto{" +
            "isFirstRun=" + isFirstRun +
            ", processStateIds=" + processStateIds +
            ", requirementIdList=" + requirementIdList +
            '}';
    }
}
