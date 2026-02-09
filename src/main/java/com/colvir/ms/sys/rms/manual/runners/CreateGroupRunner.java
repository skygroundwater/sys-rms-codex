package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.CreateRequirementsGroupDto;
import com.colvir.ms.sys.rms.dto.CreateRequirementsGroupJournalDto;
import com.colvir.ms.sys.rms.dto.CreateRequirementsGroupResultDto;
import com.colvir.ms.sys.rms.manual.handler.CreateGroupHandler;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_CREATE_GROUP)
@ApplicationScoped
public class CreateGroupRunner implements StepRunner<CreateRequirementsGroupDto, CreateRequirementsGroupJournalDto, CreateRequirementsGroupResultDto> {

    @Inject
    CreateGroupHandler createGroupHandler;

    @Override
    public ProcessStageResponse<CreateRequirementsGroupJournalDto, CreateRequirementsGroupResultDto> process(RequestItem.Request<CreateRequirementsGroupDto, CreateRequirementsGroupJournalDto> request) {
        return createGroupHandler.handle(request);
    }

    @Override
    public CompensateStageResponse<CreateRequirementsGroupJournalDto> compensate(RequestItem.Request<CreateRequirementsGroupDto, CreateRequirementsGroupJournalDto> request) {
        return createGroupHandler.undoHandle(request);
    }

}
