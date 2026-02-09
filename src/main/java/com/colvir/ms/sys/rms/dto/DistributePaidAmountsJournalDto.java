package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributePaidAmountsJournalDto extends JournalDto {

    /**
     * созданные при распределении сумм платежи
     */
    public List<Long> createdPayments = new ArrayList<>();

    /**
     * созданные записи в таблице связанных платежей
     */
    public List<Long> createdRelatedPayments = new ArrayList<>();

    /**
     * атрибуты требований до изменений
     */
    public Map<Long, RequirementJournalDto> requirementJournalMap = new HashMap<>();

    /**
     * промежуточный результат выполненной операции
     */
    public DistributePaidAmountsResultDto intermediateResult = new DistributePaidAmountsResultDto();

    @Override
    public String toString() {
        return "DistributePaidAmountsJournalDto{" +
            "createdPayments=" + createdPayments +
            ", createdRelatedPayments=" + createdRelatedPayments +
            ", requirementJournalMap=" + requirementJournalMap +
            '}';
    }
}
