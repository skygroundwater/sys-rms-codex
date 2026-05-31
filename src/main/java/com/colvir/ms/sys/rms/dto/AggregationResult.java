package com.colvir.ms.sys.rms.dto;

import com.colvir.ms.sys.opr.api.step.runner.method.response.Substep;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактный класс объектов агрегации данных работы с данными в раннерах
 * @param <P> properties
 * @param <J> journal
 * @param <R> result
 */
@Getter
@Setter
@ToString
public class AggregationResult<P, J, R> {

    protected P properties;
    protected J journal;
    protected R result;
    protected List<Substep> subSteps = new ArrayList<>();

    public AggregationResult(P properties, J journal, R result) {
        this.properties = properties;
        this.journal = journal;
        this.result = result;
    }

    public AggregationResult(J journal, List<Substep> subSteps) {
        this.journal = journal;
        this.subSteps = subSteps;
    }

    public AggregationResult(J journal, R result, List<Substep> subSteps) {
        this.journal = journal;
        this.subSteps = subSteps;
        this.result = result;
    }
}
