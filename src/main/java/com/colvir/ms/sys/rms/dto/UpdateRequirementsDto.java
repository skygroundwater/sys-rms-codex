package com.colvir.ms.sys.rms.dto;

import java.time.LocalDate;
import java.util.List;

public class UpdateRequirementsDto {

    /**
     * данные для оплаты
     */
    public List<RequirementStateInfoDto> paymentData;

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
        return "ModifyRequirementsDto{" +
            "paymentData=" + paymentData +
            ", businessDate=" + businessDate +
            ", contract='" + contract + '\'' +
            '}';
    }
}
