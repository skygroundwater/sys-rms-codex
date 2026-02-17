package com.colvir.ms.sys.rms.manual.controller;

import com.colvir.ms.sys.rms.dto.RequirementAmountRequest;
import com.colvir.ms.sys.rms.dto.RequirementAmountResponse;
import com.colvir.ms.sys.rms.dto.RequirementOverdueRequest;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

@Path("/api/requirement")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class RequirementController {

    @Inject
    RequirementService requirementService;

    @POST
    @Path("/get-paid-amount")
    @Operation(
        summary = "Get the amount paid for the requirement",
        description = "Returns the amount paid for the requirement, taking into account the contract and the client"
    )
    @APIResponse(
        responseCode = "200",
        description = "Paid amount",
        content = @Content(schema = @Schema(implementation = BigDecimal.class))
    )
    public BigDecimal getPaidAmount(
        @RequestBody(
            required = true,
            description = "Requirement, contract, and client parameters",
            content = @Content(
                schema = @Schema(implementation = RequirementAmountRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Example",
                        value = """
                                {
                                  "requirement": {"id": "269370859981676551"},
                                  "contract": {"id": "196210085918973952"},
                                  "client": {"id": "CLIENT42"}
                                }
                                """
                    )
                }
            )
        )
        RequirementAmountRequest request
    ) {
        return requirementService.getPaidAmount(
            request.requirement,
            request.contract,
            request.client
        );
    }

    @POST
    @Path("/get-paid-amounts")
    @Operation(
        summary = "Get amounts paid for the requirement",
        description = "Returns detailed information about paid amounts"
    )
    @APIResponse(
        responseCode = "200",
        description = "Paid amounts",
        content = @Content(schema = @Schema(implementation = RequirementAmountResponse.class))
    )
    public RequirementAmountResponse getPaidAmounts(
        @RequestBody(
            required = true,
            description = "Requirement parameters",
            content = @Content(schema = @Schema(implementation = RequirementAmountRequest.class))
        )
        RequirementAmountRequest request
    ) {
        return requirementService.getPaidAmounts(
            request.requirement,
            request.contract,
            request.client
        );
    }

    @POST
    @Path("/get-unpaid-amount")
    @Operation(
        summary = "Get the unpaid amount for the requirement",
        description = "Returns the unpaid amount for the requirement"
    )
    @APIResponse(
        responseCode = "200",
        description = "Unpaid amount",
        content = @Content(schema = @Schema(implementation = BigDecimal.class))
    )
    public BigDecimal getUnpaidAmount(
        @RequestBody(
            required = true,
            description = "Requirement parameters",
            content = @Content(schema = @Schema(implementation = RequirementAmountRequest.class))
        )
        RequirementAmountRequest request
    ) {
        return requirementService.getUnpaidAmount(
            request.requirement,
            request.contract,
            request.client
        );
    }

    @POST
    @Path("/get-unpaid-amounts")
    @Operation(summary = "Get unpaid amounts for the requirement")
    @APIResponse(
        responseCode = "200",
        description = "Detailed information about unpaid amounts",
        content = @Content(schema = @Schema(implementation = RequirementAmountResponse.class))
    )
    public RequirementAmountResponse getUnpaidAmounts(
        @RequestBody(
            required = true,
            description = "Requirement parameters",
            content = @Content(schema = @Schema(implementation = RequirementAmountRequest.class))
        )
        RequirementAmountRequest request
    ) {
        return requirementService.getUnpaidAmounts(
            request.requirement,
            request.contract,
            request.client
        );
    }

    @POST
    @Path("/get-is-overdue")
    @Operation(summary = "Check if the requirement is overdue")
    @APIResponse(
        responseCode = "200",
        description = "true — if the requirement is overdue",
        content = @Content(schema = @Schema(implementation = Boolean.class))
    )
    public Boolean getIsOverdue(
        @RequestBody(
            required = true,
            description = "Requirement parameters and business date",
            content = @Content(schema = @Schema(implementation = RequirementOverdueRequest.class))
        )
        RequirementOverdueRequest request
    ) {
        return requirementService.getIsOverdue(
            request.requirement,
            request.businessDate
        );
    }

    @POST
    @Path("/overdue-start-date")
    @Operation(summary = "Get the overdue start date")
    @APIResponse(
        responseCode = "200",
        description = "Overdue start date",
        content = @Content(schema = @Schema(implementation = LocalDate.class))
    )
    public LocalDate getOverdueStartDate(
        @RequestBody(
            required = true,
            description = "Requirement parameters and business date",
            content = @Content(schema = @Schema(implementation = RequirementOverdueRequest.class))
        )
        RequirementOverdueRequest request
    ) {
        return requirementService.getOverdueStartDate(
            request.requirement,
            request.businessDate
        );
    }

    @POST
    @Path("/overdue-end-date")
    @Operation(summary = "Get the overdue end date")
    @APIResponse(
        responseCode = "200",
        description = "Overdue end date",
        content = @Content(schema = @Schema(implementation = LocalDate.class))
    )
    public LocalDate getOverdueEndDate(
        @RequestBody(
            required = true,
            description = "Requirement parameters and business date",
            content = @Content(schema = @Schema(implementation = RequirementOverdueRequest.class))
        )
        RequirementOverdueRequest request
    ) {
        return requirementService.getOverdueEndDate(
            request.requirement,
            request.businessDate
        );
    }

}
