package com.colvir.ms.sys.rms.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChangeRequirementsDto {

    /** дата операционного дня */
    public LocalDate businessDate;

    /** ссылка на договор */
    public ReferenceDto contract;

    /** валюта договора */
    public ReferenceDto currency;

    /** клиент договора */
    public ReferenceDto client;

    /** исполненные платежи */
    public List<WithdrawalResultDto> payments = new ArrayList<>();

    /** требования по договору */
    public List<RequirementStateInfoDto> requirements = new ArrayList<>();

    @Override
    public String toString() {
        return "ChangeRequirementsDto{" +
            "businessDate=" + businessDate +
            ", contract=" + contract +
            ", currency=" + currency +
            ", client=" + client +
            ", payments=" + payments +
            ", requirements=" + requirements +
            '}';
    }
}
