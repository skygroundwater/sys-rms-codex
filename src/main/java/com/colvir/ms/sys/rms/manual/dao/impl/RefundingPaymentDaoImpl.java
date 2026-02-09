package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.RefundingPayment;
import com.colvir.ms.sys.rms.manual.dao.RefundingPaymentDao;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RefundingPaymentDaoImpl implements RefundingPaymentDao {

    @Override
    public RefundingPayment findById(Long id) {
        return RefundingPayment.findById(id);
    }
}
