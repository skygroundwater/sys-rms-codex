package com.colvir.ms.sys.rms.dto;

public class StartBaseProcessDto {
    private String id;

    public String getId() {
        return id;
    }

    public StartBaseProcessDto setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "StartBaseProcessDto{" +
            "id='" + id + '\'' +
            '}';
    }
}
