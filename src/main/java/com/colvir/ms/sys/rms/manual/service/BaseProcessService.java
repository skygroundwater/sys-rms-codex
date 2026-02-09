package com.colvir.ms.sys.rms.manual.service;

import com.colvir.ms.sys.rms.dto.BbpObjectProperties;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementDTO;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

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
