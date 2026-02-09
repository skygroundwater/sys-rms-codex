package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class ChangeRequirementsJournalDto {

    /** идентификаторы созданных требований */
    public List<Long> createdRequirements = new ArrayList<>();

    /** созданные при распределении сумм платежи */
    public List<Long> createdPayments = new ArrayList<>();

    /** созданные записи в таблице связанных платежей */
    public List<Long> createdRelatedPayments = new ArrayList<>();

    /** атрибуты требований до изменений */
    public List<RequirementJournalDto> requirementsBeforeChanges = new ArrayList<>();

    @Override
    public String toString() {
        return "ChangeRequirementsJournalDto{" +
            "createdRequirements=" + createdRequirements +
            ", createdPayments=" + createdPayments +
            ", createdRelatedPayments=" + createdRelatedPayments +
            ", requirementsBeforeChanges=" + requirementsBeforeChanges +
            '}';
    }
}
