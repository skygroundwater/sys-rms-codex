package com.colvir.ms.sys.rms.dto;

public class GroupMemberAddJournalDto extends JournalDto {

    public Long groupId;

    public Long addedGroupMemberId;

    @Override
    public String toString() {
        return "GroupMemberAddJournalDto{" +
            "groupId=" + groupId +
            ", addedGroupMemberId=" + addedGroupMemberId +
            '}';
    }
}
