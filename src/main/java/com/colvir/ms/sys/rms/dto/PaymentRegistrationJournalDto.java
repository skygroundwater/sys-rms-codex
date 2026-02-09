package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentRegistrationJournalDto extends JournalDto {

    /** созданные при распределении сумм платежи */
    public List<Long> createdPayments = new ArrayList<>();

    /** созданные записи в таблице связанных платежей */
    public List<Long> createdRelatedPayments = new ArrayList<>();

    /** атрибуты требований до изменений */
    public Map<Long, RequirementJournalDto> requirementJournalMap = new HashMap<>();

    /**
     * Промежуточный результат выполнения шага
     */
    public PaymentRegistrationResultDto intermediateResult;

    @Override
    public String toString() {
        return "PaymentRegistrationJournalDto{" +
            "isFirstRun=" + isFirstRun +
            ", createdPayments=" + createdPayments +
            ", createdRelatedPayments=" + createdRelatedPayments +
            ", requirementJournal=" + requirementJournalMap +
            '}';
    }
}
