package com.colvir.ms.sys.rms.dto;

public class GroupMemberDeleteJournalDto extends JournalDto {

    public Long groupId;

    public Long deletedGroupMemberId;

    @Override
    public String toString() {
        return "GroupMemberDeleteJournalDto{" +
            "groupId=" + groupId +
            ", deletedGroupMemberId=" + deletedGroupMemberId +
            '}';
    }

}


