package com.colvir.ms.sys.rms.manual.runners;


import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.BuildRequirementsDto;
import com.colvir.ms.sys.rms.dto.BuildRequirementsJournalDto;
import com.colvir.ms.sys.rms.dto.BuildRequirementsResultDto;
import com.colvir.ms.sys.rms.manual.handler.BuildRequirementsHandler;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_BUILD_REQUIREMENTS)
@ApplicationScoped
public class BuildRequirementsRunner implements StepRunner<BuildRequirementsDto, BuildRequirementsJournalDto, BuildRequirementsResultDto> {

    @Inject
    BuildRequirementsHandler buildRequirementsHandler;

    @Override
    public ProcessStageResponse<BuildRequirementsJournalDto, BuildRequirementsResultDto> process(RequestItem.Request<BuildRequirementsDto, BuildRequirementsJournalDto> request) {
        return buildRequirementsHandler.handle(request);
    }

    @Override
    public CompensateStageResponse<BuildRequirementsJournalDto> compensate(RequestItem.Request<BuildRequirementsDto, BuildRequirementsJournalDto> request) {
        return buildRequirementsHandler.undoHandle(request);
    }
}
