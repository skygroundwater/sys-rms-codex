package com.colvir.ms.sys.rms.dto;

import java.util.HashMap;
import java.util.Map;

public class WriteOffJournalDto extends JournalDto {

    /**
     * промежуточный результат исполнения операции
     */
    public WriteOffResultDto intermediateResult;

    public Map<Long, RequirementJournalDto> requirementJournalMap = new HashMap<>();

    /** атрибуты требования до изменений */
    public RequirementJournalDto requirementJournal;

    @Override
    public String toString() {
        return "WriteOffJournalDto{" +
            "isFirstRun=" + isFirstRun +
            ", intermediateResult=" + intermediateResult +
            ", requirementJournalMap=" + requirementJournalMap +
            ", requirementJournal=" + requirementJournal +
            '}';
    }
}
