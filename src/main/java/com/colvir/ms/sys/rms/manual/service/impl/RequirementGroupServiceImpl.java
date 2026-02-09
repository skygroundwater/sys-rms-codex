package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.sys.rms.generated.domain.GroupMember;
import com.colvir.ms.sys.rms.generated.domain.RequirementsGroup;
import com.colvir.ms.sys.rms.manual.service.RequirementGroupService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RequirementGroupServiceImpl implements RequirementGroupService {

    @Override
    public List<RequirementsGroup> getRequirementGroups (Long requirementId) {
        List<RequirementsGroup> groups = new ArrayList<>();
        List<GroupMember> groupMembers = GroupMember.list(
            " requirement.id = ?1" +
                  " and (isDeleted is null or isDeleted = false) ",
                requirementId
        );
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
        RequirementsGroup group = RequirementsGroup.findById(id);
        if (group == null) {
            throw new RuntimeException(String.format("Requirement Group (%s) is not found", id));
        }
        if (Boolean.TRUE.equals(group.isDeleted)) {
            throw new RuntimeException(String.format("Requirement Group (%s) is marked as deleted", id));
        }
        return group;
    }

}
