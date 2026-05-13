package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.UpdateSingleRequirementDto;
import com.colvir.ms.sys.rms.dto.UpdateSingleRequirementJournalDto;
import com.colvir.ms.sys.rms.dto.UpdateSingleRequirementResultDto;
import com.colvir.ms.sys.rms.manual.handler.UpdateSingleRequirementHandler;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_UPDATE_SINGLE_REQUIREMENT)
@ApplicationScoped
public class UpdateSingleRequirementRunner implements StepRunner<UpdateSingleRequirementDto, UpdateSingleRequirementJournalDto, UpdateSingleRequirementResultDto> {

    @Inject
    UpdateSingleRequirementHandler handler;

    @Override
    public ProcessStageResponse<UpdateSingleRequirementJournalDto, UpdateSingleRequirementResultDto> process(RequestItem.Request<UpdateSingleRequirementDto, UpdateSingleRequirementJournalDto> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<UpdateSingleRequirementJournalDto> compensate(RequestItem.Request<UpdateSingleRequirementDto, UpdateSingleRequirementJournalDto> request) {
        return handler.undoHandle(request);
    }

}
