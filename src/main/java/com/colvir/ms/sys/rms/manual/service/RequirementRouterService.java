package com.colvir.ms.sys.rms.manual.service;

import com.colvir.ms.sys.rms.dto.CreateRequirementDto;
import com.colvir.ms.sys.rms.dto.RelatedPaymentsJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementIndicatorDto;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.generated.domain.Payment;
import com.colvir.ms.sys.rms.generated.domain.RefundingPayment;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RequirementRouterService {

    Long createRequirement(CreateRequirementDto createRequirementDto);

    List<Long> createRequirements(Collection<CreateRequirementDto> createRequirementDtos);

    RequirementIndicatorDto getRequirementIndicator(Long indicatorId);

    ObjectNode deleteRequirement(Long id);

    List<ObjectNode> deleteRequirements(Collection<Long> ids);

    void restoreRequirementWithoutBbpCancel(Requirement requirement,
                                            RequirementJournalDto journal,
                                            List<Long> createdRelatedPayments,
                                            List<Long> createdReqRefundingPayments);

    void restoreRequirementForRefundUndo(Requirement requirement,
                                         RequirementJournalDto journal,
                                         Map<Long, RelatedPaymentsJournalDto> relatedToUpdate);

    void deletePayment(Payment payment);

    void deleteRefundingPayment(RefundingPayment refundingPayment);

    void deletePaymentRefundLink(RefundingPayment paymentRefund, Long refundId);
}
