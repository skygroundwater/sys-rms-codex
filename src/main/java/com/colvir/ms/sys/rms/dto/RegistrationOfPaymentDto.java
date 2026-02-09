package com.colvir.ms.sys.rms.dto;

import java.util.ArrayList;
import java.util.List;

public class RegistrationOfPaymentDto {

    /** исполненные платежи */
    public List<WithdrawalResultDto> payments = new ArrayList<>();

    /** требования по договору */
    public List<ReferenceDto> requirements = new ArrayList<>();

    @Override
    public String toString() {
        return "RegistrationOfPaymentDto{" +
            "payments=" + payments +
            ", requirements=" + requirements +
            '}';
    }
}
