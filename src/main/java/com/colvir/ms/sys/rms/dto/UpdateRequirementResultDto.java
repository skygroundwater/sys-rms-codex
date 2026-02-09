package com.colvir.ms.sys.rms.dto;

public class UpdateRequirementResultDto {
    private RequirementJournalDto journalDto;
    private RequirementStateInfoDto stateInfoDto;

    public RequirementJournalDto getJournalDto() {
        return journalDto;
    }

    public UpdateRequirementResultDto setJournalDto(RequirementJournalDto journalDto) {
        this.journalDto = journalDto;
        return this;
    }

    public RequirementStateInfoDto getStateInfoDto() {
        return stateInfoDto;
    }

    public UpdateRequirementResultDto setStateInfoDto(RequirementStateInfoDto stateInfoDto) {
        this.stateInfoDto = stateInfoDto;
        return this;
    }

    @Override
    public String toString() {
        return "UpdateRequirementResultDto{" +
            "journalDto=" + journalDto +
            ", stateInfoDto=" + stateInfoDto +
            '}';
    }

}
