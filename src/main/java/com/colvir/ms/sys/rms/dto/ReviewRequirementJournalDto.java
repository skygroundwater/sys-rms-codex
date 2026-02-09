package com.colvir.ms.sys.rms.dto;


public class ReviewRequirementJournalDto {

    /** атрибуты требования до изменений */
    public RequirementJournalDto requirementJournal;

    /** журнал возврата платежа */
    public RefundJournalDto refundJournal;

    @Override
    public String toString() {
        return "ReviewRequirementJournalDto{" +
            "requirementJournal=" + requirementJournal +
            ", refundJournal=" + refundJournal +
            '}';
    }
}
