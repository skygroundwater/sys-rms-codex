package com.colvir.ms.sys.rms.dto;

public class JournalDto {

    /**
     * контрольное поле повторности исполнения шага
     */
    public boolean isFirstRun = true;

    @Override
    public String toString() {
        return "JournalDto{" +
            "isFirstRun=" + isFirstRun +
            '}';
    }
}
