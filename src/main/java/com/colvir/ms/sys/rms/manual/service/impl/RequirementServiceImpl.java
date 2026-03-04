package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.common.Constants;
import com.colvir.ms.sys.rms.dto.AmountForIndicatorDto;
import com.colvir.ms.sys.rms.dto.BbpStateResult;
import com.colvir.ms.sys.rms.dto.BuildRequirementsDto;
import com.colvir.ms.sys.rms.dto.CreateRequirementDto;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RefundOfRequirementsDto;
import com.colvir.ms.sys.rms.dto.RefundResponse;
import com.colvir.ms.sys.rms.dto.RequirementAmountResponse;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementJournalDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementResponse;
import com.colvir.ms.sys.rms.dto.ReviewRequirementResultDto;
import com.colvir.ms.sys.rms.generated.domain.Payment;
import com.colvir.ms.sys.rms.generated.domain.RefundingPayment;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.manual.constant.RmsConstants;
import com.colvir.ms.sys.rms.manual.dao.PaymentDao;
import com.colvir.ms.sys.rms.manual.dao.RefundingPaymentDao;
import com.colvir.ms.sys.rms.manual.dao.RequirementDao;
import com.colvir.ms.sys.rms.manual.service.BaseProcessService;
import com.colvir.ms.sys.rms.manual.service.RequirementPaymentService;
import com.colvir.ms.sys.rms.manual.service.RequirementRouterService;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.util.RequirementMapperUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class RequirementServiceImpl implements RequirementService {

    @ConfigProperty(name = "requirements-machine-id", defaultValue = "")
    String machineId;

    @Inject
    BaseProcessService baseProcessService;

    @Inject
    RequirementPaymentService paymentService;

    @Inject
    Logger log;

    @Inject
    RequirementDao requirementDao;

    @Inject
    PaymentDao paymentDao;

    @Inject
    RefundingPaymentDao refundingPaymentDao;

    @Inject
    RequirementRouterService requirementRouterService;

    public static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.LOCAL_DATE_FORMAT);

    @Override
    public List<RequirementStateInfoDto> createRequirements(BuildRequirementsDto request) {
        // метод является частью общего метода "Изменение требований по договору"
        // TODO: установка холдов

        if (request.getPaymentData() == null || request.getPaymentData().isEmpty()) {
            // возвращаем без изменений, но исключаем null
            return List.of();
        }
        checkBuildRequirements(request);
        List<RequirementStateInfoDto> result = new ArrayList<>();
        for (RequirementStateInfoDto paymentData : request.getPaymentData()) {
            CreateRequirementDto createRequirementDto = new CreateRequirementDto()
                .setPaymentData(paymentData)
                .setClient(request.getClient())
                .setContract(request.getContract())
                .setCurrency(request.getCurrency())
                .setBusinessDate(request.getBusinessDate());
            RequirementStateInfoDto requirementInfo = this.createRequirement(createRequirementDto);
            result.add(requirementInfo);
        }
        return result;
    }

    @Override
    public List<RequirementStateInfoDto> createRequirements(List<CreateRequirementDto> metaRequirements) {
        if (Objects.isNull(metaRequirements) || metaRequirements.isEmpty()) {
            // возвращаем без изменений, но исключаем null
            return List.of();
        }
        List<Long> ids = requirementRouterService.createRequirements(metaRequirements);
        if (ids.size() != metaRequirements.size()) {
            throw new RuntimeException(String.format("Ids list size is not equal createRequirements size ids = %s, createRequirements = %s", ids.size(), metaRequirements.size()));
        }
        return IntStream.range(0, ids.size())
            .mapToObj(
                i -> RequirementMapperUtils.mapRequirementStateInfoDto(
                    metaRequirements.get(i).getPaymentData(), ids.get(i), RequirementAction.SAVE, RequirementStatus.WAIT)
            ).toList();
    }

    @Override
    public RequirementStateInfoDto createRequirement(CreateRequirementDto createRequirementDto) {

        log.infof("------------- createRequirementDto = %s ", createRequirementDto);
        // старт процесса ББП (удалить вместе с createRequirements)
        if (StringUtils.isEmpty(createRequirementDto.getInitialBbpState())) {
            String initialState = baseProcessService.startProcess(machineId, null);
            createRequirementDto.setInitialBbpState(initialState);
        }
        Long id = requirementRouterService.createRequirement(createRequirementDto);
        return RequirementMapperUtils.mapRequirementStateInfoDto(createRequirementDto.getPaymentData(), id, RequirementAction.SAVE, RequirementStatus.WAIT);
    }

    @Override
    public void checkBuildRequirements(BuildRequirementsDto request) {
        // проверяем корректность атрибутов записи:
        //    "состояние требования" должно быть = не сформировано;
        //    "сумма к оплате" должна быть > 0;
        //    "оплаченная сумма" должна быть = 0
        List<RequirementStateInfoDto> incorrectPaymentData = request.getPaymentData().stream()
            .peek(r -> {
                // защита от пустых сумм, чтобы не падала проверка дальше
                r.payedAmount = Objects.requireNonNullElse(r.payedAmount, BigDecimal.ZERO);
                r.amount = Objects.requireNonNullElse(r.amount, BigDecimal.ZERO);
            })
            .filter(r -> !RequirementAction.CREATE.equals(r.action) ||
                !RequirementStatus.NONEXISTENT.equals(r.status) ||
                r.amount.compareTo(BigDecimal.ZERO) <= 0 ||
                r.payedAmount.compareTo(BigDecimal.ZERO) != 0
            )
            .toList();

        if (!incorrectPaymentData.isEmpty()) {
            throw new RuntimeException(String.format("Incorrect PaymentData: %s", incorrectPaymentData));
        }
        if (request.getContract() == null || request.getContract().id == null) {
            throw new RuntimeException("Contract is null or does not contain id");
        }
        if (request.getCurrency() == null || request.getCurrency().id == null) {
            throw new RuntimeException("Currency is null or does not contain id");
        }
        if (request.getClient() == null || request.getClient().id == null) {
            throw new RuntimeException("Client is null or does not contain id");
        }


        // если требования создают с готовыми идентификаторами, они не должны совпадать с существующими
        // в противном случае роутер их обновит
        List<Long> newRequirementIds = request.getPaymentData().stream()
            .map(r -> r.requirementId)
            .filter(Objects::nonNull)
            .toList();
        if (!newRequirementIds.isEmpty()) {
            long countExisting = requirementDao.countByIds(newRequirementIds);
            if (countExisting > 0) {
                throw new RuntimeException("Some of new requirements already exist");
            }
        }
    }

    @Override
    public void deleteRequirements(List<Long> requirementIdList) {
        // TODO ? есть ограничения на удаление требования ?
        try {
            List<ObjectNode> response = requirementRouterService.deleteRequirements(requirementIdList);
            log.infof("Delete requirements modifyResponse: \r\n" + response);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error while deleting Requirements ids= %s: %s", requirementIdList, e));
        }
    }

    @Override
    public void restoreWithoutBbpCancelExecution(List<RequirementJournalDto> requirementJournal,
                                                 List<Long> createdPayments,
                                                 List<Long> createdRelatedPayments,
                                                 List<Long> createdRefundingPayments,
                                                 List<Long> createdReqRefundingPayments) {
        if (requirementJournal != null) {
            for (RequirementJournalDto requirementJournalDto : requirementJournal) {
                Requirement requirement = getRequirementById(requirementJournalDto.id);
                requirementRouterService.restoreRequirementWithoutBbpCancel(
                    requirement,
                    requirementJournalDto,
                    createdRelatedPayments,
                    createdReqRefundingPayments
                );
            }
        }

        if (createdPayments != null && !createdPayments.isEmpty()) {
            createdPayments.forEach(p -> {
                Payment payment = paymentDao.findById(p);
                if (payment != null && !Boolean.TRUE.equals(payment.isDeleted)) {
                    try {
                        requirementRouterService.deletePayment(payment);
                    } catch (Exception e) {
                        throw new RuntimeException(String.format("Error while deleting Payment id=%s: %s", payment.id, e));
                    }
                }
            });
        }

        if (createdRefundingPayments != null && !createdRefundingPayments.isEmpty()) {
            createdRefundingPayments.forEach(p -> {
                RefundingPayment refundingPayment = refundingPaymentDao.findById(p);
                if (refundingPayment != null && !Boolean.TRUE.equals(refundingPayment.isDeleted)) {
                    try {
                        requirementRouterService.deleteRefundingPayment(refundingPayment);
                    } catch (Exception e) {
                        throw new RuntimeException(String.format("Error while deleting RefundingPayment id=%s: %s", refundingPayment.id, e));
                    }
                }
            });
        }
    }

    @Override
    public void updateRequirementsUndo(List<RequirementJournalDto> journal) {
        log.infof("updateRequirementsUndo:\n%s", journal);
        restoreWithoutBbpCancelExecution(journal, null, null, null, null);
    }

    @Override
    @Transactional
    public ReviewRequirementResponse requirementReview(ReviewRequirementDto request) {
        // пересмотр требования
        // не поддерживает работу с холдами (все действия с холдами выполняются через операции)
        // TODO список изменяемых параметров будет дополняться (меняться может дата окончания оплаты, реквизиты для списания, референс во внешней системе, дополнительная информация)

        log.infof("requirementReview: %s", request);
        ReviewRequirementResponse response = new ReviewRequirementResponse();
        ReviewRequirementResultDto result = new ReviewRequirementResultDto();
        ReviewRequirementJournalDto journal = new ReviewRequirementJournalDto();
        response.reviewResult = result;
        response.reviewJournal = journal;

        if (request == null || request.requirement.id == null) {
            throw new RuntimeException("Requirement id is null");
        }

        Long requirementId = request.requirement.id;
        Requirement requirement = this.getRequirementById(requirementId);
        String currentBbpState = requirement.bbpState000StateCode;

        // проверяем состояние (требование, которое ожидает оплаты или оплачено частично, может быть пересмотрено)
        if ((!RmsConstants.PART_PAID_STATUS.equals(currentBbpState) && !RmsConstants.WAIT_PAY_STATUS.equals(currentBbpState)) ||
            (!RequirementStatus.WAIT.equals(requirement.state))) {
            throw new RuntimeException(String.format("Review is not allowed for Requirement (id=%s) in state (%s)", requirementId, requirement.state));
        }

        // сохраняем первоначальные значения для отката
        journal.requirementJournal = RequirementMapperUtils.fillRequirementJournal(requirement);

        if (request.priority != null) {
            // приоритет, меняется только дробная часть := "приоритет оплаты" из записи массива
            BigInteger integerPriority = requirement.priority.toBigInteger();
            BigDecimal decimalPriority = new BigDecimal(request.priority).divide(new BigDecimal("100"), 2, RoundingMode.UP);
            requirement.priority = new BigDecimal(integerPriority).add(decimalPriority);
        }

        if (request.amount != null) {
            // если суммы отличаются
            if (requirement.amount.compareTo(request.amount) != 0) {
                // невозможно просто уменьшить сумму требования, если по нему уже оплачена или списана более крупная сумма
                if (requirement.paidAmount.compareTo(request.amount) > 0) {
                    // пробуем выполнить возврат
                    BigDecimal refundAmount = requirement.paidAmount.subtract(request.amount);
                    log.infof("requirement.paidAmount = %s, request.amount = %s, refundAmount = %s", requirement.paidAmount, request.amount, refundAmount);

                    RefundOfRequirementsDto refundRequest = new RefundOfRequirementsDto();
                    refundRequest.refundAmount = refundAmount;
                    refundRequest.requirements = List.of(new ReferenceDto(requirementId, "/SYS/RMS/Requirement"));
                    RefundResponse refundResponse = paymentService.refundOfPayment(refundRequest);

                    log.infof("refundResponse:\n%s", refundResponse);
                    // журнал возврата платежа
                    journal.refundJournal = refundResponse.refundJournal;

                    // данные по суммам и состояниям поменялись
                    requirementDao.refresh(requirement);
                }
                requirement.amount = request.amount;
                requirement.unpaidAmount = requirement.amount.subtract(requirement.paidAmount);
                if (requirement.unpaidAmount.compareTo(BigDecimal.ZERO) == 0) {
                    requirement.state = RequirementStatus.PAID;
                }
            }
        }
        requirement.update();
        result.requirementInfo = RequirementMapperUtils.fillRequirementInfo(
            requirement.id,
            RequirementAction.SAVE,
            requirement.state,
            requirement.amount,
            requirement.paidAmount,
            requirement.priority,
            requirement.indicatorId
        );
        return response;
    }

    @Override
    public void requirementReviewUndo(ReviewRequirementJournalDto request) {
        log.infof("requirementReviewUndo: %s", request);
        if (request.refundJournal != null) {
            paymentService.refundOfPaymentUndo(request.refundJournal);
        }
        if (request.requirementJournal != null &&
            (request.refundJournal == null || (request.refundJournal.requirementJournal != null && request.refundJournal.requirementJournal.isEmpty()))) {
            restoreWithoutBbpCancelExecution(List.of(request.requirementJournal), null, null, null, null);
        }
    }

    @Override
    public void updateRequirementBbpStates(List<RequirementJournalDto> requirements, Map<String, BbpStateResult> processIdStateMap) {
            if (processIdStateMap != null && !processIdStateMap.isEmpty()) {
                log.infof("updateRequirementBbpStates requirements: %s \n processIdStateMap: %s", requirements, processIdStateMap);
                requirements.forEach(req -> {
                    String processId = req.bbpProcessId;
                    if (processIdStateMap.containsKey(processId)) {
                        Requirement requirement = this.getRequirementById(req.id);
                        BbpStateResult bbpState = processIdStateMap.get(processId);

                        requirement.bbpState000JournalId = String.valueOf(bbpState.journalId());
                        requirement.bbpState000ProcessId = bbpState.processId();
                        requirement.bbpState000StateCode = bbpState.stateCode();

                        requirement.update();
                    }
                });
            } else {
                log.infof("processIdStateMap is empty for requirements: %s", requirements);
            }
    }

    @Override
    public Requirement getRequirementById(Long id) {
        if (id == null) {
            throw new RuntimeException("Requirement id is null");
        }
        return requirementDao.findByIdOrThrow(id);
    }

    @Override
    public List<Requirement> getRequirementsByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return requirementDao.findActiveByIds(ids);
    }

    private List<Requirement> getRequirements(ReferenceDto requirement, ReferenceDto contract, ReferenceDto client) {
        // дата не требуется, нужно актуальное состояние в моменте
        if (requirement != null) {
            Requirement requirementEntity = this.getRequirementById(requirement.id);
            return List.of(requirementEntity);
        }
        if (contract != null || client != null) {
            String contractRef = contract != null ? contract.toString() : null;
            Long clientId = client != null ? client.id : null;
            List<Requirement> requirementList = requirementDao.findActiveByContractOrClient(contractRef, clientId);
            if (requirementList != null && !requirementList.isEmpty()) {
                return requirementList;
            } else {
                return null;
            }
        }
        return null;
    }


    // получение оплаченной суммы
    // предполагается что все требования в валюте договора
    @Override
    public BigDecimal getPaidAmount(ReferenceDto requirement, ReferenceDto contract, ReferenceDto client) {
        List<Requirement> requirementList = getRequirements(requirement, contract, client);
        if (requirementList != null && !requirementList.isEmpty()) {
            return requirementList.stream()
                .map(r -> r.paidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            return null;
        }
    }


    // получение оплаченной суммы в разбивке по расчетным категориям
    // предполагается что все требования в валюте договора
    @Override
    public RequirementAmountResponse getPaidAmounts(ReferenceDto requirement, ReferenceDto contract, ReferenceDto client) {
        RequirementAmountResponse response = new RequirementAmountResponse();
        List<Requirement> requirementList = getRequirements(requirement, contract, client);
        if (requirementList != null && !requirementList.isEmpty()) {
            Map<Long, List<Requirement>> groupByIndicator = requirementList.stream()
                .collect(Collectors.groupingBy(r -> r.indicatorId));
            for (Long indicatorId : groupByIndicator.keySet()) {
                AmountForIndicatorDto amountForIndicator = new AmountForIndicatorDto();
                amountForIndicator.indicator = new ReferenceDto(indicatorId, "/SYS/PROD/Indicator");
                amountForIndicator.amount = groupByIndicator.get(indicatorId).stream()
                    .map(r -> r.paidAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                response.amounts.add(amountForIndicator);
            }
        }
        return response;
    }


    // получение неоплаченной суммы
    @Override
    public BigDecimal getUnpaidAmount(ReferenceDto requirement, ReferenceDto contract, ReferenceDto client) {
        List<Requirement> requirementList = getRequirements(requirement, contract, client);
        if (requirementList != null && !requirementList.isEmpty()) {
            return requirementList.stream()
                .map(r -> r.unpaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            return null;
        }
    }

    // получение неоплаченной суммы в разбивке по расчетным категориям
    // предполагается что все требования в валюте договора
    @Override
    public RequirementAmountResponse getUnpaidAmounts(ReferenceDto requirement, ReferenceDto contract, ReferenceDto client) {
        RequirementAmountResponse response = new RequirementAmountResponse();
        List<Requirement> requirementList = getRequirements(requirement, contract, client);
        if (requirementList != null && !requirementList.isEmpty()) {
            Map<Long, List<Requirement>> groupByIndicator = requirementList.stream()
                .collect(Collectors.groupingBy(r -> r.indicatorId));
            for (Long indicatorId : groupByIndicator.keySet()) {
                AmountForIndicatorDto amountForIndicator = new AmountForIndicatorDto();
                amountForIndicator.indicator = new ReferenceDto(indicatorId, "/SYS/PROD/Indicator");
                amountForIndicator.amount = groupByIndicator.get(indicatorId).stream()
                    .map(r -> r.unpaidAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                response.amounts.add(amountForIndicator);
            }
        }
        return response;
    }


    // получение просроченного требования
    private Requirement getOverdueRequirement (ReferenceDto requirement, LocalDate businessDate) {
        // заданная дата больше даты окончания оплаты (или даты требования, если дата окончания оплаты не задана явно)
        // неоплаченная сумма больше нуля или фактическая дата оплаты больше заданной даты
        return requirementDao.findOverdueByRequirementIdAndBusinessDate(requirement.id, businessDate);
    }

    // получение признака просрочки на дату
    @Override
    public Boolean getIsOverdue(ReferenceDto requirement, LocalDate businessDate) {
        Requirement overdueRequirement = getOverdueRequirement(requirement, businessDate);
        return overdueRequirement != null;
    }

    // получение даты начала просрочки
    @Override
    public LocalDate getOverdueStartDate(ReferenceDto requirement, LocalDate businessDate) {
        Requirement overdueRequirement = getOverdueRequirement(requirement, businessDate);
        if (overdueRequirement != null) {
            // если требование на заданную дату просрочено
            // датой начала просрочки считается следующая дата после даты окончания оплаты
            // (или даты требования, если дата окончания оплаты не задана явно)
            // TODO ?? должны ли применяться календари ?
            LocalDate overdueStartDate;
            if (overdueRequirement.paymentEndDate != null) {
                overdueStartDate = overdueRequirement.paymentEndDate.plusDays(1);
            } else {
                overdueStartDate = overdueRequirement.date.plusDays(1);
            }
            return overdueStartDate;
        }
        // иначе дата начала просрочки пустая
        return null;
    }


    // получение даты окончания просрочки
    @Override
    public LocalDate getOverdueEndDate(ReferenceDto requirement, LocalDate businessDate) {
        Requirement overdueRequirement = getOverdueRequirement(requirement, businessDate);
        if (overdueRequirement != null) {
            // требование на заданную дату просрочено, но в будущем есть дата, на которую оно не просрочено
            // т.е. неоплаченная сумма требования должна быть нулевой, а фактическая дата оплаты и есть дата окончания просрочки
            if (overdueRequirement.actualPaymentDate != null && overdueRequirement.unpaidAmount.compareTo(BigDecimal.ZERO) == 0) {
                return overdueRequirement.actualPaymentDate;
            }
        }
        // иначе дата окончания просрочки пустая
        return null;
    }
}
