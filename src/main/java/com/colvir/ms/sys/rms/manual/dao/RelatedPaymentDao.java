package com.colvir.ms.sys.rms.manual.dao;

import com.colvir.ms.sys.rms.generated.domain.RelatedPayment;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface RelatedPaymentDao {

    RelatedPayment findById(Long id);

    List<RelatedPayment> findNonDeletedByPaymentId(Long paymentId);

    List<RelatedPayment> findRefundableByPaymentId(Long paymentId);

    List<RelatedPayment> findPaidByRequirementIds(Set<Long> requirementIds);

    List<RelatedPayment> findForOwMassReport(Instant startOfDay, Instant endOfDay, Long withdrawalTypeId);
}
