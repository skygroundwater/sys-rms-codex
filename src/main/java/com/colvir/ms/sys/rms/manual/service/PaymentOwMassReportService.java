package com.colvir.ms.sys.rms.manual.service;

import com.colvir.ms.sys.rms.dto.PaymentOwMassResultDto;

import java.time.LocalDate;
import java.util.List;


public interface PaymentOwMassReportService {
    List<PaymentOwMassResultDto> getPaymentOwMassData(LocalDate date);
}
