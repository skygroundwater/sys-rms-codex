package com.colvir.ms.sys.rms.manual.dao;

import com.colvir.ms.sys.rms.generated.domain.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentDao {

    Payment findById(Long id);

    Payment findActiveByReferenceCurrencyAmountAndWithdrawalType(String reference, Long currencyId, BigDecimal amount, Long withdrawalTypeId);

    List<Payment> findActiveByIds(List<Long> ids);
}
