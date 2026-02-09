package com.colvir.ms.sys.rms.manual.service;

import com.colvir.ms.sys.rms.dto.AdjustByPastDateJournalDto;
import com.colvir.ms.sys.rms.dto.AdjustByPastDateResultDto;
import com.colvir.ms.sys.rms.dto.AdjustRefundPaymentResultDto;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RefundJournalDto;
import com.colvir.ms.sys.rms.dto.RefundOfPaymentDto;
import com.colvir.ms.sys.rms.dto.RefundOfRequirementsDto;
import com.colvir.ms.sys.rms.dto.RefundResponse;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentDto;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentResponse;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

public interface RequirementPaymentService {

    RegistrationOfPaymentResponse registrationOfPayment (RegistrationOfPaymentDto request);

    RefundResponse refundOfPayment (RefundOfRequirementsDto request);

    RefundResponse refundOfPayment (RefundOfPaymentDto request);

    void refundOfPaymentUndo (RefundJournalDto request);

    void processRefundingPayment(List<AdjustRefundPaymentResultDto> outgoingPayments,
                                 List<Pair<RequirementStateInfoDto, ReferenceDto>> requirementMap,
                                 AdjustByPastDateJournalDto journal, AdjustByPastDateResultDto result);
}
