package com.colvir.ms.sys.rms.dto;

public class StartBaseProcessResultDto {
    private String initialBbpState;

    public String getInitialBbpState() {
        return initialBbpState;
    }

    public StartBaseProcessResultDto setInitialBbpState(String initialBbpState) {
        this.initialBbpState = initialBbpState;
        return this;
    }

}
