package com.colvir.ms.sys.rms.dto;

public class AdjustResultAggregation {

    public AdjustByPastDateJournalDto journal;

    public AdjustByPastDateResultDto result;

    public AdjustResultAggregation(AdjustByPastDateJournalDto journal, AdjustByPastDateResultDto result) {
        this.journal = journal;
        this.result = result;
    }
}
