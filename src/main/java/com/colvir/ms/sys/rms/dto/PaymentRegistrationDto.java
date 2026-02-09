package com.colvir.ms.sys.rms.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class PaymentRegistrationDto {

    /** исполненные платежи */
    public List<WithdrawalResultDto> payments = new ArrayList<>();

    /** требования (список или одиночное значение) */
    public JsonNode requirements;

    @Override
    public String toString() {
        return "PaymentRegistrationDto{" +
            "payments=" + payments +
            ", requirements=" + requirements +
            '}';
    }
}
