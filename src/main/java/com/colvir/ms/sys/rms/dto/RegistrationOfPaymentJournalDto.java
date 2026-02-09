package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class RegistrationOfPaymentJournalDto {

    /** созданные при распределении сумм платежи */
    public List<Long> createdPayments = new ArrayList<>();

    /** созданные записи в таблице связанных платежей */
    public List<Long> createdRelatedPayments = new ArrayList<>();

    /** атрибуты требований до изменений */
    public List<RequirementJournalDto> requirementJournal = new ArrayList<>();

    @Override
    public String toString() {
        return "RegistrationOfPaymentJournalDto{" +
            "createdPayments=" + createdPayments +
            ", createdRelatedPayments=" + createdRelatedPayments +
            ", requirementJournal=" + requirementJournal +
            '}';
    }
}
