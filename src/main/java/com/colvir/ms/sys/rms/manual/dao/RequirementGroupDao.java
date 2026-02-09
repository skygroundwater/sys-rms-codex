package com.colvir.ms.sys.rms.manual.dao;

import com.colvir.ms.sys.rms.generated.domain.GroupMember;
import com.colvir.ms.sys.rms.generated.domain.RequirementsGroup;

import java.util.List;

public interface RequirementGroupDao {

    List<GroupMember> findActiveMembersByRequirementId(Long requirementId);

    RequirementsGroup findById(Long id);

    RequirementsGroup findActiveByIdOrThrow(Long id);
}
