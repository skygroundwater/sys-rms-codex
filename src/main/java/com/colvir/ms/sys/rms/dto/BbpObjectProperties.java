package com.colvir.ms.sys.rms.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class BbpObjectProperties {
    public String processId;
    public String event;
    public ObjectNode machineContext;

    @Override
    public String toString() {
        return "BbpObjectProperties{" +
            "processId='" + processId + '\'' +
            ", event='" + event + '\'' +
            ", machineContext=" + machineContext +
            '}';
    }
}

