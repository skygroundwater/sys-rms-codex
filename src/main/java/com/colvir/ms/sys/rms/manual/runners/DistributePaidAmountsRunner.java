package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.DistributePaidAmountsDto;
import com.colvir.ms.sys.rms.dto.DistributePaidAmountsJournalDto;
import com.colvir.ms.sys.rms.dto.DistributePaidAmountsResultDto;
import com.colvir.ms.sys.rms.manual.handler.DistributePaidAmountsHandler;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_DISTRIBUTE_PAID_AMOUNTS)
@ApplicationScoped
public class DistributePaidAmountsRunner implements StepRunner<DistributePaidAmountsDto, DistributePaidAmountsJournalDto, DistributePaidAmountsResultDto> {

    @Inject
    DistributePaidAmountsHandler handler;

    @Override
    public ProcessStageResponse<DistributePaidAmountsJournalDto, DistributePaidAmountsResultDto> process(RequestItem.Request<DistributePaidAmountsDto, DistributePaidAmountsJournalDto> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<DistributePaidAmountsJournalDto> compensate(RequestItem.Request<DistributePaidAmountsDto, DistributePaidAmountsJournalDto> request) {
        return handler.undoHandle(request);
    }

}
