package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.RelatedPayment;
import com.colvir.ms.sys.rms.manual.dao.RelatedPaymentDao;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class RelatedPaymentDaoImpl implements RelatedPaymentDao {

    @Override
    public List<RelatedPayment> findNonDeletedByPaymentId(Long paymentId) {
        return RelatedPayment.list("select r from RelatedPayment r where " +
                " r.payment.id = :paymentId " +
                " and (r.isDeleted is null or r.isDeleted = false) ",
            Map.of("paymentId", paymentId));
    }

    @Override
    public List<RelatedPayment> findRefundableByPaymentId(Long paymentId) {
        return RelatedPayment.list("select r from RelatedPayment r where " +
                " r.payment.id = :id " +
                " and (r.amount > 0 or r.amountOfPayment > 0) " +
                " and (r.isDeleted is null or r.isDeleted = false) " +
                " order by r.payment.createTime desc ",
            Map.of("id", paymentId));
    }

    @Override
    public List<RelatedPayment> findPaidByRequirementIds(Set<Long> requirementIds) {
        return RelatedPayment.list("select r from RelatedPayment r where " +
                " r.requirementOfRelatedPayments.id in (:ids) " +
                " and r.payment.paymentResult = 'PAID' " +
                " and (r.isDeleted is null or r.isDeleted = false) " +
                " order by r.payment.createTime desc ",
            Map.of("ids", requirementIds));
    }

    @Override
    public List<RelatedPayment> findForOwMassReport(Instant startOfDay, Instant endOfDay, Long withdrawalTypeId) {
        return RelatedPayment.list("select rp from RelatedPayment rp where" +
                " rp.requirementOfRelatedPayments is not null and" +
                " rp.requirementOfRelatedPayments.isDeleted = false and" +
                " rp.requirementOfRelatedPayments.baseDocument like '%/BNK/LN/LoanContract:%' and" +
                " (rp.payment.createTime between :startOfDay and :endOfDay)" +
                " and rp.payment.withdrawalTypeId = :withdrawalTypeId",
            Map.of(
                "startOfDay", startOfDay,
                "endOfDay", endOfDay,
                "withdrawalTypeId", withdrawalTypeId
            ));
    }
}
