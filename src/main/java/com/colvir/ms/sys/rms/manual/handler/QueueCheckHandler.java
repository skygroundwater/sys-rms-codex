package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.CheckQueueDto;
import com.colvir.ms.sys.rms.dto.JournalDto;
import com.colvir.ms.sys.rms.dto.QueueCheckDto;
import com.colvir.ms.sys.rms.dto.QueueCheckResultDto;
import com.colvir.ms.sys.rms.dto.WithdrawalRuleDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.SysAcc000WithdrawalRule;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.manual.domain.ClientAccountSelectionType;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.util.RequirementMapperUtils;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.lang3.BooleanUtils;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.colvir.ms.sys.acc.generated.domain.enumeration.ClientAccountSelectionType.SPECIFIED;

@ApplicationScoped
public class QueueCheckHandler extends AbstractStepRunnerHandler<QueueCheckDto, JournalDto, QueueCheckResultDto> {

    RequirementService requirementService;

    @Inject
    public QueueCheckHandler(RequirementService requirementService,
                             Logger log) {
        super(StepsNames.SYS_RMS_DISTRIBUTE_PAID_AMOUNTS, log);
        this.requirementService = requirementService;
    }

    @Override
    public void validateProperties(QueueCheckDto properties) {
        if (properties.requirement == null || properties.requirement.id == null) {
            throw new RuntimeException("queueCheckRunner: 'requirement' value should not be empty");
        }
    }

    @Override
    public AggregationResult<QueueCheckDto, JournalDto, QueueCheckResultDto> process(StepMethod.RequestItem.Request<QueueCheckDto, JournalDto> request) {
        // проверяет наличие неоплаченных требований с лучшим приоритетом
        // рассматриваются требования с тем же вариантом списания и тем же счетом
        // если такие есть, возвращает true, в противном случае false
        QueueCheckResultDto result = new QueueCheckResultDto();
        QueueCheckDto properties = request.getProperties();
        CheckQueueDto checkRequest = new CheckQueueDto();
        checkRequest.requirementId = properties.requirement.id;
        checkRequest.withdrawalRules = properties.withdrawalRules;
        result.isFound = this.checkRequirementQueue(checkRequest);

        return new AggregationResult<>(request.getJournal(), result, List.of());
    }

    public Boolean checkRequirementQueue(CheckQueueDto request) {
        Requirement requirement = requirementService.getRequirementById(request.requirementId);
        if (request.withdrawalRules == null || request.withdrawalRules.isEmpty()) {
            return false;
        }

        // "с указанных счетов"
        WithdrawalRuleDto withdrawalRule = request.withdrawalRules.stream()
            .filter(r -> ClientAccountSelectionType.SPECIFIED.equals(r.accountSelectionType))
            .findFirst()
            .orElse(null);

        if (withdrawalRule == null) {
            return false;
        }
        if (withdrawalRule.accountNumbers == null || withdrawalRule.accountNumbers.isEmpty()) {
            throw new RuntimeException(String.format("'accountNumbers' for WithdrawalRule (id = %s) are not specified", withdrawalRule.id));
        }

        // ищем правила списания с указанным вариантом списания и списком счетов
        List<String> accountNumbers = withdrawalRule.accountNumbers;
        Long withdrawalTypeId = withdrawalRule.withdrawalType.id;

        // получаем список требований с которыми связаны правила
        Map<String, Object> params = new HashMap<>();
        params.put("accountNumbers", accountNumbers);
        params.put("withdrawalTypeId", withdrawalTypeId);
        params.put("accountSelectionType", SPECIFIED);
        String query = """
            select distinct wr from SysAcc000WithdrawalRule wr
            join fetch wr.accountNumbers an where
            an.value in (:accountNumbers)
            and wr.withdrawalTypeId = :withdrawalTypeId
            and wr.accountSelectionType = :accountSelectionType
            and (wr.isDeleted is null or wr.isDeleted = false)""";
        List<Long> requirementIdList = SysAcc000WithdrawalRule.list(query, params)
            .stream()
            .map(wr -> ((SysAcc000WithdrawalRule) wr))
            .filter(wr -> BooleanUtils.isNotTrue(wr.requirementOfWithdrawalRules.isDeleted))
            .map(wr -> wr.requirementOfWithdrawalRules.id)
            .filter(id -> !Objects.equals(id, request.requirementId)).toList();
        log.infof("--------- requirementIdList: \r\n" + requirementIdList);

        if (requirementIdList.isEmpty()) {
            return false;
        }

        // читаем требования по полученному списку
        params = new HashMap<>();
        params.put("requirementIdList", requirementIdList);
        params.put("state", RequirementStatus.WAIT);
        params.put("priority", requirement.priority != null ? requirement.priority : BigDecimal.valueOf(Integer.MAX_VALUE));
        query = "select r from Requirement r where " +
            " r.id in (:requirementIdList) " +
            " and r.state = :state " +
            " and (r.isDeleted is null or r.isDeleted = false) " +
            " and r.priority < :priority";
        List<Requirement> requirementList = Requirement.list(query, params);

        // проверяется только целая часть приоритета (т.е.законодательно установленная), дробная часть приоритета игнорируется
        BigInteger requirementPriority = RequirementMapperUtils.getPriorityInteger(requirement.priority);
        List<Requirement> queueRequirementList = requirementList.stream()
            .filter(r -> requirementPriority.compareTo(RequirementMapperUtils.getPriorityInteger(r.priority)) > 0)
            .toList();

        return !queueRequirementList.isEmpty();
    }

}
