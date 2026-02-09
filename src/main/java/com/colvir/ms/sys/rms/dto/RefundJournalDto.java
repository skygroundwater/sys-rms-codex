package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RefundJournalDto extends JournalDto {

    /** созданные записи по возврату платежей */
    public List<Long> createdPaymentRefunds = new ArrayList<>();

    /** атрибуты связанных платежей до изменений */
    public List<RelatedPaymentsJournalDto> relatedPaymentsJournal = new ArrayList<>();

    /** атрибуты требований до изменений */
    public Map<Long, RequirementJournalDto> requirementJournal = new HashMap<>();

    /** промежуточный результат выполнения операции возврата платежа */
    public RefundOfPaymentResultDto intermediateRefundOfPaymentResult = new RefundOfPaymentResultDto();

    /** промежуточный результат выполнения операции возврата требования */
    public RefundOfRequirementsResultDto intermediateRefundOfRequirementResult = new RefundOfRequirementsResultDto();

    @Override
    public String toString() {
        return "RefundJournalDto{" +
            "createdPaymentRefunds=" + createdPaymentRefunds +
            ", relatedPaymentsJournal=" + relatedPaymentsJournal +
            ", requirementJournal=" + requirementJournal +
            ", intermediateRefundOfPaymentResult=" + intermediateRefundOfPaymentResult +
            ", intermediateRefundOfRequirementResult=" + intermediateRefundOfRequirementResult +
            '}';
    }
}
