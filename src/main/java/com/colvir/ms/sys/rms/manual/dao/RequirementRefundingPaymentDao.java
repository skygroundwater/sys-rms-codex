package com.colvir.ms.sys.rms.manual.dao;

import com.colvir.ms.sys.rms.generated.domain.RequirementRefundingPayment;

import java.util.List;

public interface RequirementRefundingPaymentDao {

    RequirementRefundingPayment findById(Long id);

    List<RequirementRefundingPayment> findActiveByRequirementIdOrderByValueDateDesc(Long requirementId);

    List<RequirementRefundingPayment> findActiveByRefundingPaymentIdOrderByValueDateDesc(Long refundingPaymentId);
}
