package com.colvir.ms.sys.rms.manual.dao;

import com.colvir.ms.sys.rms.generated.domain.RequirementRefundingPayment;

import java.util.List;

public interface RequirementRefundingPaymentDao {

    List<RequirementRefundingPayment> findActiveByRequirementIdOrderByValueDateDesc(Long requirementId);
}
