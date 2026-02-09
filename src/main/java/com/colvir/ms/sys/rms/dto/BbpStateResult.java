package com.colvir.ms.sys.rms.dto;

import java.io.Serializable;

public record BbpStateResult(
    String processId,
    Long journalId,
    String stateCode,
    String stateName) implements Serializable {
}
