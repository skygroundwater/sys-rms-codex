package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.Payment;
import com.colvir.ms.sys.rms.manual.dao.PaymentDao;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PaymentDaoImpl implements PaymentDao {

    @Override
    public Payment findById(Long id) {
        return Payment.findById(id);
    }

    @Override
    public Payment findActiveByReferenceCurrencyAmountAndWithdrawalType(String reference, Long currencyId, BigDecimal amount, Long withdrawalTypeId) {
        Map<String, Object> paymentParams = new HashMap<>();
        paymentParams.put("reference", reference);
        paymentParams.put("currencyId", currencyId);
        paymentParams.put("amount", amount);
        paymentParams.put("withdrawalTypeId", withdrawalTypeId);
        return Payment.find("select p from Payment p where" +
                " (p.reference = :reference or p.payment = :reference) " +
                " and (p.isDeleted is null or p.isDeleted = false) " +
                " and p.withdrawalTypeId = :withdrawalTypeId" +
                " and p.currencyId = :currencyId" +
                " and p.amount = :amount ", paymentParams)
            .firstResult();
    }

    @Override
    public List<Payment> findActiveByIds(List<Long> ids) {
        return Payment.list(" id in (?1) and (isDeleted is null or isDeleted = false)", ids);
    }
}
