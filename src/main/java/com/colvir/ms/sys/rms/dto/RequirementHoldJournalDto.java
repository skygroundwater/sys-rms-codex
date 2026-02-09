package com.colvir.ms.sys.rms.dto;

import java.math.BigDecimal;

public class RequirementHoldJournalDto {

    public Long id;

    /** идентификатор требования */
    public Long requirementId;

    /** вариант списания денег */
    public Long withdrawalTypeId;

    /** номер счета */
    public String accountNumber;

    /** сумма */
    public BigDecimal amount;

    /** валюта */
    public Long currencyId;

    /** холд по online счету */
    public Long holdId;

    /** референс холда во внешней системе / обобщенная ссылка на холд */
    public String reference;

    /** является удалённым объектом */
    public Boolean isDeleted;

    @Override
    public String toString() {
        return "RequirementHoldJournalDto{" +
            "id=" + id +
            ", requirementId=" + requirementId +
            ", withdrawalTypeId=" + withdrawalTypeId +
            ", accountNumber='" + accountNumber + '\'' +
            ", amount=" + amount +
            ", currencyId=" + currencyId +
            ", holdId=" + holdId +
            ", reference='" + reference + '\'' +
            ", isDeleted=" + isDeleted +
            '}';
    }
}
