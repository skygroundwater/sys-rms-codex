package com.colvir.ms.sys.rms.dto;

public record SimpleBaseProcessResultDto(
    String processId,
    Long journalId,
    String stateCode,
    String stateName) {
}
