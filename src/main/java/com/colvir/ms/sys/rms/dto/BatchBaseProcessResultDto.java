package com.colvir.ms.sys.rms.dto;

import java.util.Map;

public record BatchBaseProcessResultDto(
    Map<String, BaseProcessResultDto> startBatchResult
) {
}
