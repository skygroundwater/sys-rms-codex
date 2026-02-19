package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjustByPastDateJournalDto extends JournalDto {

    public AdjustByPastDateResultDto intermediateResult = new AdjustByPastDateResultDto();

    public Map<Long, RequirementJournalDto> requirementJournalMap = new HashMap<>();

    public List<Long> requirementRefundingPaymentIds = new ArrayList<>();

    public List<Long> refundingPaymentIds = new ArrayList<>();

    public List<Long> paymentIds = new ArrayList<>();

    public List<Long> relatedPaymentIds = new ArrayList<>();

    public List<RelatedPaymentsJournalDto> redistributedRelatedPayments = new ArrayList<>();

    @Override
    public String toString() {
        return "AdjustByPastDateJournalDto{" +
            "intermediateResult=" + intermediateResult +
            ", requirementJournalMap=" + requirementJournalMap +
            ", requirementRefundingPaymentIds=" + requirementRefundingPaymentIds +
            ", refundingPaymentIds=" + refundingPaymentIds +
            ", paymentIds=" + paymentIds +
            ", relatedPaymentIds=" + relatedPaymentIds +
            ", redistributedRelatedPayments=" + redistributedRelatedPayments +
            ", isFirstRun=" + isFirstRun +
            '}';
    }
}
