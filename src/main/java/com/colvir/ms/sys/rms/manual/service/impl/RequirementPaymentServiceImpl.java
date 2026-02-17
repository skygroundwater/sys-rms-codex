package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.sys.rms.dto.AdjustByPastDateJournalDto;
import com.colvir.ms.sys.rms.dto.AdjustByPastDateResultDto;
import com.colvir.ms.sys.rms.dto.AdjustRefundPaymentResultDto;
import com.colvir.ms.sys.rms.dto.PaymentInfoDto;
import com.colvir.ms.sys.rms.dto.PaymentInfoExtendedDto;
import com.colvir.ms.sys.rms.dto.PaymentRefundInfoDto;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RefundJournalDto;
import com.colvir.ms.sys.rms.dto.RefundOfPaymentDto;
import com.colvir.ms.sys.rms.dto.RefundOfRequirementsDto;
import com.colvir.ms.sys.rms.dto.RefundResponse;
import com.colvir.ms.sys.rms.dto.RefundResultDto;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentDto;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentJournalDto;
import com.colvir.ms.sys.rms.dto.RegistrationOfPaymentResponse;
import com.colvir.ms.sys.rms.dto.RelatedPaymentsJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.dto.WithdrawalResultDto;
import com.colvir.ms.sys.rms.generated.domain.Payment;
import com.colvir.ms.sys.rms.generated.domain.RefundingPayment;
import com.colvir.ms.sys.rms.generated.domain.RelatedPayment;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.RequirementRefundingPayment;
import com.colvir.ms.sys.rms.generated.domain.enumeration.PaymentLinkType;
import com.colvir.ms.sys.rms.generated.domain.enumeration.PaymentResult;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.generated.service.dto.PaymentDTO;
import com.colvir.ms.sys.rms.generated.service.mapper.PaymentMapper;
import com.colvir.ms.sys.rms.generated.service.mapper.RequirementMapper;
import com.colvir.ms.sys.rms.manual.constant.RmsConstants;
import com.colvir.ms.sys.rms.manual.dao.PaymentDao;
import com.colvir.ms.sys.rms.manual.dao.RefundingPaymentDao;
import com.colvir.ms.sys.rms.manual.dao.RelatedPaymentDao;
import com.colvir.ms.sys.rms.manual.dao.RequirementDao;
import com.colvir.ms.sys.rms.manual.service.RequirementPaymentService;
import com.colvir.ms.sys.rms.manual.service.RequirementRouterService;
import com.colvir.ms.sys.rms.manual.util.RequirementMapperUtils;
import com.colvir.ms.sys.rms.manual.util.SessionContext;
import com.google.common.base.Functions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.Pair;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class RequirementPaymentServiceImpl implements RequirementPaymentService {

    @Inject
    PaymentMapper paymentMapper;

    @Inject
    RequirementMapper requirementMapper;

    @Inject
    RequirementRouterService requirementRouterService;

    @Inject
    Logger log;


    @Inject
    RequirementDao requirementDao;

    @Inject
    PaymentDao paymentDao;

    @Inject
    RefundingPaymentDao refundingPaymentDao;

    @Inject
    RelatedPaymentDao relatedPaymentDao;

    @Override
    @Transactional
    public RegistrationOfPaymentResponse registrationOfPayment(RegistrationOfPaymentDto request) {
        log.infof("registrationOfPayment:\n%s", request);

        // TODO: не реализовано снятие холдов

        RegistrationOfPaymentResponse response = new RegistrationOfPaymentResponse();
        RegistrationOfPaymentJournalDto journal = new RegistrationOfPaymentJournalDto();

        if (request.requirements == null || request.requirements.isEmpty()) {
            throw new RuntimeException("Incorrect request: requirements data is empty");
        }
        if (request.payments == null || request.payments.isEmpty()) {
            throw new RuntimeException("Incorrect request: payments data is empty");
        }

        Map<Long, Requirement> requirementMap = new HashMap<>();
        for (var requirementRef : request.requirements) {
            Requirement requirement = requirementDao.findByIdOrThrow(requirementRef.id);
            requirementMap.put(requirementRef.id, requirement);
            // сохраняем первоначальные значения для отката
            RequirementJournalDto requirementJournal = RequirementMapperUtils.fillRequirementJournal(requirement);
            journal.requirementJournal.add(requirementJournal);
        }

        log.infof("Requirements journal filled %s", journal);

        // для каждого списания из входного массива создаётся объект /SYS/RMS/Payment на сумму списания (в валюте списания)
        // у платежа заполняется всё, что можно вытащить из информации о списании
        List<PaymentInfoExtendedDto> payments = new ArrayList<>();
        for (WithdrawalResultDto withdrawal : request.payments) {
            PaymentInfoExtendedDto paymentInfo = new PaymentInfoExtendedDto();
            // сумма платежа в валюте требования
            paymentInfo.amount = withdrawal.amount;
            Payment payment;
            // ищем платеж
            payment = paymentDao.findActiveByReferenceCurrencyAmountAndWithdrawalType(
                withdrawal.reference,
                withdrawal.currency.id,
                withdrawal.amountPaid,
                withdrawal.withdrawalType.id
            );

            if (payment != null) {
                paymentInfo.payment = paymentMapper.toDto(payment);
                paymentInfo.isNewPayment = false;
                // ищем использованные суммы по таблице связей
                List<RelatedPayment> relatedPayments = relatedPaymentDao.findNonDeletedByPaymentId(payment.id);
                // использованная сумма в валюте требования
                paymentInfo.usedAmount = relatedPayments.stream()
                    .map(p -> p.amount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                // использованная сумма в валюте платежа
                paymentInfo.usedAmountOfPayment = relatedPayments.stream()
                    .map(p -> p.amountOfPayment)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                payments.add(paymentInfo);

            } else {
                // создаем платеж
                payment = new Payment();
                payment.amount = withdrawal.amountPaid;
                payment.currencyId = withdrawal.currency.id;
                payment.createTime = Instant.now();
                // payment.execTime
                // payment.payment
                // payment.valDate
                payment.paymentLinkType = PaymentLinkType.REQUIREMENT;
                payment.paymentResult = PaymentResult.PAID;
                payment.reference = withdrawal.reference;
                payment.withdrawalTypeId = withdrawal.withdrawalType.id;
                payment.persist();
                journal.createdPayments.add(payment.id);

                paymentInfo.payment = paymentMapper.toDto(payment);
                // мы его уже сохранили, поэтому false
                paymentInfo.isNewPayment = false;
                paymentInfo.usedAmount = BigDecimal.ZERO;
                paymentInfo.usedAmountOfPayment = BigDecimal.ZERO;
                payments.add(paymentInfo);
            }
        }

        // распределяем сумму платежей по требованиям
        Set<Long> requirementsForUpdate = new HashSet<>();
        for (int p = 0, r = 0; p < payments.size() && r < request.requirements.size(); ) {
            log.infof("-------------------------------------");
            ReferenceDto requirementRef = request.requirements.get(r);
            PaymentInfoExtendedDto paymentInfo = payments.get(p);

            PaymentDTO currentPayment = paymentInfo.payment;
            Requirement currentRequirement = requirementMap.get(requirementRef.id);

            // сумма платежа в валюте платежа
            BigDecimal availableInPaymentCurrency = currentPayment.amount.subtract(paymentInfo.usedAmountOfPayment);
            // сумма платежа в валюте требования
            BigDecimal availableInRequirementCurrency = paymentInfo.amount.subtract(paymentInfo.usedAmount);

            log.infof("--- currentPayment: amount = %s (%s), amountInReqCurrency = %s (%s), usedAmount = %s (%s), usedAmountOfPayment = %s (%s)",
                currentPayment.amount, currentPayment.currencyId, paymentInfo.amount, currentRequirement.currencyId,
                paymentInfo.usedAmount, currentRequirement.currencyId, paymentInfo.usedAmountOfPayment, currentPayment.currencyId
            );
            log.infof("--- currentRequirement: id = %s, amount = %s (%s), paidAmount = %s, unpaidAmount = %s",
                currentRequirement.id, currentRequirement.amount, currentRequirement.currencyId, currentRequirement.paidAmount, currentRequirement.unpaidAmount)
            ;
            log.infof("--- availableInRequirementCurrency = %s (%s), availableInPaymentCurrency = %s (%s)",
                availableInRequirementCurrency, currentRequirement.currencyId,
                availableInPaymentCurrency, currentPayment.currencyId
            );

            if (currentRequirement.unpaidAmount.compareTo(BigDecimal.ZERO) == 0) {
                // требование уже оплачено, переходим к следующему
                r += 1;
            } else if (availableInRequirementCurrency.compareTo(BigDecimal.ZERO) == 0) {
                // вся сумма платежа израсходована, переходим к следующему
                p += 1;

            } else if (availableInRequirementCurrency.compareTo(currentRequirement.unpaidAmount) <= 0) {
                // p.amount < r.amount, вся сумма платежа уходит на текущее требование
                // создаем связанный платеж
                RelatedPayment relatedPayment = createRelatedPayment(
                    availableInRequirementCurrency,
                    availableInPaymentCurrency,
                    currentRequirement.id,
                    currentPayment.id
                );
                relatedPayment.persist();
                journal.createdRelatedPayments.add(relatedPayment.id);
                log.infof("-------- relatedPayment: id=%s, amount = %s (%s), amountOfPayment = %s (%s)",
                    relatedPayment.id, relatedPayment.amount, currentRequirement.currencyId,
                    relatedPayment.amountOfPayment, currentPayment.currencyId);

                currentRequirement.relatedPayments.add(relatedPayment);
                // переходим к следующему платежу
                p += 1;
                // требование не меняется, если остаток суммы > 0
                if (availableInRequirementCurrency.compareTo(currentRequirement.unpaidAmount) == 0) {
                    // суммы равны, переходим к следующему требованию
                    r += 1;
                }
                currentRequirement.paidAmount = currentRequirement.paidAmount.add(availableInRequirementCurrency);
                currentRequirement.unpaidAmount = currentRequirement.unpaidAmount.subtract(availableInRequirementCurrency);
                paymentInfo.usedAmount = paymentInfo.usedAmount.add(availableInRequirementCurrency);
                paymentInfo.usedAmountOfPayment = paymentInfo.usedAmountOfPayment.add(availableInPaymentCurrency);
                requirementsForUpdate.add(currentRequirement.id);
                BigDecimal currentTxAmount = response.currentTransactionAmounts.getOrDefault(currentRequirement.id, BigDecimal.ZERO);
                response.currentTransactionAmounts.put(currentRequirement.id, currentTxAmount.add(availableInRequirementCurrency));
            } else if (paymentInfo.amount.compareTo(currentRequirement.unpaidAmount) > 0) {
                // p.amount > r.amount, требование будет оплачено полностью, но платеж не распределён

                // конвертируем доступную часть суммы в валюту платежа
                BigDecimal amountOfPayment;
                if (currentPayment.currencyId.equals(currentRequirement.currencyId)) {
                    amountOfPayment = currentRequirement.unpaidAmount;
                } else {
                    // пробуем сконвертировать сумму не обращаясь к sys-cur
                    BigDecimal rateValue = currentPayment.amount.divide(paymentInfo.amount, 10, RoundingMode.HALF_UP);
                    amountOfPayment = currentRequirement.unpaidAmount.multiply(rateValue).setScale(2, RoundingMode.HALF_UP);
                    log.infof("-------- rateValue=%s, amountOfPayment = %s (%s)",
                        rateValue, amountOfPayment, currentPayment.currencyId
                    );
                }

                // создаем связанный платеж
                RelatedPayment relatedPayment = createRelatedPayment(
                    currentRequirement.unpaidAmount,
                    amountOfPayment,
                    currentRequirement.id,
                    currentPayment.id
                );
                relatedPayment.persist();
                journal.createdRelatedPayments.add(relatedPayment.id);
                log.infof("-------- relatedPayment: id=%s, amount = %s (%s), amountOfPayment = %s (%s)",
                    relatedPayment.id, relatedPayment.amount, currentRequirement.currencyId,
                    relatedPayment.amountOfPayment, currentPayment.currencyId);

                currentRequirement.relatedPayments.add(relatedPayment);
                // индекс платежа не меняется, т.к. остаток суммы > 0
                paymentInfo.usedAmount = paymentInfo.usedAmount.add(currentRequirement.unpaidAmount);
                paymentInfo.usedAmountOfPayment = paymentInfo.usedAmountOfPayment.add(amountOfPayment);
                // переходим к следующему требованию
                r += 1;
                currentRequirement.paidAmount = currentRequirement.paidAmount.add(currentRequirement.unpaidAmount);
                currentRequirement.unpaidAmount = BigDecimal.ZERO;
                requirementsForUpdate.add(currentRequirement.id);
                BigDecimal currentTxAmount = response.currentTransactionAmounts.getOrDefault(currentRequirement.id, BigDecimal.ZERO);
                response.currentTransactionAmounts.put(currentRequirement.id, currentTxAmount.add(amountOfPayment));
            }
            log.infof("-------- updatedRequirement: id = %s, amount = %s (%s), paidAmount = %s, unpaidAmount = %s",
                currentRequirement.id, currentRequirement.amount, currentRequirement.currencyId, currentRequirement.paidAmount, currentRequirement.unpaidAmount);
        }

        for (PaymentInfoExtendedDto payment : payments) {
            PaymentInfoDto paymentInfoDto = new PaymentInfoDto();
            paymentInfoDto.payment = payment.payment;
            paymentInfoDto.usedAmountOfPayment = payment.usedAmountOfPayment;
            paymentInfoDto.usedAmount = payment.usedAmount;
            paymentInfoDto.isNewPayment = payment.isNewPayment;
            response.paymentsInfo.add(paymentInfoDto);
        }

        response.journal = journal;

        for (var requirementId : requirementMap.keySet()) {
            Requirement requirement = requirementMap.get(requirementId);
            if (requirementsForUpdate.contains(requirementId)) {
                // Обновляем только требования в списке на обновление
                processRequirementUpdateWithoutBbpUpdate(requirement, true, false);
            }
            response.requirements.add(requirementMapper.toDto(requirement));
        }
        log.infof("---- response: %s", response);
        return response;
    }


    // формируем связанный платеж
    private RelatedPayment createRelatedPayment(BigDecimal amount, BigDecimal amountOfPayment, Long requirementId, Long paymentId) {
        RelatedPayment relatedPayment = new RelatedPayment();
        relatedPayment.amount = amount;
        relatedPayment.amountOfPayment = amountOfPayment;
        relatedPayment.requirementOfRelatedPayments = Requirement.getEntityManager().getReference(Requirement.class, requirementId);
        relatedPayment.payment = Payment.getEntityManager().getReference(Payment.class, paymentId);
        return relatedPayment;
    }


    private RefundingPayment createRefundingPayment(Long paymentId, BigDecimal amount) {
        RefundingPayment refundingPayment = new RefundingPayment();
        Payment payment = Payment.getEntityManager().getReference(Payment.class, paymentId);
        refundingPayment.paymentOfRefundPayments = payment;
        refundingPayment.amount = amount;
        refundingPayment.creationTime = Instant.now();
        refundingPayment.isDeleted = false;
        refundingPayment.currencyId = payment.currencyId;
        refundingPayment.paymentResult = PaymentResult.PAID;
        refundingPayment.paymentLinkType = payment.paymentLinkType;
        refundingPayment.reference = payment.payment;
        refundingPayment.creationTime = Instant.now();
        refundingPayment.refundTime = Instant.now();
        return refundingPayment;
    }


    // возврат платежа по списку требований
    @Override
    @Transactional
    public RefundResponse refundOfPayment(RefundOfRequirementsDto request) {
        log.infof("refundOfPayment: %s", request);

        RefundResponse response = new RefundResponse();
        RefundResultDto result = new RefundResultDto();
        RefundJournalDto journal = new RefundJournalDto();
        response.refundResult = result;
        response.refundJournal = journal;

        if (request.requirements == null || request.requirements.isEmpty()) {
            return response;
        }

        Set<Long> idSet = request.requirements.stream()
            .filter(Objects::nonNull)
            .map(r -> r.id)
            .collect(Collectors.toSet());

        List<Requirement> requirements = requirementDao.findActiveByIds(idSet);

        if (requirements.isEmpty()) {
            return response;
        } else {
            // проверяем состояние требований
            List<Long> incorrectData = requirements.stream()
                .filter(r -> {
                    String bbpState = r.bbpState000StateCode;
                    return ((!RmsConstants.PART_PAID_STATUS.equalsIgnoreCase(bbpState) && !RmsConstants.PAID_STATUS.equalsIgnoreCase(bbpState)) ||
                        (!RequirementStatus.WAIT.equals(r.state) && !RequirementStatus.PAID.equals(r.state))
                    );
                })
                .map(r -> r.id)
                .toList();
            if (!incorrectData.isEmpty()) {
                throw new RuntimeException(String.format("Refund is not allowed for Requirements (id): %s", incorrectData));
            }
        }

        // получаем сумму к возврату в валюте требований
        BigDecimal remainingAmount;
        if (request.refundAmount != null && (request.refundAmount.compareTo(BigDecimal.ZERO) > 0)) {
            remainingAmount = request.refundAmount;
        } else {
            // считаем по требованиям (требования должны быть в одной валюте)
            remainingAmount = requirements.stream()
                .map(r -> r.paidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        log.infof("--- remainingAmount = %s", remainingAmount);

        // получаем все связанные платежи
        List<RelatedPayment> relatedPayments = relatedPaymentDao.findPaidByRequirementIds(idSet);
        log.infof("--- relatedPayments \r\n %s", relatedPayments);

        if (relatedPayments.isEmpty()) {
            // возвращаем результат без изменений
            requirements.forEach(r -> {
                RequirementStateInfoDto requirementInfo = RequirementMapperUtils.fillRequirementInfo(
                    r.id,
                    RequirementAction.SAVE,
                    r.state,
                    r.amount,
                    r.paidAmount,
                    r.priority,
                    r.indicatorId);
                result.requirementsInfo.add(requirementInfo);
            });
            return response;
        }

        // сохраняем первоначальные значения для отката
        requirements.forEach(r -> journal.requirementJournal.put(
            r.id, RequirementMapperUtils.fillRequirementJournal(r)
        ));
        relatedPayments.forEach(p -> journal.relatedPaymentsJournal.add(
            fillRelatedPaymentsJournal(
                p.id,
                p.amount,
                p.amountOfPayment,
                p.requirementOfRelatedPayments.id,
                p.payment.id
            )
        ));

        Map<Long, Requirement> requirementMap = requirements.stream()
            .collect(Collectors.toMap(r -> r.id, Functions.identity()));

        // распределение возвращаемых сумм по платежам
        Map<Long, BigDecimal> paymentRefundMap = new HashMap<>();
        Map<Long, BigDecimal> requirementsForUpdate = new HashMap<>();

        for (RelatedPayment currentRelatedPayment : relatedPayments) {
            Requirement currentRequirement = requirementMap.get(currentRelatedPayment.requirementOfRelatedPayments.id);
            Payment currentPayment = currentRelatedPayment.payment;

            log.infof("--- currentRequirement: id = %s, amount = %s (%s), paidAmount = %s, unpaidAmount = %s",
                currentRequirement.id, currentRequirement.amount, currentRequirement.currencyId,
                currentRequirement.paidAmount, currentRequirement.unpaidAmount
            );
            log.infof("--- currentRelatedPayment: id = %s, amount = %s (%s), amountOfPayment = %s (%s)",
                currentRelatedPayment.id, currentRelatedPayment.amount, currentRequirement.currencyId,
                currentRelatedPayment.amountOfPayment, currentPayment.currencyId
            );
            log.infof("--- remainingAmount = %s (%s)",
                remainingAmount, currentRequirement.currencyId
            );

            if ((currentRelatedPayment.amount.compareTo(BigDecimal.ZERO) > 0) && (remainingAmount.compareTo(currentRelatedPayment.amount) >= 0)) {
                remainingAmount = remainingAmount.subtract(currentRelatedPayment.amount);
                // эта сумма должна быть в валюте платежа
                BigDecimal refundAmount = paymentRefundMap.getOrDefault(currentPayment.id, BigDecimal.ZERO);
                paymentRefundMap.put(currentPayment.id, refundAmount.add(currentRelatedPayment.amountOfPayment));

                currentRequirement.paidAmount = currentRequirement.paidAmount.subtract(currentRelatedPayment.amount);
                currentRequirement.unpaidAmount = currentRequirement.unpaidAmount.add(currentRelatedPayment.amount);
                requirementsForUpdate.compute(currentRequirement.id, (id, cta) -> (cta == null) ? currentRelatedPayment.amount.negate() : cta.subtract(currentRelatedPayment.amount));

                currentRelatedPayment.amount = BigDecimal.ZERO;
                currentRelatedPayment.amountOfPayment = BigDecimal.ZERO;
                currentRelatedPayment.update();

                log.infof("-------- relatedPayment: id=%s, amount = %s (%s), amountOfPayment = %s (%s)",
                    currentRelatedPayment.id, currentRelatedPayment.amount, currentRequirement.currencyId,
                    currentRelatedPayment.amountOfPayment, currentPayment.currencyId);


            } else if (currentRelatedPayment.amount.compareTo(remainingAmount) > 0) {
                // конвертируем изменяемую часть суммы в валюту платежа
                BigDecimal amountOfPayment;
                if (currentPayment.currencyId.equals(currentRequirement.currencyId)) {
                    amountOfPayment = remainingAmount;
                } else {
                    // пробуем сконвертировать сумму в валюту платежа не обращаясь к sys-cur
                    BigDecimal rateValue = currentRelatedPayment.amountOfPayment.divide(currentRelatedPayment.amount, 10, RoundingMode.HALF_UP);
                    amountOfPayment = remainingAmount.multiply(rateValue).setScale(2, RoundingMode.HALF_UP);
                    log.infof("-------- rateValue=%s, amountOfPayment = %s (%s)",
                        rateValue, amountOfPayment, currentPayment.currencyId
                    );
                }

                currentRelatedPayment.amount = currentRelatedPayment.amount.subtract(remainingAmount);
                currentRelatedPayment.amountOfPayment = currentRelatedPayment.amountOfPayment.subtract(amountOfPayment);
                currentRelatedPayment.update();

                BigDecimal refundAmount = paymentRefundMap.getOrDefault(currentPayment.id, BigDecimal.ZERO);
                paymentRefundMap.put(currentPayment.id, refundAmount.add(amountOfPayment));

                currentRequirement.paidAmount = currentRequirement.paidAmount.subtract(remainingAmount);
                currentRequirement.unpaidAmount = currentRequirement.unpaidAmount.add(remainingAmount);
                requirementsForUpdate.compute(currentRequirement.id, (id, cta) -> (cta == null) ? amountOfPayment.negate() : cta.subtract(amountOfPayment));
                remainingAmount = BigDecimal.ZERO;

                log.infof("-------- relatedPayment: id=%s, amount = %s (%s), amountOfPayment = %s (%s)",
                    currentRelatedPayment.id, currentRelatedPayment.amount, currentRequirement.currencyId,
                    currentRelatedPayment.amountOfPayment, currentPayment.currencyId);


            } else if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }

            log.infof("-------- updatedRequirement: id = %s, amount = %s (%s), paidAmount = %s, unpaidAmount = %s",
                currentRequirement.id, currentRequirement.amount, currentRequirement.currencyId, currentRequirement.paidAmount, currentRequirement.unpaidAmount);
        }

        for (var requirementId : requirementMap.keySet()) {
            Requirement requirement = requirementMap.get(requirementId);
            if (requirementsForUpdate.containsKey(requirementId)) {
                // Обновляем только требования в списке на обновление
                processRequirementUpdateWithoutBbpUpdate(requirement, false, false);
            }

            RequirementStateInfoDto requirementInfo = RequirementMapperUtils.fillRequirementInfo(
                requirement.id,
                RequirementAction.SAVE,
                requirement.state,
                requirement.amount,
                requirement.paidAmount,
                requirement.priority,
                requirement.indicatorId);
            result.requirementsInfo.add(requirementInfo);
            requirementInfo.currentTransactionAmount = requirementsForUpdate.get(requirementId);

        }

        // создаем записи по суммам возврата
        for (var paymentId : paymentRefundMap.keySet()) {
            Payment payment = paymentDao.findById(paymentId);
            RefundingPayment paymentRefund = createRefundingPayment(paymentId, paymentRefundMap.get(paymentId));
            paymentRefund.persist();
            payment.refundPayments.add(paymentRefund);
            payment.update();
            journal.createdPaymentRefunds.add(paymentRefund.id);

            PaymentRefundInfoDto refundInfo = fillRefundInfo(paymentId, payment.currencyId, paymentRefund.amount);
            result.refundsInfo.add(refundInfo);
        }
        return response;
    }


    // возврат платежа
    @Override
    @Transactional
    public RefundResponse refundOfPayment(RefundOfPaymentDto request) {
        log.infof("refundOfPayment: %s", request);

        RefundResponse response = new RefundResponse();
        RefundResultDto result = new RefundResultDto();
        RefundJournalDto journal = new RefundJournalDto();
        response.refundResult = result;
        response.refundJournal = journal;

        if (request.payment == null || request.payment.id == null) {
            return response;
        }
        Payment payment = paymentDao.findById(request.payment.id);
        if (payment == null || Boolean.TRUE.equals(payment.isDeleted)) {
            return response;
        }
        if (!PaymentResult.PAID.equals(payment.paymentResult)) {
            throw new RuntimeException(String.format("Refund is not allowed for Payment (id=%s) in state (%s)", payment.id, payment.paymentResult));
        }

        // получаем сумму к возврату (в валюте платежа)
        BigDecimal refundAmount;
        // получаем часть платежа, которую уже могли вернуть/назначить к возврату
        BigDecimal refunded = BigDecimal.ZERO;
        if (payment.refundPayments != null && !payment.refundPayments.isEmpty()) {
            refunded = payment.refundPayments.stream()
                .filter(p -> !Boolean.TRUE.equals(p.isDeleted))
                .map(p -> p.amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        // оставшаяся сумма платежа
        BigDecimal remaining = payment.amount.subtract(refunded);
        if (request.refundAmount != null && (request.refundAmount.compareTo(BigDecimal.ZERO) > 0)) {
            refundAmount = request.refundAmount;
            if (refundAmount.compareTo(remaining) > 0) {
                throw new RuntimeException(String.format("Refund amount (%s) is greater than not refunded Payment amount (%s)", refundAmount, remaining));
            }
        } else {
            // считаем что возвращается вся сумма
            refundAmount = remaining;
        }
        log.infof("--- refundAmount = %s", refundAmount);

        // получаем все связанные платежи
        List<RelatedPayment> relatedPayments = relatedPaymentDao.findRefundableByPaymentId(payment.id);
        log.infof("--- relatedPayments \r\n %s", relatedPayments);

        if (relatedPayments.isEmpty()) {
            return response;
        }

        Set<Long> requirementIdSet = relatedPayments.stream()
            .map(rp -> rp.requirementOfRelatedPayments.id)
            .collect(Collectors.toSet());

        List<Requirement> requirements = requirementDao.findActiveByIds(requirementIdSet);

        // проверяем состояние требований
        List<Long> incorrectData = requirements.stream()
            .filter(r -> {
                String bbpState = r.bbpState000StateCode;
                // приостановлено // аннулировано
                return (RmsConstants.SUSPENDED_STATE.equals(bbpState) || RmsConstants.CANCELLED_STATE.equals(bbpState) ||
                    RequirementStatus.SUSPENDED.equals(r.state) || RequirementStatus.CANCELED.equals(r.state)
                );
            })
            .map(r -> r.id)
            .toList();
        if (!incorrectData.isEmpty()) {
            throw new RuntimeException(String.format("Refund is not allowed for Requirements (id): %s", incorrectData));
        }

        // сохраняем первоначальные значения для отката
        requirements.forEach(r -> journal.requirementJournal.put(
            r.id, RequirementMapperUtils.fillRequirementJournal(r)
        ));
        relatedPayments.forEach(p -> journal.relatedPaymentsJournal.add(
            fillRelatedPaymentsJournal(
                p.id,
                p.amount,
                p.amountOfPayment,
                p.requirementOfRelatedPayments.id,
                p.payment.id
            )
        ));

        Map<Long, Requirement> requirementMap = requirements.stream()
            .collect(Collectors.toMap(r -> r.id, Functions.identity()));

        Set<Long> requirementsForUpdate = new HashSet<>();
        // сумма в валюте платежа
        BigDecimal remainingAmount = refundAmount;

        // распределяем возвращаемую сумму по связям с требованиями
        for (RelatedPayment currentRelatedPayment : relatedPayments) {
            Requirement currentRequirement = requirementMap.get(currentRelatedPayment.requirementOfRelatedPayments.id);
            boolean isWrittenOff = RequirementStatus.WRITTEN_OFF.equals(currentRequirement.state);

            log.infof("--- currentRequirement: id = %s, amount = %s (%s), paidAmount = %s, unpaidAmount = %s",
                currentRequirement.id, currentRequirement.amount, currentRequirement.currencyId,
                currentRequirement.paidAmount, currentRequirement.unpaidAmount
            );
            log.infof("--- currentRelatedPayment: id = %s, amount = %s (%s), amountOfPayment = %s (%s)",
                currentRelatedPayment.id, currentRelatedPayment.amount, currentRequirement.currencyId,
                currentRelatedPayment.amountOfPayment, payment.currencyId
            );
            log.infof("--- remainingAmount = %s (%s)",
                remainingAmount, payment.currencyId
            );

            if ((currentRelatedPayment.amountOfPayment.compareTo(BigDecimal.ZERO) > 0) && (remainingAmount.compareTo(currentRelatedPayment.amountOfPayment) >= 0)) {
                remainingAmount = remainingAmount.subtract(currentRelatedPayment.amountOfPayment);

                currentRequirement.paidAmount = currentRequirement.paidAmount.subtract(currentRelatedPayment.amount);
                if (isWrittenOff) {
                    // если требование списано, переносим оплаченную часть на списанную
                    currentRequirement.writeOffAmount = currentRequirement.writeOffAmount.add(currentRelatedPayment.amount);
                } else {
                    currentRequirement.unpaidAmount = currentRequirement.unpaidAmount.add(currentRelatedPayment.amount);
                }
                requirementsForUpdate.add(currentRequirement.id);

                currentRelatedPayment.amount = BigDecimal.ZERO;
                currentRelatedPayment.amountOfPayment = BigDecimal.ZERO;
                currentRelatedPayment.update();

                log.infof("-------- relatedPayment: id=%s, amount = %s (%s), amountOfPayment = %s (%s)",
                    currentRelatedPayment.id, currentRelatedPayment.amount, currentRequirement.currencyId,
                    currentRelatedPayment.amountOfPayment, payment.currencyId);


            } else if (currentRelatedPayment.amountOfPayment.compareTo(remainingAmount) > 0) {
                // конвертируем изменяемую часть суммы в валюту требования
                BigDecimal amount;
                if (payment.currencyId.equals(currentRequirement.currencyId)) {
                    amount = remainingAmount;
                } else {
                    // пробуем сконвертировать сумму в валюту требования не обращаясь к sys-cur
                    BigDecimal rateValue = currentRelatedPayment.amount.divide(currentRelatedPayment.amountOfPayment, 10, RoundingMode.HALF_UP);
                    amount = remainingAmount.multiply(rateValue).setScale(2, RoundingMode.HALF_UP);
                    log.infof("-------- rateValue=%s, amount = %s (%s)",
                        rateValue, amount, currentRequirement.currencyId
                    );
                }
                currentRelatedPayment.amount = currentRelatedPayment.amount.subtract(amount);
                currentRelatedPayment.amountOfPayment = currentRelatedPayment.amountOfPayment.subtract(remainingAmount);
                currentRelatedPayment.update();

                currentRequirement.paidAmount = currentRequirement.paidAmount.subtract(amount);
                if (isWrittenOff) {
                    currentRequirement.writeOffAmount = currentRequirement.writeOffAmount.add(amount);
                } else {
                    currentRequirement.unpaidAmount = currentRequirement.unpaidAmount.add(amount);
                }
                requirementsForUpdate.add(currentRequirement.id);
                remainingAmount = BigDecimal.ZERO;

                log.infof("-------- relatedPayment: id=%s, amount = %s (%s), amountOfPayment = %s (%s)",
                    currentRelatedPayment.id, currentRelatedPayment.amount, currentRequirement.currencyId,
                    currentRelatedPayment.amountOfPayment, payment.currencyId);


            } else if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }

            log.infof("-------- updatedRequirement: id = %s, amount = %s (%s), paidAmount = %s, unpaidAmount = %s",
                currentRequirement.id, currentRequirement.amount, currentRequirement.currencyId, currentRequirement.paidAmount, currentRequirement.unpaidAmount);
        }

        for (var requirementId : requirementMap.keySet()) {
            Requirement requirement = requirementMap.get(requirementId);
            if (requirementsForUpdate.contains(requirementId)) {
                // Обновляем только требования в списке на обновление
                processRequirementUpdateWithoutBbpUpdate(requirement, false, true);
            }

            RequirementStateInfoDto requirementInfo = RequirementMapperUtils.fillRequirementInfo(
                requirement.id,
                RequirementAction.SAVE,
                requirement.state,
                requirement.amount,
                requirement.paidAmount,
                requirement.priority,
                requirement.indicatorId);
            result.requirementsInfo.add(requirementInfo);
        }

        // создаем запись по сумме возврата
        RefundingPayment paymentRefund = createRefundingPayment(payment.id, refundAmount);
        paymentRefund.persist();
        payment.refundPayments.add(paymentRefund);
        payment.update();
        journal.createdPaymentRefunds.add(paymentRefund.id);

        PaymentRefundInfoDto refundInfo = fillRefundInfo(payment.id, payment.currencyId, paymentRefund.amount);
        result.refundsInfo.add(refundInfo);

        return response;
    }

    private void processRequirementUpdateWithoutBbpUpdate(Requirement requirement, boolean checkWaitState, boolean ignoreWrittenOff) {
        if (checkWaitState && !RequirementStatus.WAIT.equals(requirement.state)) {
            return;
        }

        if (ignoreWrittenOff && RequirementStatus.WRITTEN_OFF.equals(requirement.state)) {
            return;
        }

        // Обновляем состояние
        if (requirement.amount.compareTo(BigDecimal.ZERO) == 0 && requirement.unpaidAmount.compareTo(BigDecimal.ZERO) == 0) {
            requirement.state = RequirementStatus.CANCELED;
        } else if (requirement.amount.compareTo(BigDecimal.ZERO) > 0) {
            requirement.state = requirement.unpaidAmount.compareTo(BigDecimal.ZERO) > 0 ?
                RequirementStatus.WAIT : RequirementStatus.PAID;
        }

        // Управление датой фактической оплаты
        if (RequirementStatus.PAID.equals(requirement.state) && requirement.actualPaymentDate == null) {
            requirement.actualPaymentDate = SessionContext.getOperationDate();
        }

        requirement.update();
    }

    private PaymentRefundInfoDto fillRefundInfo(Long paymentId, Long currencyId, BigDecimal amount) {
        PaymentRefundInfoDto refundInfo = new PaymentRefundInfoDto();
        refundInfo.paymentId = paymentId;
        refundInfo.currencyId = currencyId;
        refundInfo.amount = amount;
        return refundInfo;
    }

    private RelatedPaymentsJournalDto fillRelatedPaymentsJournal(Long id, BigDecimal amount, BigDecimal amountOfPayment,
                                                                 Long requirementId, Long paymentId) {
        RelatedPaymentsJournalDto relationJournal = new RelatedPaymentsJournalDto();
        relationJournal.relationId = id;
        relationJournal.amount = amount;
        relationJournal.amountOfPayment = amountOfPayment;
        relationJournal.requirementId = requirementId;
        relationJournal.paymentId = paymentId;
        return relationJournal;
    }

    @Override
    public void refundOfPaymentUndo(RefundJournalDto request) {
        log.infof("refundOfPaymentUndo: %s", request);

        Map<Long, RelatedPaymentsJournalDto> relatedToUpdate;
        if (request.relatedPaymentsJournal != null && !request.relatedPaymentsJournal.isEmpty()) {
            relatedToUpdate = request.relatedPaymentsJournal.stream()
                .collect(Collectors.toMap(r -> r.relationId, Functions.identity()));
        } else {
            relatedToUpdate = new HashMap<>();
        }

        if (request.requirementJournal != null) {
            for (RequirementJournalDto journal : request.requirementJournal.values()) {
                Requirement requirement = requirementDao.findById(journal.id);
                if (requirement != null && !Boolean.TRUE.equals(requirement.isDeleted)) {

                    // восстановление атрибутов требований
                    requirementRouterService.restoreRequirementForRefundUndo(requirement, journal, relatedToUpdate);
                }
            }
        }

        // удаление созданных записей по возврату платежей
        if (request.createdPaymentRefunds != null && !request.createdPaymentRefunds.isEmpty()) {
            for (Long refundId : request.createdPaymentRefunds) {
                RefundingPayment paymentRefund = refundingPaymentDao.findById(refundId);
                if (paymentRefund != null && !Boolean.TRUE.equals(paymentRefund.isDeleted)) {
                    requirementRouterService.deletePaymentRefundLink(paymentRefund, refundId);
                }
            }
        }
    }

    @Override
    @Transactional
    public void processRefundingPayment(List<AdjustRefundPaymentResultDto> outgoingPayments,
                                        List<Pair<RequirementStateInfoDto, Requirement>> decreasingRequirements,
                                        AdjustByPastDateJournalDto journal, AdjustByPastDateResultDto result) {

        if (outgoingPayments == null || outgoingPayments.isEmpty()) {
            throw new RuntimeException("outgoingPayments cannot be null or empty for decreasing requirements");
        }

        // Группируем требования по КНП, потому что возврат всегда должен идти в рамках одного paymentPurposeCode.
        Map<String, List<Pair<RequirementStateInfoDto, Requirement>>> requirementMapByPpc = new HashMap<>();

        decreasingRequirements.forEach(pair -> {
            Requirement requirement = pair.b;
            String requirementPpc = pair.a.paymentPurposeCode;
            log.infof("adjustByPastDate: decreasing requirement=%s, requirementPpc=%s", requirement, requirementPpc);
            if (requirementMapByPpc.containsKey(requirementPpc)) {
                requirementMapByPpc.get(requirementPpc).add(new Pair<>(pair.a, requirement));
            } else {
                requirementMapByPpc.put(requirementPpc, new ArrayList<>(List.of(new Pair<>(pair.a, requirement))));
            }
            journal.requirementJournalMap.put(requirement.id, RequirementMapperUtils.fillRequirementJournal(requirement));
        });

        log.infof("adjustByPastDate: requirementMapByPpc=%s", requirementMapByPpc);

        for (AdjustRefundPaymentResultDto outgoingPayment : outgoingPayments) {
            String ppc = outgoingPayment.paymentPurposeCode;
            if (ppc == null || ppc.isEmpty()) throw new RuntimeException(String.format("PaymentPurposeCode is null of empty ppc=%s", ppc));
            if (!requirementMapByPpc.containsKey(ppc)) throw new RuntimeException(String.format("PaymentPurposeCode %s is not exists in requirementMapByPpc=%s", ppc, requirementMapByPpc));

            BigDecimal paymentBalance = outgoingPayment.amount;

            log.infof("processRefundingPayment: start processing refund payment amount=%s", paymentBalance);
            if (paymentBalance.signum() > 0) {
                log.infof("processRefundingPayment: creating refunding payment entity");

                List<Pair<RequirementStateInfoDto, Requirement>> requirementsForUpdate = requirementMapByPpc.get(ppc);
                // Идем по приоритетам, чтобы порядок распределения был предсказуем и повторяем.
                requirementsForUpdate.sort(Comparator.comparingInt(r -> Objects.requireNonNullElse(r.a.priority, Integer.MAX_VALUE)));

                RefundingPayment refundingPayment = new RefundingPayment();
                refundingPayment.reference = outgoingPayment.reference;
                refundingPayment.amount = outgoingPayment.amount;
                refundingPayment.currencyId = outgoingPayment.currency.id;
                refundingPayment.valueDate = LocalDate.now();
                refundingPayment.paymentResult = PaymentResult.PAID;
                refundingPayment.paymentLinkType = PaymentLinkType.REQUIREMENT;
                refundingPayment.provisionMethodId = outgoingPayment.paymentType.id;

                RefundingPayment.persistOrUpdate(refundingPayment);

                log.infof("processRefundingPayment: refunding payment persisted, %s", refundingPayment);

                List<RequirementRefundingPayment> refundingPayments = new ArrayList<>();

                for (Pair<RequirementStateInfoDto, Requirement> pair : requirementsForUpdate) {
                    RequirementStateInfoDto reqDto = pair.a;
                    Requirement requirement = pair.b;
                    BigDecimal refundable = requirement.paidAmount.max(BigDecimal.ZERO);
                    if (paymentBalance.signum() <= 0) {
                        break;
                    }
                    if (refundable.signum() <= 0) {
                        continue;
                    }

                    // Возвращаем только то, что реально можно вернуть по требованию на текущем шаге.
                    BigDecimal refundAmount = paymentBalance.min(refundable);
                    log.infof("processRefundingPayment: processing requirement=%s, refundable amount=%s", requirement, refundAmount);

                    requirement.paidAmount = requirement.paidAmount.subtract(refundAmount);
                    requirement.unpaidAmount = requirement.amount.subtract(requirement.paidAmount);
                    if (requirement.amount.compareTo(BigDecimal.ZERO) == 0 && requirement.unpaidAmount.compareTo(BigDecimal.ZERO) == 0) {
                        requirement.state = RequirementStatus.CANCELED;
                    } else {
                        requirement.state = requirement.unpaidAmount.compareTo(BigDecimal.ZERO) > 0 ? RequirementStatus.WAIT : RequirementStatus.PAID;
                    }

                    RequirementRefundingPayment rrp = new RequirementRefundingPayment();
                    rrp.distributionAmount = refundAmount;
                    rrp.requirementOfRefundingPayments = requirement;
                    rrp.refundingPayment = refundingPayment;
                    refundingPayments.add(rrp);

                    requirement.update();
                    paymentBalance = paymentBalance.subtract(refundAmount);

                    reqDto.payedAmount = requirement.paidAmount;
                    reqDto.status = requirement.state;
                }

                // Если после обхода требований по КНП остаток не нулевой — входной возврат больше, чем оплачено.
                if (paymentBalance.signum() > 0) {
                    throw new RuntimeException(String.format("The refund cannot be made due to the lack of funds in requirements by paymentPurposeCode=%s, remaining=%s", ppc, paymentBalance));
                }

                RequirementRefundingPayment.persist(refundingPayments);
                log.infof("processRefundingPayment: persisted %d refunding payment links refundingPayments=%s", refundingPayments.size(), requirementsForUpdate);

                journal.requirementRefundingPaymentIds.addAll(
                    refundingPayments.stream().map(rp -> rp.id).toList()
                );
                journal.refundingPaymentIds.add(refundingPayment.id);

            } else {
                throw new RuntimeException(String.format("The refund cannot be made due to the lack of funds for the refund in the payment. paymentBalance=%s", paymentBalance));
            }
        }
    }
}
