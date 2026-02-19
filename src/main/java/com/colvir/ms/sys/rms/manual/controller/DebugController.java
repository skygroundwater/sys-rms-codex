package com.colvir.ms.sys.rms.manual.controller;

import com.colvir.ms.sys.rms.dto.AdjustByPastDateResultDto;
import com.colvir.ms.sys.rms.dto.BuildRequirementsDto;
import com.colvir.ms.sys.rms.dto.CheckQueueDto;
import com.colvir.ms.sys.rms.dto.DebugRedistributeRequestDto;
import com.colvir.ms.sys.rms.dto.PaymentOwMassRequestDto;
import com.colvir.ms.sys.rms.dto.PaymentOwMassResultDto;
import com.colvir.ms.sys.rms.dto.RefundJournalDto;
import com.colvir.ms.sys.rms.dto.RefundOfPaymentDto;
import com.colvir.ms.sys.rms.dto.RefundOfRequirementsDto;
import com.colvir.ms.sys.rms.dto.RefundResponse;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentDto;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentResponse;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementJournalDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementResponse;
import com.colvir.ms.sys.rms.manual.handler.QueueCheckHandler;
import com.colvir.ms.sys.rms.manual.service.PaymentOwMassReportService;
import com.colvir.ms.sys.rms.manual.service.RequirementPaymentService;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Arrays;
import java.util.List;

@Path("/debug")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DebugController {

    @Inject
    RequirementService requirementService;

    @Inject
    RequirementPaymentService paymentService;

    @Inject
    QueueCheckHandler queueCheckHandler;

    @Inject
    PaymentOwMassReportService paymentOwMassReportService;


    @POST
    @Path("/create-requirements")
    public List<RequirementStateInfoDto> debugCreateRequirements(BuildRequirementsDto request){
        return requirementService.createRequirements(request);
    }

    @POST
    @Path("/delete-requirements")
    @Consumes(MediaType.TEXT_PLAIN)
    public void debugDeleteRequirements(String request){
        List<Long> ids = Arrays.stream(request.split(","))
            .map(Long::valueOf)
            .toList();
        requirementService.deleteRequirements(ids);
    }

    @POST
    @Path("undo-update")
    public void debugUpdateRequirementsUndo (List<RequirementJournalDto> journal) {
        requirementService.updateRequirementsUndo(journal);
    }

    @POST
    @Path("registration-of-payment")
    public RegistrationOfPaymentResponse debugRegistrationOfPayment (RegistrationOfPaymentDto request) {
        return paymentService.registrationOfPayment(request);
    }

    @POST
    @Path("requirement-refund")
    public RefundResponse refundOfPayment (RefundOfRequirementsDto request) {
        return paymentService.refundOfPayment(request);
    }

    @POST
    @Path("payment-refund")
    public RefundResponse refundOfPayment (RefundOfPaymentDto request) {
        return paymentService.refundOfPayment(request);
    }

    @POST
    @Path("refund-undo")
    public void refundOfPaymentUndo (RefundJournalDto request) {
        paymentService.refundOfPaymentUndo(request);
    }

    @POST
    @Path("requirement-review")
    public ReviewRequirementResponse requirementReview (ReviewRequirementDto request) {
        return requirementService.requirementReview(request);
    }

    @POST
    @Path("review-undo")
    public void requirementReviewUndo (ReviewRequirementJournalDto request) {
        requirementService.requirementReviewUndo(request);
    }

    @POST
    @Path("check-queue")
    public Boolean checkQueue(CheckQueueDto request) {
        return queueCheckHandler.checkRequirementQueue(request);
    }

    @POST
    @Path("payment-ow-mass")
    public List<PaymentOwMassResultDto> paymentOwMassDto(PaymentOwMassRequestDto request) {
        return paymentOwMassReportService.getPaymentOwMassData(request.date);
    }

    @POST
    @Path("adjust-by-past-date")
    public AdjustByPastDateResultDto adjustByPastDate(DebugRedistributeRequestDto request) {
        paymentService.redistributeExistingRequirementPayments(
            request.requirements,
            request.journal,
            request.result
        );
        return request.result;
    }
}
