package com.colvir.ms.sys.rms.dto;

import com.colvir.ms.sys.opr.api.step.runner.method.response.Substep;

import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактный класс объектов агрегации данных работы с данными в раннерах
 * @param <P> properties
 * @param <J> journal
 * @param <R> result
 */
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

    public P getProperties() {
        return this.properties;
    }

    public J getJournal() {
        return this.journal;
    }

    public R getResult() {
        return this.result;
    }

    public void setProperties(P properties) {
        this.properties = properties;
    }

    public void setJournal(J journal) {
        this.journal = journal;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public void setSubSteps(List<Substep> subSteps) {
        this.subSteps = subSteps;
    }

    public List<Substep> getSubSteps() {
        return this.subSteps;
    }

    @Override
    public String toString() {
        return "AggregationResult{" +
            "properties=" + properties +
            ", journal=" + journal +
            ", result=" + result +
            ", subSteps=" + subSteps +
            '}';
    }
}
