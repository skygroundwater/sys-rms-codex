package com.colvir.ms.sys.rms.dto;

import com.colvir.ms.sys.rms.generated.domain.Requirement;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

public class DebugRedistributeRequestDto {

    public List<Pair<RequirementStateInfoDto, Requirement>> requirements;

    public AdjustByPastDateJournalDto journal;

}
