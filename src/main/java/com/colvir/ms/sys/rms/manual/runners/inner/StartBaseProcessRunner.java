package com.colvir.ms.sys.rms.manual.runners.inner;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.StartBaseProcessDto;
import com.colvir.ms.sys.rms.dto.StartBaseProcessJournalDto;
import com.colvir.ms.sys.rms.dto.StartBaseProcessResultDto;
import com.colvir.ms.sys.rms.manual.service.BaseProcessService;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.Optional;

@Step("rms-start-base-business-process")
@ApplicationScoped
public class StartBaseProcessRunner implements StepRunner<StartBaseProcessDto, StartBaseProcessJournalDto, StartBaseProcessResultDto> {
    @ConfigProperty(name = "requirements-machine-id", defaultValue = "")
    String requirementsMachineId;

    private final BaseProcessService baseProcessService;
    private final Logger log;

    public StartBaseProcessRunner(BaseProcessService baseProcessService, Logger log) {
        this.baseProcessService = baseProcessService;
        this.log = log;
    }


    @Override
    public ProcessStageResponse<StartBaseProcessJournalDto, StartBaseProcessResultDto> process(RequestItem.Request<StartBaseProcessDto, StartBaseProcessJournalDto> request) {
        log.infof("rms-start-base-business-process:\n%s", request.getProperties());
        String initialBbpState = baseProcessService.startProcess(requirementsMachineId, null);

        StartBaseProcessResultDto result = new StartBaseProcessResultDto()
            .setInitialBbpState(initialBbpState);
        StartBaseProcessJournalDto journal = new StartBaseProcessJournalDto()
            .setProcessId(baseProcessService.getProcessId(initialBbpState))
            .setJournalId(baseProcessService.getJournalId(initialBbpState));

        return ProcessStageResponse.<StartBaseProcessJournalDto, StartBaseProcessResultDto>builder()
            .journal(journal)
            .result(result)
            .build();
    }

    @Override
    public CompensateStageResponse<StartBaseProcessJournalDto> compensate(RequestItem.Request<StartBaseProcessDto, StartBaseProcessJournalDto> request) {
        log.infof("rms-start-base-business-process compensate:\n%s", request.getJournal());
        Optional.ofNullable(request.getJournal())
            .filter(j -> j.getJournalId() != null && j.getProcessId() != null)
            .ifPresent(j -> baseProcessService.cancelExecution(j.getProcessId(), j.getJournalId(), false));
        return CompensateStageResponse.<StartBaseProcessJournalDto>builder()
            .journal(request.getJournal())
            .build();
    }
}
