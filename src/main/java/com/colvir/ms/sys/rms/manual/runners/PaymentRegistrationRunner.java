package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.PaymentRegistrationResultDto;
import com.colvir.ms.sys.rms.dto.PaymentRegistrationDto;
import com.colvir.ms.sys.rms.dto.PaymentRegistrationJournalDto;
import com.colvir.ms.sys.rms.manual.handler.PaymentRegistrationHandler;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_PAYMENT_REGISTRATION)
@ApplicationScoped
public class PaymentRegistrationRunner implements StepRunner<PaymentRegistrationDto, PaymentRegistrationJournalDto, PaymentRegistrationResultDto> {

    @Inject
    PaymentRegistrationHandler handler;

    @Override
    public ProcessStageResponse<PaymentRegistrationJournalDto, PaymentRegistrationResultDto> process(RequestItem.Request<PaymentRegistrationDto, PaymentRegistrationJournalDto> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<PaymentRegistrationJournalDto> compensate(RequestItem.Request<PaymentRegistrationDto, PaymentRegistrationJournalDto> request) {
        return handler.undoHandle(request);
    }

}
