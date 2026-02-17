package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.JournalDto;
import com.colvir.ms.sys.rms.dto.QueueCheckDto;
import com.colvir.ms.sys.rms.dto.QueueCheckResultDto;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.handler.QueueCheckHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_QUEUE_CHECK)
@ApplicationScoped
public class QueueCheckRunner implements StepRunner<QueueCheckDto, JournalDto, QueueCheckResultDto> {

    @Inject
    QueueCheckHandler handler;

    @Override
    public ProcessStageResponse<JournalDto, QueueCheckResultDto> process(RequestItem.Request<QueueCheckDto, JournalDto> request) {
        return handler.handle(request);
    }

}
