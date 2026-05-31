package com.colvir.ms.sys.rms.manual.reactive.service;

import com.colvir.ms.sys.rms.dto.BuildRequirementsDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface RequirementReactiveService {

    Uni<Void> checkBuildRequirements(BuildRequirementsDto request);

    Uni<List<RequirementStateInfoDto>> createRequirements(BuildRequirementsDto request, List<String> initialBbpStates);

    Uni<Void> deleteRequirements(List<Long> requirementIdList);
}
