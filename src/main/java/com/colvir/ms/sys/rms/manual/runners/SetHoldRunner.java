package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.SetHoldDto;
import com.colvir.ms.sys.rms.dto.SetHoldJournalDto;
import com.colvir.ms.sys.rms.dto.SetHoldResultDto;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.handler.SetHoldHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_SET_HOLD)
@ApplicationScoped
public class SetHoldRunner implements StepRunner<SetHoldDto, SetHoldJournalDto, SetHoldResultDto> {

    @Inject
    SetHoldHandler handler;

    @Override
    public ProcessStageResponse<SetHoldJournalDto, SetHoldResultDto> process(RequestItem.Request<SetHoldDto, SetHoldJournalDto> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<SetHoldJournalDto> compensate(RequestItem.Request<SetHoldDto, SetHoldJournalDto> request) {
        return handler.undoHandle(request);
    }

}
