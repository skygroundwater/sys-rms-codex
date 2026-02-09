package com.colvir.ms.sys.rms.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Request for obtaining amounts by requirements")
public class RequirementAmountRequest {

    @Schema(required = true, description = "Reference to requirement")
    public ReferenceDto requirement;

    @Schema(description = "Reference to contract")
    public ReferenceDto contract;

    @Schema(description = "Reference to client")
    public ReferenceDto client;

    @Override
    public String toString() {
        return "RequirementAmountRequest{" +
            "requirement=" + requirement +
            ", contract=" + contract +
            ", client=" + client +
            '}';
    }
}
