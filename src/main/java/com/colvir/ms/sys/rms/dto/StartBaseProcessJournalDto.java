package com.colvir.ms.sys.rms.dto;

public class StartBaseProcessJournalDto {

    private String processId;
    private Long journalId;

    public String getProcessId() {
        return processId;
    }

    public StartBaseProcessJournalDto setProcessId(String processId) {
        this.processId = processId;
        return this;
    }

    public Long getJournalId() {
        return journalId;
    }

    public StartBaseProcessJournalDto setJournalId(Long journalId) {
        this.journalId = journalId;
        return this;
    }
}
