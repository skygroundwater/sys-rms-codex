package com.colvir.ms.sys.rms.dto;

import java.time.LocalDate;
import java.util.List;

public class BuildRequirementsDto {

    /**
     * данные для оплаты
     */
    private List<RequirementStateInfoDto> paymentData;

    /**
     * дата операционного дня
     */
    private LocalDate businessDate;

    /**
     * ссылка на договор
     */
    private ReferenceDto contract;

    /**
     * валюта договора
     */
    private ReferenceDto currency;

    /**
     * клиент договора
     */
    private ReferenceDto client;


    @Override
    public String toString() {
        return "BuildRequirementsDto{" +
            "paymentData=" + paymentData +
            ", businessDate=" + businessDate +
            ", contract=" + contract +
            ", currency=" + currency +
            ", client=" + client +
            '}';
    }

    public List<RequirementStateInfoDto> getPaymentData() {
        return paymentData;
    }

    public BuildRequirementsDto setPaymentData(List<RequirementStateInfoDto> paymentData) {
        this.paymentData = paymentData;
        return this;
    }

    public LocalDate getBusinessDate() {
        return businessDate;
    }

    public BuildRequirementsDto setBusinessDate(LocalDate businessDate) {
        this.businessDate = businessDate;
        return this;
    }

    public ReferenceDto getContract() {
        return contract;
    }

    public BuildRequirementsDto setContract(ReferenceDto contract) {
        this.contract = contract;
        return this;
    }

    public ReferenceDto getCurrency() {
        return currency;
    }

    public BuildRequirementsDto setCurrency(ReferenceDto currency) {
        this.currency = currency;
        return this;
    }

    public ReferenceDto getClient() {
        return client;
    }

    public BuildRequirementsDto setClient(ReferenceDto client) {
        this.client = client;
        return this;
    }
}
