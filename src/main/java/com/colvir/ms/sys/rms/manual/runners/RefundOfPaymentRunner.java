package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.RefundJournalDto;
import com.colvir.ms.sys.rms.dto.RefundOfPaymentResultDto;
import com.colvir.ms.sys.rms.dto.RefundOfPaymentRunnerDto;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.handler.RefundOfPaymentHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_REFUND_PAYMENT)
@ApplicationScoped
public class RefundOfPaymentRunner implements StepRunner<RefundOfPaymentRunnerDto, RefundJournalDto, RefundOfPaymentResultDto> {

    @Inject
    RefundOfPaymentHandler handler;

    @Override
    public ProcessStageResponse<RefundJournalDto, RefundOfPaymentResultDto> process(RequestItem.Request<RefundOfPaymentRunnerDto, RefundJournalDto> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<RefundJournalDto> compensate(RequestItem.Request<RefundOfPaymentRunnerDto, RefundJournalDto> request) {
        return handler.undoHandle(request);
    }

}
