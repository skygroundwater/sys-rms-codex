package com.colvir.ms.sys.rms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record StartBbpRunnerProperties(
    String machineId,
    String objectPath,
    String stateField,
    ObjectNode businessObject,
    boolean objectResult
) {
}
