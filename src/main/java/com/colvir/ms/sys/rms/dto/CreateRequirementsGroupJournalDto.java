package com.colvir.ms.sys.rms.dto;

public class CreateRequirementsGroupJournalDto extends JournalDto {

    public Long createdGroupId;

    @Override
    public String toString() {
        return "CreateRequirementsGroupJournalDto{" +
            "createdGroupId=" + createdGroupId +
            '}';
    }
}
