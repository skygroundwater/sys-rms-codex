package com.colvir.ms.sys.rms.dto;

import java.util.List;

public class BbpPackRunnerProperties {
    public List<BbpObjectProperties> objectProperties;

    @Override
    public String toString() {
        return "BbpPackRunnerProperties{" +
            "objectProperties=" + objectProperties +
            '}';
    }
}
