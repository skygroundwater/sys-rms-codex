package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.JournalDto;
import com.colvir.ms.sys.rms.dto.NonPaidRequirementsDto;
import com.colvir.ms.sys.rms.dto.NonPaidRequirementsResultDto;
import com.colvir.ms.sys.rms.manual.handler.NonPaidRequirementsHandler;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_GET_NON_PAID)
@ApplicationScoped
public class NonPaidRequirementsRunner implements StepRunner<NonPaidRequirementsDto, JournalDto, NonPaidRequirementsResultDto> {

    @Inject
    NonPaidRequirementsHandler handler;

    @Override
    public ProcessStageResponse<JournalDto, NonPaidRequirementsResultDto> process(RequestItem.Request<NonPaidRequirementsDto, JournalDto> request) {
        return handler.handle(request);
    }

}
