package com.colvir.ms.sys.rms.dto;

import java.time.LocalDate;

public class CreateRequirementDto {
    private RequirementStateInfoDto paymentData;
    private String initialBbpState;
    private ReferenceDto contract;
    private ReferenceDto currency;
    private ReferenceDto client;
    private LocalDate businessDate;


    public RequirementStateInfoDto getPaymentData() {
        return paymentData;
    }

    public CreateRequirementDto setPaymentData(RequirementStateInfoDto paymentData) {
        this.paymentData = paymentData;
        return this;
    }

    public String getInitialBbpState() {
        return initialBbpState;
    }

    public CreateRequirementDto setInitialBbpState(String initialBbpState) {
        this.initialBbpState = initialBbpState;
        return this;
    }

    public ReferenceDto getContract() {
        return contract;
    }

    public CreateRequirementDto setContract(ReferenceDto contract) {
        this.contract = contract;
        return this;
    }

    public ReferenceDto getCurrency() {
        return currency;
    }

    public CreateRequirementDto setCurrency(ReferenceDto currency) {
        this.currency = currency;
        return this;
    }

    public ReferenceDto getClient() {
        return client;
    }

    public CreateRequirementDto setClient(ReferenceDto client) {
        this.client = client;
        return this;
    }

    public LocalDate getBusinessDate() {
        return businessDate;
    }

    public CreateRequirementDto setBusinessDate(LocalDate businessDate) {
        this.businessDate = businessDate;
        return this;
    }

    @Override
    public String toString() {
        return "CreateRequirementDto{" +
            "paymentData=" + paymentData +
            ", initialBbpState='" + initialBbpState + '\'' +
            ", contract=" + contract +
            ", currency=" + currency +
            ", client=" + client +
            ", businessDate=" + businessDate +
            '}';
    }
}
