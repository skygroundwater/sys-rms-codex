package com.colvir.ms.sys.rms.dto;

public class CreatePaymentData {
    private Boolean isSuccess;
    private Object primaryKey;

    private Boolean isPartialPaid;
    private Boolean isPaid;

    private String requirementId;

    public CreatePaymentData(String requirementId, Boolean isPartialPaid) {
        this.isPartialPaid = isPartialPaid;
        this.requirementId = requirementId;
        this.isPaid = false;
        this.isSuccess = false;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Object primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Boolean getPartialPaid() {
        return isPartialPaid;
    }

    public void setPartialPaid(Boolean partialPaid) {
        isPartialPaid = partialPaid;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "CreatePaymentData{" +
               "isSuccess=" + isSuccess +
               ", primaryKey=" + primaryKey +
               ", isPartialPaid=" + isPartialPaid +
               ", isPaid=" + isPaid +
               '}';
    }
}
