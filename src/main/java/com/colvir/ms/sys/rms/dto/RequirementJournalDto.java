package com.colvir.ms.sys.rms.dto;

import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;


public class RequirementJournalDto {

    /**
     * идентификатор
     */
    public Long id;

    /**
     * оплаченная сумма
     */
    public BigDecimal paidAmount;

    /**
     * неоплаченная сумма
     */
    public BigDecimal unpaidAmount;

    /**
     * сумма
     */
    public BigDecimal amount;

    /**
     * списанная сумма
     */
    public BigDecimal writeOffAmount;

    /**
     * состояние
     */
    public RequirementStatus state;

    /**
     * состояние требования
     */
    public String bbpJournalId;

    /**
     * идентификатор процесса
     */
    public String bbpProcessId;

    /**
     * код состояния
     */
    public String bbpStateCode;

    /**
     * приоритет
     */
    public BigDecimal priority;

    /**
     * фактическая дата оплаты
     */
    public LocalDate actualPaymentDate;

    public LocalDate paymentEndDate;

    /**
     * расчетная категория
     */
    public RequirementIndicatorDto indicator;

    @Override
    public String toString() {
        return "RequirementJournalDto{" +
            "id=" + id +
            ", paidAmount=" + paidAmount +
            ", unpaidAmount=" + unpaidAmount +
            ", amount=" + amount +
            ", writeOffAmount=" + writeOffAmount +
            ", state=" + state +
            ", bbpJournalId='" + bbpJournalId + '\'' +
            ", bbpProcessId='" + bbpProcessId + '\'' +
            ", bbpStateCode='" + bbpStateCode + '\'' +
            ", priority=" + priority +
            ", actualPaymentDate=" + actualPaymentDate +
            ", paymentEndDate=" + paymentEndDate +
            ", indicator=" + indicator +
            '}';
    }
}
