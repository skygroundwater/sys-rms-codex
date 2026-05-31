package com.colvir.ms.sys.rms.manual.reactive.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.Substep;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.JournalDto;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.mutiny.Uni;
import java.util.List;
import org.jboss.logging.Logger;

public abstract class AbstractReactiveStepRunnerHandler<P, J extends JournalDto, R> {

    protected String stepRunnerName;

    protected Logger log;

    public AbstractReactiveStepRunnerHandler() {}

    public AbstractReactiveStepRunnerHandler(String stepRunnerName, Logger log) {
        this.stepRunnerName = stepRunnerName;
        this.log = log;
    }

    public final ProcessStageResponse<J, R> handle(StepMethod.RequestItem.Request<P, J> request) {
        return handleReactive(request).await().indefinitely();
    }

    public final CompensateStageResponse<J> compensate(StepMethod.RequestItem.Request<P, J> request) {
        return compensateReactive(request).await().indefinitely();
    }

    public final CompensateStageResponse<J> undoHandle(StepMethod.RequestItem.Request<P, J> request) {
        return compensate(request);
    }

    public final Uni<ProcessStageResponse<J, R>> handleReactive(StepMethod.RequestItem.Request<P, J> request) {
        return Uni.createFrom()
            .deferred(() -> QuarkusTransaction.joiningExisting()
                .call(() -> handleInTransaction(request)));
    }

    public final Uni<CompensateStageResponse<J>> compensateReactive(StepMethod.RequestItem.Request<P, J> request) {
        return Uni.createFrom()
            .deferred(() -> QuarkusTransaction.joiningExisting()
                .call(() -> compensateInTransaction(request)));
    }

    public final Uni<CompensateStageResponse<J>> undoHandleReactive(StepMethod.RequestItem.Request<P, J> request) {
        return compensateReactive(request);
    }

    private Uni<ProcessStageResponse<J, R>> handleInTransaction(StepMethod.RequestItem.Request<P, J> request) {
        J journal = request.getJournal();
        P properties = request.getProperties();
        log.infof("%s runner started with properties: %s journal: %s", stepRunnerName, properties, journal);
        Uni<Void> validation = journal.isFirstRun
            ? validateProperties(properties)
            : Uni.createFrom().voidItem();

        return validation
            .chain(() -> process(request))
            .chain(this::toProcessStageResponse);
    }

    private Uni<ProcessStageResponse<J, R>> toProcessStageResponse(AggregationResult<P, J, R> aggregationResult) {
        ProcessStageResponse.ProcessStageResponseBuilder<J, R> builder = ProcessStageResponse.builder();
        J journal = aggregationResult.getJournal();
        R result = aggregationResult.getResult();
        List<Substep> subSteps = aggregationResult.getSubSteps();
        if (!subSteps.isEmpty()) {
            log.infof("%s runner first cycle finished with journal: %s", stepRunnerName, journal);
            return Uni.createFrom().item(builder
                .journal(journal)
                .steps(subSteps)
                .build());
        }
        log.infof("%s runner finally finished with journal: %s and result: %s", stepRunnerName, journal, result);
        return Uni.createFrom().item(builder
            .journal(journal)
            .result(result)
            .build());
    }

    private Uni<CompensateStageResponse<J>> compensateInTransaction(StepMethod.RequestItem.Request<P, J> request) {
        P properties = request.getProperties();
        J journal = request.getJournal();
        log.infof("%s compensate process properties: %s, journal: %s", stepRunnerName, properties, journal);
        return undo(journal)
            .replaceWith(CompensateStageResponse.<J>builder()
                .journal(journal)
                .build());
    }

    public abstract Uni<AggregationResult<P, J, R>> process(StepMethod.RequestItem.Request<P, J> request);

    public Uni<Void> undo(J journal) {
        return Uni.createFrom().voidItem();
    }

    public Uni<Void> validateProperties(P properties) {
        return Uni.createFrom().voidItem();
    }
}
