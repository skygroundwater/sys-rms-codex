package com.colvir.ms.sys.rms.manual.dao;

import com.colvir.ms.sys.rms.generated.domain.GroupMember;

public interface GroupMemberDao {

    GroupMember findById(Long id);

    long countActiveByRequirementId(Long requirementId);
}
