package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.RefundingPayment;
import com.colvir.ms.sys.rms.manual.dao.RefundingPaymentDao;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RefundingPaymentDaoImpl implements RefundingPaymentDao {

    private static final String REFUNDING_PAYMENT_NOT_FOUND = "RefundingPayment with id=%s is not found";

    @Override
    public RefundingPayment findById(Long id) {
        return RefundingPayment.findById(id);
    }

    @Override
    public RefundingPayment findByIdOrThrow(Long id) {
        RefundingPayment refundingPayment = findById(id);
        if (refundingPayment == null || Boolean.TRUE.equals(refundingPayment.isDeleted)) {
            throw new RuntimeException(String.format(REFUNDING_PAYMENT_NOT_FOUND, id));
        }
        return refundingPayment;
    }
}
