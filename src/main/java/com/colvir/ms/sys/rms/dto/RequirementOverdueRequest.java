package com.colvir.ms.sys.rms.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Request for determining overdue status")
public class RequirementOverdueRequest {

    @Schema(required = true, description = "Reference to requirement")
    public ReferenceDto requirement;

    @Schema(required = true, description = "Business date")
    public LocalDate businessDate;

    @Override
    public String toString() {
        return "RequirementOverdueRequest{" +
            "requirement=" + requirement +
            ", businessDate=" + businessDate +
            '}';
    }
}
