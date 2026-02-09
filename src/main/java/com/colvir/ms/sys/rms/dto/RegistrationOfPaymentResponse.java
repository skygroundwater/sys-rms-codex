package com.colvir.ms.sys.rms.dto;

import com.colvir.ms.sys.rms.generated.service.dto.RequirementDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationOfPaymentResponse {

    /** измененный массив требований */
    public List<RequirementDTO> requirements = new ArrayList<>();

    /** информация о платежах */
    public List<PaymentInfoDto> paymentsInfo = new ArrayList<>();

    /** информация об исполненных платежах по требованию */
    public Map<Long, BigDecimal> currentTransactionAmounts = new HashMap<>();

    /** информация для отката изменений */
    public RegistrationOfPaymentJournalDto journal = new RegistrationOfPaymentJournalDto();

    @Override
    public String toString() {
        return "RegistrationOfPaymentResponse{" +
            "requirements=" + requirements +
            ", paymentsInfo=" + paymentsInfo +
            ", currentTransactionAmounts=" + currentTransactionAmounts +
            ", journal=" + journal +
            '}';
    }
}
