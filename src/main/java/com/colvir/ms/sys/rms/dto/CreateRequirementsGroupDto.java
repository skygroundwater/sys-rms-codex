package com.colvir.ms.sys.rms.dto;

import com.colvir.ms.sys.rms.generated.domain.enumeration.GroupPaymentStrategy;
import com.fasterxml.jackson.databind.JsonNode;


public class CreateRequirementsGroupDto {

    /** стратегия оплаты в группе */
    public GroupPaymentStrategy groupPaymentStrategy;

    /** список участников группы */
    public JsonNode groupMembers;

    @Override
    public String toString() {
        return "CreateRequirementsGroupDto{" +
            "groupPaymentStrategy=" + groupPaymentStrategy +
            ", groupMembers=" + groupMembers +
            '}';
    }
}
