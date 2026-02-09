package com.colvir.ms.sys.rms.dto;

public class UpdateSingleRequirementJournalDto extends JournalDto {

    /**
     * атрибуты требований до изменений
     */
    private RequirementJournalDto requirementJournal;

    /**
     * промежуточный результат исполнения шага
     */
    private UpdateSingleRequirementResultDto intermediateResult;

    @Override
    public String toString() {
        return "UpdateSingleRequirementJournalDto{" +
            "isFirstRun=" + isFirstRun +
            ", requirementJournal=" + requirementJournal +
            ", intermediateResult=" + intermediateResult +
            '}';
    }

    public RequirementJournalDto getRequirementJournal() {
        return requirementJournal;
    }

    public boolean isFirstRun() {
        return isFirstRun;
    }

    public void setFirstRun(boolean firstRun) {
        isFirstRun = firstRun;
    }

    public void setIntermediateResult(UpdateSingleRequirementResultDto intermediateResult) {
        this.intermediateResult = intermediateResult;
    }

    public UpdateSingleRequirementResultDto getIntermediateResult() {
        return intermediateResult;
    }

    public UpdateSingleRequirementJournalDto setRequirementJournal(RequirementJournalDto requirementJournal) {
        this.requirementJournal = requirementJournal;
        return this;
    }
}
