package com.colvir.ms.sys.rms.manual.util;

import java.time.LocalDate;

public class SessionContext {

    public static LocalDate getOperationDate() {
        // TODO здесь должна быть операционная дата
        return LocalDate.now();
    }

}
