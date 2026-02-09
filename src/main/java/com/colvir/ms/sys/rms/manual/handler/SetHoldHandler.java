package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.opr.api.step.runner.method.response.Substep;
import com.colvir.ms.sys.opr.api.step.runner.method.response.SubstepParameter;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RequirementHoldInfoDto;
import com.colvir.ms.sys.rms.dto.SetHoldDto;
import com.colvir.ms.sys.rms.dto.SetHoldJournalDto;
import com.colvir.ms.sys.rms.dto.SetHoldResultDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.RequirementHold;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.colvir.ms.sys.rms.manual.util.RequirementMapperUtils;
import com.colvir.ms.sys.rms.manual.util.RmsConstants;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import com.colvir.ms.sys.rms.manual.util.SystemParameterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class SetHoldHandler extends AbstractStepRunnerHandler<SetHoldDto, SetHoldJournalDto, SetHoldResultDto> {

    SystemParameterService systemParameterService;

    RequirementService requirementService;

    StepCreatorService stepCreatorService;

    @Inject
    public SetHoldHandler(SystemParameterService systemParameterService,
                          RequirementService requirementService,
                          Logger log) {
        super(StepsNames.SYS_RMS_GROUP_MEMBER_ADD, log);
        this.systemParameterService = systemParameterService;
        this.requirementService = requirementService;
    }

    @Override
    public void validateProperties(SetHoldDto properties) {
        if (properties.amount == null || BigDecimal.ZERO.compareTo(properties.amount) == 0) {
            throw new RuntimeException("setHoldRunner: 'amount' value should not be 'null' or zero");
        }
        if (properties.currency == null || properties.currency.id == null) {
            throw new RuntimeException("setHoldRunner: 'currency' value should not be empty");
        }
        if (properties.client == null || properties.client.id == null) {
            throw new RuntimeException("setHoldRunner: 'client' value should not be empty");
        }
        if (properties.requirement == null || properties.requirement.id == null) {
            throw new RuntimeException("setHoldRunner: 'requirement' value should not be empty");
        }
    }

    @Override
    public AggregationResult<SetHoldDto, SetHoldJournalDto, SetHoldResultDto> process(StepMethod.RequestItem.Request<SetHoldDto, SetHoldJournalDto> request) {
        SetHoldJournalDto journal = request.getJournal();
        SetHoldDto properties = request.getProperties();
        SetHoldResultDto result = new SetHoldResultDto();

        journal.requirementId = properties.requirement.id;
        if (journal.isFirstRun) {
            journal.isFirstRun = false;
            // при первом вызове формируем сразу все суб-шаги
            if (properties.withdrawalRules == null || properties.withdrawalRules.isEmpty()) {
                log.infof("--- rms-set-hold --- return on empty rules");
                return new AggregationResult<>(journal, result, List.of());
            }

            Requirement requirement = requirementService.getRequirementById(properties.requirement.id);

            if (!Boolean.TRUE.equals(requirement.isContractBound) && !Boolean.TRUE.equals(requirement.isHolding)) {
                log.infof("--- rms-set-hold --- return on isHolding=false");
                // требование не связано с договором и не установлен признак "холдировать средства"
                return new AggregationResult<>(journal, result, List.of());
            }

            // берем только целую часть приоритета (т.е.законодательно установленную)
            BigInteger priority = RequirementMapperUtils.getPriorityInteger(requirement.priority);
            ReferenceDto holdType;
            if (properties.holdType == null || properties.holdType.id == null) {
                Long holdTypeId = systemParameterService.getHoldType();
                holdType = new ReferenceDto(holdTypeId, "/SYS/ACC/HoldType");
            } else {
                holdType = properties.holdType;
            }

            List<Substep> subSteps = new ArrayList<>();

            for (var rule : properties.withdrawalRules) {
                if (rule.withdrawalType == null || rule.withdrawalType.id == null) {
                    throw new RuntimeException("setHoldRunner: 'withdrawalType.id' value should not be empty");
                }
                // номера счетов будут вычисляться по правилам и вариантам списания в вызываемых раннерах
                subSteps.add(
                    stepCreatorService
                        .createSubStep(
                            StepsNames.SYS_RMS_SET_HOLD_WDRW,
                            Map.of(
                                "withdrawalRule", SubstepParameter.value(rule),
                                "withdrawalType", SubstepParameter.value(rule.withdrawalType),
                                "client", SubstepParameter.value(properties.client),
                                "contract", SubstepParameter.value(properties.contract),
                                "amount", SubstepParameter.value(properties.amount),
                                "currency", SubstepParameter.value(properties.currency),
                                "priority", SubstepParameter.value(priority),
                                "holdType", SubstepParameter.value(holdType),
                                "holdResult", SubstepParameter.result(RmsConstants.HOLD_RESULT_PATH + rule.withdrawalType.id)
                            )
                        )
                );
            }
            return new AggregationResult<>(journal, subSteps);

        } else {
            // отрабатывает один раз после вызова всех суб шагов
            if (properties.withdrawalRules == null || properties.withdrawalRules.isEmpty()) {
                throw new RuntimeException("setHoldRunner: 'withdrawalRules' has been changed since last iteration");
            }
            // обработка результата предыдущих суб-шагов
            for (var rule : properties.withdrawalRules) {
                JsonNode holdResultNode = request.getContextMapper().findNode(RmsConstants.HOLD_RESULT_PATH + rule.withdrawalType.id);
                try {
                    if (holdResultNode != null && !holdResultNode.isMissingNode()) {
                        journal.holdResults.add(
                            ContextObjectMapper.get().treeToValue(holdResultNode, RequirementHoldInfoDto.class)
                        );
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            // сохраняем созданные холды в требование и пишем результат в журнал
            journal.createdHolds.addAll(
                createRequirementHolds(properties.requirement.id, journal.holdResults)
            );
            result.holdResult.addAll(journal.holdResults);

            log.infof("--- rms-set-hold --- result: %s", result);
            return new AggregationResult<>(properties, journal, result);
        }
    }

    @Override
    public void undo(SetHoldJournalDto journal) {
        if (journal.createdHolds != null && !journal.createdHolds.isEmpty()) {
            deleteRequirementHolds (journal.requirementId, journal.createdHolds);
        }
    }

    private Set<Long> createRequirementHolds (Long requirementId, List<RequirementHoldInfoDto> holdInfoList) {
        Set<Long> createdHolds = new HashSet<>();
        if (holdInfoList != null && !holdInfoList.isEmpty()) {
            Requirement requirement = Requirement.findById(requirementId);
            if (requirement != null && !Boolean.TRUE.equals(requirement.isDeleted)) {
                for (var holdInfo : holdInfoList) {
                    RequirementHold requirementHold = new RequirementHold();
                    requirementHold.accountNumber = holdInfo.accountNumber;
                    requirementHold.amount = holdInfo.amount;
                    if (holdInfo.withdrawalType.id != null) {
                        requirementHold.withdrawalTypeId = holdInfo.withdrawalType.id;
                    }
                    if (holdInfo.currency.id != null) {
                        requirementHold.currencyId = holdInfo.currency.id;
                    }
                    if (holdInfo.hold.id != null) {
                        requirementHold.holdId = holdInfo.hold.id;
                    }
                    requirementHold.reference = holdInfo.reference;
                    requirementHold.requirementOfAssignedHolds = Requirement.getEntityManager().getReference(Requirement.class, requirementId);
                    requirementHold.persist();
                    requirement.assignedHolds.add(requirementHold);
                    createdHolds.add(requirementHold.id);
                }
                requirement.update();
            }
        }
        return createdHolds;
    }

    private void deleteRequirementHolds (Long requirementId, Set<Long> holdIdList) {
        if (holdIdList != null && !holdIdList.isEmpty()) {
            Requirement requirement = Requirement.findById(requirementId);
            if (requirement != null && !Boolean.TRUE.equals(requirement.isDeleted)) {
                Map<Long, RequirementHold> holdMap = requirement.assignedHolds.stream()
                    .filter(h -> !Boolean.TRUE.equals(h.isDeleted))
                    .collect(Collectors.toMap(h -> h.id, Function.identity()));
                for (var holdId : holdIdList) {
                    if (holdMap.containsKey(holdId)) {
                        RequirementHold requirementHold = holdMap.get(holdId);
                        requirementHold.isDeleted = true;
                        requirementHold.update();
                    }
                }
                requirement.update();
            }
        }
    }

}
