package com.colvir.ms.sys.rms.manual.service;

import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;

public interface BaseProcessService {

    String startProcess(String machineId, Object mainObject);

    String getProcessId(String bbpStateText);

    Long getJournalId(String bbpStateText);

    String getMachineId(String bbpStateText);

    String getProcessState(String bbpStateText);

    String sendEvent(String bbpEvent, String bbpProcessId, Object mainObject);

    String cancelExecution (String bbpProcessId, Long bbpJournalId);

    String cancelExecution(String bbpProcessId, Long bbpJournalId, boolean exclusive);

    String getBbpUpdateEvent(RequirementStateInfoDto newBbpState, RequirementJournalDto prevBbpState);
}
