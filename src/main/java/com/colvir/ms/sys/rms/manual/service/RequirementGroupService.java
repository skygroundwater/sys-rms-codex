package com.colvir.ms.sys.rms.manual.service;

import com.colvir.ms.sys.rms.generated.domain.RequirementsGroup;

import java.util.List;

public interface RequirementGroupService {

    List<RequirementsGroup> getRequirementGroups (Long requirementId);

    RequirementsGroup findRequirementsGroupById(Long id);

}
