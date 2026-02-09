package com.colvir.ms.sys.rms.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class WriteOffDto {

    /** требование */
    public JsonNode requirement;

    @Override
    public String toString() {
        return "WriteOffDto{" +
            "requirement=" + requirement +
            '}';
    }
}
