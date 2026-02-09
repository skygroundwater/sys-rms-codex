package com.colvir.ms.sys.rms.dto;

import java.util.Objects;

public class ReferenceDto {
    public Long id;
    public String __objectType;

    public ReferenceDto(Long id, String __objectType) {
        this.id = id;
        this.__objectType = __objectType;
    }

    public ReferenceDto() {
    }

    @Override
    public String toString() {
        return __objectType + ':' + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReferenceDto that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(__objectType, that.__objectType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, __objectType);
    }
}
