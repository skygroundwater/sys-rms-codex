package com.colvir.ms.sys.rms.dto;

public class DistributePaidAmountsResponse {

    public DistributePaidAmountsResultDto distributionResult;

    public DistributePaidAmountsJournalDto distributionJournal;

    @Override
    public String toString() {
        return "DistributePaidAmountsResponse{" +
            "distributionResult=" + distributionResult +
            ", distributionJournal=" + distributionJournal +
            '}';
    }
}
