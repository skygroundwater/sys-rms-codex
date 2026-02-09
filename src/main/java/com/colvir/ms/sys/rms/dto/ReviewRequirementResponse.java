package com.colvir.ms.sys.rms.dto;

public class ReviewRequirementResponse {

    public ReviewRequirementResultDto reviewResult;

    public ReviewRequirementJournalDto reviewJournal;

    @Override
    public String toString() {
        return "ReviewRequirementResponse{" +
            "reviewResult=" + reviewResult +
            ", reviewJournal=" + reviewJournal +
            '}';
    }
}
