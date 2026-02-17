package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.WriteOffDto;
import com.colvir.ms.sys.rms.dto.WriteOffJournalDto;
import com.colvir.ms.sys.rms.dto.WriteOffResultDto;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.handler.WriteOffHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_WRITE_OFF)
@ApplicationScoped
public class WriteOffRunner implements StepRunner<WriteOffDto, WriteOffJournalDto, WriteOffResultDto> {

    @Inject
    WriteOffHandler handler;

    @Override
    public ProcessStageResponse<WriteOffJournalDto, WriteOffResultDto> process(RequestItem.Request<WriteOffDto, WriteOffJournalDto> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<WriteOffJournalDto> compensate(RequestItem.Request<WriteOffDto, WriteOffJournalDto> request) {
        return handler.undoHandle(request);
    }

}
