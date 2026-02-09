package com.colvir.ms.sys.rms.dto;

public class GroupMemberDeleteDto {

    /** группа требований */
    public ReferenceDto requirementGroup;

    /** требование */
    public ReferenceDto requirement;

    @Override
    public String toString() {
        return "GroupMemberDeleteDto{" +
            "requirementGroup=" + requirementGroup +
            ", requirement=" + requirement +
            '}';
    }
}
