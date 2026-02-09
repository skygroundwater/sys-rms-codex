package com.colvir.ms.sys.rms.dto;

import java.time.LocalDate;

public class UpdateSingleRequirementDto {
    /**
     * данные для оплаты
     */
    private RequirementStateInfoDto paymentData;

    /**
     * дата операционного дня
     */
    private LocalDate businessDate;

    /**
     * ссылка на договор
     */
    private ReferenceDto contract;

    @Override
    public String toString() {
        return "UpdateSingleRequirementDto{" +
            "paymentData=" + paymentData +
            ", businessDate=" + businessDate +
            ", contract='" + contract + '\'' +
            '}';
    }

    public RequirementStateInfoDto getPaymentData() {
        return paymentData;
    }

    public UpdateSingleRequirementDto setPaymentData(RequirementStateInfoDto paymentData) {
        this.paymentData = paymentData;
        return this;
    }

    public LocalDate getBusinessDate() {
        return businessDate;
    }

    public UpdateSingleRequirementDto setBusinessDate(LocalDate businessDate) {
        this.businessDate = businessDate;
        return this;
    }

    public ReferenceDto getContract() {
        return contract;
    }

    public UpdateSingleRequirementDto setContract(ReferenceDto contract) {
        this.contract = contract;
        return this;
    }
}
