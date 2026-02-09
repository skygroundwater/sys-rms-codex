package com.colvir.ms.sys.rms.dto;



public class UpdateSingleRequirementResultDto {
    /**
     * измеменный массив информации о требованиях по договору
     */
    private RequirementStateInfoDto requirement;

    @Override
    public String toString() {
        return "UpdateSingleRequirementJournalDto{" +
            "requirement=" + requirement +
            '}';
    }

    public RequirementStateInfoDto getRequirement() {
        return requirement;
    }

    public UpdateSingleRequirementResultDto setRequirement(RequirementStateInfoDto requirement) {
        this.requirement = requirement;
        return this;
    }
}
