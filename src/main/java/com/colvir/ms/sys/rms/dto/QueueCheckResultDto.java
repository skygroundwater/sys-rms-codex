package com.colvir.ms.sys.rms.dto;

public class QueueCheckResultDto {

    /** есть требования с более высоким приоритетом */
    public Boolean isFound;

    @Override
    public String toString() {
        return "QueueCheckResultDto{" +
            "isFound=" + isFound +
            '}';
    }
}
