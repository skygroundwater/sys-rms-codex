package com.colvir.ms.sys.rms.dto;

public class RefundResponse {

    public RefundResultDto refundResult;

    public RefundJournalDto refundJournal;

    @Override
    public String toString() {
        return "RefundResponse{" +
            "refundResult=" + refundResult +
            ", refundJournal=" + refundJournal +
            '}';
    }
}
