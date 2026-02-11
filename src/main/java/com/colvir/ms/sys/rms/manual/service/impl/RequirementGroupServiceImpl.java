package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.sys.rms.generated.domain.GroupMember;
import com.colvir.ms.sys.rms.generated.domain.RequirementsGroup;
import com.colvir.ms.sys.rms.manual.dao.RequirementGroupDao;
import com.colvir.ms.sys.rms.manual.service.RequirementGroupService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RequirementGroupServiceImpl implements RequirementGroupService {

    @Inject
    RequirementGroupDao requirementGroupDao;

    @Override
    public List<RequirementsGroup> getRequirementGroups (Long requirementId) {
        List<RequirementsGroup> groups = new ArrayList<>();
        List<GroupMember> groupMembers = requirementGroupDao.findActiveMembersByRequirementId(requirementId);
        if (groupMembers != null && !groupMembers.isEmpty()) {
            groups = groupMembers.stream()
                .flatMap(m -> m.requirementsGroupOfMembers.stream())
                .filter(g -> !Boolean.TRUE.equals(g.isDeleted))
                .toList();
        }
        return groups;
    }

    @Override
    public RequirementsGroup findRequirementsGroupById(Long id) {
        return requirementGroupDao.findActiveByIdOrThrow(id);
    }

}
