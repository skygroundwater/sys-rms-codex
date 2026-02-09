package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.GroupMember;
import com.colvir.ms.sys.rms.generated.domain.RequirementsGroup;
import com.colvir.ms.sys.rms.manual.dao.RequirementGroupDao;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RequirementGroupDaoImpl implements RequirementGroupDao {

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
    public RequirementsGroup findActiveByIdOrThrow(Long id, String notFoundMessageTemplate, String deletedMessageTemplate) {
        RequirementsGroup group = findById(id);
        if (group == null) {
            throw new RuntimeException(String.format(notFoundMessageTemplate, id));
        }
        if (Boolean.TRUE.equals(group.isDeleted)) {
            throw new RuntimeException(String.format(deletedMessageTemplate, id));
        }
        return group;
    }
}
