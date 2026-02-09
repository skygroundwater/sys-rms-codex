package com.colvir.ms.sys.rms.manual.service;

import com.colvir.ms.sys.rms.dto.CreateRequirementDto;
import com.colvir.ms.sys.rms.dto.RequirementIndicatorDto;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.List;

public interface RequirementRouterService {

    Long createRequirement(CreateRequirementDto createRequirementDto);
    List<Long> createRequirements(Collection<CreateRequirementDto> createRequirementDtos);
    RequirementIndicatorDto getRequirementIndicator(Long indicatorId);
    ObjectNode deleteRequirement(Long id);
    List<ObjectNode> deleteRequirements(Collection<Long> ids);

}
