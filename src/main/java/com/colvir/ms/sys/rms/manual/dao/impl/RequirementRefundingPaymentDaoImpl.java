package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.RequirementRefundingPayment;
import com.colvir.ms.sys.rms.manual.dao.RequirementRefundingPaymentDao;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class RequirementRefundingPaymentDaoImpl implements RequirementRefundingPaymentDao {

    @Override
    public RequirementRefundingPayment findById(Long id) {
        return RequirementRefundingPayment.findById(id);
    }

    @Override
    public List<RequirementRefundingPayment> findActiveByRequirementIdOrderByValueDateDesc(Long requirementId) {
        return RequirementRefundingPayment.list(
            "select rrp from RequirementRefundingPayment rrp where rrp.requirementOfRefundingPayments.id = :requirementId " +
                "and rrp.distributionAmount > 0 and (rrp.refundingPayment.isDeleted is null or rrp.refundingPayment.isDeleted = false) " +
                "order by rrp.refundingPayment.valueDate desc",
            Map.of("requirementId", requirementId)
        );
    }
    @Override
    public List<RequirementRefundingPayment> findActiveByRefundingPaymentIdOrderByValueDateDesc(Long refundingPaymentId) {
        return RequirementRefundingPayment.list(
            "select rrp from RequirementRefundingPayment rrp where rrp.refundingPayment.id = :refundingPaymentId " +
                "and rrp.distributionAmount > 0 and (rrp.refundingPayment.isDeleted is null or rrp.refundingPayment.isDeleted = false) " +
                "order by rrp.refundingPayment.valueDate desc",
            Map.of("refundingPaymentId", refundingPaymentId)
        );
    }

}
