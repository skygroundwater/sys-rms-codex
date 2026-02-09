package com.colvir.ms.sys.rms.dto;

import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RequirementStateInfoDto {

    /**
     * оплаченная сумма
     */
    public BigDecimal payedAmount;

    /**
     * приоритет оплаты
     */
    public Integer priority;

    /**
     * расчетная категория
     */
    public RequirementIndicatorDto indicator;

    /**
     * состояние требования
     */
    public RequirementStatus status;

    /**
     * ссылка на требование
     */
    public Long requirementId;

    /**
     * сумма к оплате
     */
    public BigDecimal amount;

    /**
     * текущая сумма исполнения в рамках операции
     */
    public BigDecimal currentTransactionAmount;

    /**
     * что делать с требованием
     */
    public RequirementAction action;

    /**
     * код назначения платежа
     */
    public String paymentPurposeCode;

    public LocalDate paymentEndDate;

    @Override
    public String toString() {
        return "RequirementStateInfoDto{" +
            "payedAmount=" + payedAmount +
            ", priority=" + priority +
            ", indicator=" + indicator +
            ", status=" + status +
            ", requirementId=" + requirementId +
            ", amount=" + amount +
            ", currentTransactionAmount=" + currentTransactionAmount +
            ", action=" + action +
            ", paymentPurposeCode='" + paymentPurposeCode + '\'' +
            ", paymentEndDate=" + paymentEndDate +
            '}';
    }
}
