package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.JournalDto;
import com.colvir.ms.sys.rms.dto.UpdateRequirementsDto;
import com.colvir.ms.sys.rms.dto.UpdateRequirementsResultDto;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.handler.UpdateRequirementsHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_UPDATE_REQUIREMENTS)
@ApplicationScoped
public class UpdateRequirementsRunner implements StepRunner<UpdateRequirementsDto, JournalDto, UpdateRequirementsResultDto> {

    @Inject
    UpdateRequirementsHandler handler;

    @Override
    public ProcessStageResponse<JournalDto, UpdateRequirementsResultDto> process(RequestItem.Request<UpdateRequirementsDto, JournalDto> request) {
        return handler.handle(request);
    }

}
