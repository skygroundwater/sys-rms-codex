package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.Substep;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.JournalDto;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

public abstract class AbstractStepRunnerHandler<P, J extends JournalDto, R> {

    protected String stepRunnerName;

    protected Logger log;

    public AbstractStepRunnerHandler() {}

    public AbstractStepRunnerHandler(String stepRunnerName,Logger log) {
        this.stepRunnerName = stepRunnerName;
        this.log = log;
    }

    @Transactional
    public final ProcessStageResponse<J, R> handle(StepMethod.RequestItem.Request<P, J> request) {
        ProcessStageResponse.ProcessStageResponseBuilder<J, R> builder = ProcessStageResponse.builder();
        J journal = request.getJournal();
        P properties = request.getProperties();
        log.infof("%s runner started with properties: %s journal: %s", stepRunnerName, properties, journal);
        if (journal.isFirstRun) {
            validateProperties(properties);
        }
        AggregationResult<P, J, R> aggregationResult = this.process(request);
        journal = aggregationResult.getJournal();
        R result = aggregationResult.getResult();
        List<Substep> subSteps = aggregationResult.getSubSteps();
        if (!subSteps.isEmpty()) {
            log.infof("%s runner first cycle finished with journal: %s", stepRunnerName, journal);
            return builder
                .journal(journal)
                .steps(subSteps)
                .build();
        }
        log.infof("%s runner finally finished with journal: %s and result: %s", stepRunnerName, journal, result);
        return builder
            .journal(journal)
            .result(result)
            .build();
    }

    @Transactional
    public final CompensateStageResponse<J> undoHandle(StepMethod.RequestItem.Request<P, J> request) {
        P properties = request.getProperties();
        J journal = request.getJournal();
        log.infof("%s compensate process properties: %s, journal: %s", stepRunnerName, properties, journal);
        this.undo(journal);
        return CompensateStageResponse.<J>builder()
            .journal(journal)
            .build();
    }

    public abstract AggregationResult<P, J, R> process(StepMethod.RequestItem.Request<P, J> request);

    public void undo(J journal) {}

    public void validateProperties(P properties) {}

}
