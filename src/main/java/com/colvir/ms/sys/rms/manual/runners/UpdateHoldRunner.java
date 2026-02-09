package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.UpdateHoldDto;
import com.colvir.ms.sys.rms.dto.UpdateHoldJournalDto;
import com.colvir.ms.sys.rms.dto.UpdateHoldResultDto;
import com.colvir.ms.sys.rms.manual.handler.UpdateHoldHandler;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_UPDATE_HOLD)
@ApplicationScoped
public class UpdateHoldRunner implements StepRunner<UpdateHoldDto, UpdateHoldJournalDto, UpdateHoldResultDto> {

    @Inject
    UpdateHoldHandler handler;

    @Override
    public ProcessStageResponse<UpdateHoldJournalDto, UpdateHoldResultDto> process(RequestItem.Request<UpdateHoldDto, UpdateHoldJournalDto> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<UpdateHoldJournalDto> compensate(RequestItem.Request<UpdateHoldDto, UpdateHoldJournalDto> request) {
        return handler.undoHandle(request);
    }

}
