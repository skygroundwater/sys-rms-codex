package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.GroupMember;
import com.colvir.ms.sys.rms.manual.dao.GroupMemberDao;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GroupMemberDaoImpl implements GroupMemberDao {

    @Override
    public GroupMember findById(Long id) {
        return GroupMember.findById(id);
    }

    @Override
    public long countActiveByRequirementId(Long requirementId) {
        return GroupMember.count(
            "requirement.id = ?1 and (isDeleted is null or isDeleted = false)",
            requirementId
        );
    }
}
