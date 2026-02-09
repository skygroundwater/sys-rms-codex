package com.colvir.ms.sys.rms.manual.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseProcessResultDto {

    public String processId;
    public Long journalId;
    public String stateCode;
    public String stateName;
    public Boolean deleted;

    @Deprecated //old format
    public String machineId;
    @Deprecated //old format
    public String state;
    @Deprecated //old format
    public List<String> states;

    @Override
    public String toString() {
        return "BaseProcessResultDto{" +
            "processId='" + processId + '\'' +
            ", machineId='" + machineId + '\'' +
            ", journalId=" + journalId +
            ", state='" + state + '\'' +
            ", stateCode='" + stateCode + '\'' +
            ", stateName='" + stateName + '\'' +
            ", states=" + states +
            ", deleted=" + deleted +
            '}';
    }
}
