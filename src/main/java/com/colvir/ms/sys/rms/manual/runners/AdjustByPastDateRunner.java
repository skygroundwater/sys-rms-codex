package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.AdjustByPastDateDto;
import com.colvir.ms.sys.rms.dto.AdjustByPastDateJournalDto;
import com.colvir.ms.sys.rms.dto.AdjustByPastDateResultDto;
import com.colvir.ms.sys.rms.manual.handler.AdjustByPastDateHandler;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_ADJUST_BY_PAST_DATE)
@ApplicationScoped
public class AdjustByPastDateRunner implements StepRunner<AdjustByPastDateDto, AdjustByPastDateJournalDto, AdjustByPastDateResultDto> {

    @Inject
    AdjustByPastDateHandler adjustByPastDateHandler;

    @Override
    public ProcessStageResponse<AdjustByPastDateJournalDto, AdjustByPastDateResultDto> process(RequestItem.Request<AdjustByPastDateDto, AdjustByPastDateJournalDto> request) {
        return adjustByPastDateHandler.handle(request);
    }

    @Override
    public CompensateStageResponse<AdjustByPastDateJournalDto> compensate(RequestItem.Request<AdjustByPastDateDto, AdjustByPastDateJournalDto> request) {
        return adjustByPastDateHandler.undoHandle(request);
    }

}
