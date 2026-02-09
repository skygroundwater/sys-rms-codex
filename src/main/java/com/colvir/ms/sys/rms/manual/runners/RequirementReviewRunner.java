package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.RequirementReviewDto;
import com.colvir.ms.sys.rms.dto.RequirementReviewJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementReviewResultDto;
import com.colvir.ms.sys.rms.manual.handler.RequirementReviewHandler;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_REVIEW)
@ApplicationScoped
public class RequirementReviewRunner implements StepRunner<RequirementReviewDto, RequirementReviewJournalDto, RequirementReviewResultDto> {

    @Inject
    RequirementReviewHandler handler;

    @Override
    public ProcessStageResponse<RequirementReviewJournalDto, RequirementReviewResultDto> process(RequestItem.Request<RequirementReviewDto, RequirementReviewJournalDto> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<RequirementReviewJournalDto> compensate(RequestItem.Request<RequirementReviewDto, RequirementReviewJournalDto> request) {
        return handler.undoHandle(request);
    }

}
