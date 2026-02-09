package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.GroupMember;
import com.colvir.ms.sys.rms.generated.domain.RequirementsGroup;
import com.colvir.ms.sys.rms.manual.dao.RequirementGroupDao;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RequirementGroupDaoImpl implements RequirementGroupDao {

    private static final String REQUIREMENT_GROUP_NOT_FOUND = "Requirement Group (%s) is not found";
    private static final String REQUIREMENT_GROUP_DELETED = "Requirement Group (%s) is marked as deleted";

    @Override
    public List<GroupMember> findActiveMembersByRequirementId(Long requirementId) {
        return GroupMember.list(
            " requirement.id = ?1 and (isDeleted is null or isDeleted = false) ",
            requirementId
        );
    }

    @Override
    public RequirementsGroup findById(Long id) {
        return RequirementsGroup.findById(id);
    }

    @Override
    public RequirementsGroup findActiveByIdOrThrow(Long id) {
        RequirementsGroup group = findById(id);
        if (group == null) {
            throw new RuntimeException(String.format(REQUIREMENT_GROUP_NOT_FOUND, id));
        }
        if (Boolean.TRUE.equals(group.isDeleted)) {
            throw new RuntimeException(String.format(REQUIREMENT_GROUP_DELETED, id));
        }
        return group;
    }
}
