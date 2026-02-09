package com.colvir.ms.sys.rms.dto;

public class RequirementReviewJournalDto extends JournalDto {

    /** атрибуты требования до изменений */
    public RequirementJournalDto requirementJournal;

    /** журнал возврата платежа */
    public RefundJournalDto refundJournal;

    /** промежуточный результат выполнения операции **/
    public RequirementReviewResultDto intermediateResult;

    @Override
    public String toString() {
        return "RequirementReviewJournalDto{" +
            "requirementJournal=" + requirementJournal +
            ", refundJournal=" + refundJournal +
            '}';
    }
}
