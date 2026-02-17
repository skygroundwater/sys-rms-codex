package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.RefundJournalDto;
import com.colvir.ms.sys.rms.dto.RefundOfRequirementsResultDto;
import com.colvir.ms.sys.rms.dto.RefundOfRequirementsRunnerDto;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.handler.RefundOfRequirementsHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_REFUND_REQUIREMENTS)
@ApplicationScoped
public class RefundOfRequirementsRunner implements StepRunner<RefundOfRequirementsRunnerDto, RefundJournalDto, RefundOfRequirementsResultDto> {

    @Inject
    RefundOfRequirementsHandler handler;

    @Override
    public ProcessStageResponse<RefundJournalDto, RefundOfRequirementsResultDto> process(RequestItem.Request<RefundOfRequirementsRunnerDto, RefundJournalDto> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<RefundJournalDto> compensate(RequestItem.Request<RefundOfRequirementsRunnerDto, RefundJournalDto> request) {
        return handler.undoHandle(request);
    }

}
