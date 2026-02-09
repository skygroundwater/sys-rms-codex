package com.colvir.ms.sys.rms.dto;

import java.time.LocalDate;

public class NonPaidRequirementsDto {

    /**
     * дата операционного дня
     */
    public LocalDate businessDate;

    /**
     * ссылка на договор
     */
    public ReferenceDto contract;

    @Override
    public String toString() {
        return "NonPaidRequirementsDto{" +
            "businessDate=" + businessDate +
            ", contract='" + contract + '\'' +
            '}';
    }

}
