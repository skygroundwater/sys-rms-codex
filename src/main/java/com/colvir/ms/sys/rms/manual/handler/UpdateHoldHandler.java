package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.opr.api.step.runner.method.response.Substep;
import com.colvir.ms.sys.opr.api.step.runner.method.response.SubstepParameter;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RequirementHoldInfoDto;
import com.colvir.ms.sys.rms.dto.RequirementHoldJournalDto;
import com.colvir.ms.sys.rms.dto.UpdateHoldDto;
import com.colvir.ms.sys.rms.dto.UpdateHoldJournalDto;
import com.colvir.ms.sys.rms.dto.UpdateHoldResultDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.RequirementHold;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementHoldState;
import com.colvir.ms.sys.rms.manual.constant.RmsConstants;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class UpdateHoldHandler extends AbstractStepRunnerHandler<UpdateHoldDto, UpdateHoldJournalDto, UpdateHoldResultDto> {

    RequirementService requirementService;

    StepCreatorService stepCreatorService;

    @Inject
    public UpdateHoldHandler(RequirementService requirementService,
                             Logger log) {
        super(StepsNames.SYS_RMS_GROUP_MEMBER_ADD, log);
        this.requirementService = requirementService;
    }

    @Override
    public void validateProperties(UpdateHoldDto properties) {
        if (properties.requirement == null || properties.requirement.id == null) {
            throw new RuntimeException("updateHoldRunner: 'requirement' value should not be empty");
        }
    }

    @Override
    public AggregationResult<UpdateHoldDto, UpdateHoldJournalDto, UpdateHoldResultDto> process(StepMethod.RequestItem.Request<UpdateHoldDto, UpdateHoldJournalDto> request) {
        UpdateHoldJournalDto journal = request.getJournal();
        UpdateHoldDto properties = request.getProperties();
        UpdateHoldResultDto result = new UpdateHoldResultDto();

        journal.requirementId = properties.requirement.id;

        if (journal.isFirstRun) {
            journal.isFirstRun = false;
            // при первом вызове формируем сразу все суб-шаги
            Requirement requirement = requirementService.getRequirementById(properties.requirement.id);

            List<RequirementHold> assignedHolds = requirement.assignedHolds.stream()
                .filter(h -> !Boolean.TRUE.equals(h.isDeleted))
                .toList();

            if (assignedHolds.isEmpty()) {
                log.infof("--- rms-update-hold --- return on empty requirement holds");
                return new AggregationResult<>(journal, result, List.of());
            }

            List<Substep> subSteps = new ArrayList<>();

            for (RequirementHold hold : assignedHolds) {
                if (hold.withdrawalTypeId == null) {
                    throw new RuntimeException("updateHoldRunner: 'withdrawalTypeId' value should not be empty");
                }
                subSteps.add(
                    stepCreatorService
                        .createSubStep(
                            StepsNames.SYS_RMS_SET_HOLD_WDRW,
                            Map.of(
                                "withdrawalType", SubstepParameter.value(new ReferenceDto(hold.withdrawalTypeId, RmsConstants.SYS_ACC_WITHDRAWAL_TYPE)),
                                "reference", SubstepParameter.value(hold.reference),
                                "amount", SubstepParameter.value(properties.amount),
                                "currency", SubstepParameter.value(properties.currency),
                                "holdResult", SubstepParameter.result(RmsConstants.HOLD_RESULT_PATH + hold.withdrawalTypeId)
                            )
                        )
                );
            }
            return new AggregationResult<>(journal, subSteps);

        } else {
            // отрабатывает один раз после вызова всех суб шагов
            Requirement requirement = requirementService.getRequirementById(properties.requirement.id);
            List<RequirementHold> assignedHolds = requirement.assignedHolds.stream()
                .filter(h -> !Boolean.TRUE.equals(h.isDeleted))
                .toList();

            for (RequirementHold hold : assignedHolds) {
                RequirementHoldJournalDto holdJournal = new RequirementHoldJournalDto();
                holdJournal.id = hold.id;
                holdJournal.requirementId = requirement.id;
                holdJournal.withdrawalTypeId = hold.withdrawalTypeId;
                holdJournal.accountNumber = hold.accountNumber;
                holdJournal.amount = hold.amount;
                holdJournal.currencyId = hold.currencyId;
                holdJournal.holdId = hold.holdId;
                holdJournal.reference = hold.reference;
                holdJournal.isDeleted = hold.isDeleted;
                journal.holdJournal.add(holdJournal);

                // обработка результата предыдущих суб-шагов
                JsonNode holdResultNode = request.getContextMapper().findNode(RmsConstants.HOLD_RESULT_PATH + hold.withdrawalTypeId);
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
            updateRequirementHolds(requirement, journal.holdResults);
            result.holdResult.addAll(journal.holdResults);
            log.infof("--- rms-set-hold --- result: %s", result);
            return new AggregationResult<>(journal, result, List.of());
        }
    }

    @Override
    public void undo(UpdateHoldJournalDto journal) {
        if (journal.holdJournal != null && !journal.holdJournal.isEmpty()) {
            restoreRequirementHolds(journal.requirementId, journal.holdJournal);
        }
    }

    protected void updateRequirementHolds(Requirement requirement, List<RequirementHoldInfoDto> holdInfoList) {
        if (holdInfoList != null && !holdInfoList.isEmpty()) {

            Map<Long, RequirementHold> assignedHoldsMap = requirement.assignedHolds.stream()
                .filter(h -> !Boolean.TRUE.equals(h.isDeleted))
                .collect(Collectors.toMap(h -> h.withdrawalTypeId, Function.identity()));

            for (RequirementHoldInfoDto holdInfo : holdInfoList) {
                RequirementHold requirementHold = assignedHoldsMap.getOrDefault(holdInfo.withdrawalType.id, null);
                if (requirementHold != null) {
                    if (RequirementHoldState.UPDATED.equals(holdInfo.holdState)) {
                        requirementHold.amount = holdInfo.amount;
                        if (holdInfo.currency.id != null) {
                            requirementHold.currencyId = holdInfo.currency.id;
                        }
                    } else if (RequirementHoldState.DELETED.equals(holdInfo.holdState)) {
                        requirementHold.isDeleted = true;
                        requirementHold.amount = holdInfo.amount;
                    }
                    requirementHold.update();
                }
            }
            requirement.update();
        }
    }

    protected void restoreRequirementHolds(Long requirementId, List<RequirementHoldJournalDto> holdJournal) {
        if (holdJournal != null && !holdJournal.isEmpty()) {
            Requirement requirement = requirementService.getRequirementById(requirementId);
            Map<Long, RequirementHold> assignedHoldsMap = requirement.assignedHolds.stream()
                .collect(Collectors.toMap(h -> h.id, Function.identity()));
            for (var journalHold : holdJournal) {
                if (assignedHoldsMap.containsKey(journalHold.id)) {
                    RequirementHold requirementHold = assignedHoldsMap.get(journalHold.id);
                    requirementHold.amount = journalHold.amount;
                    requirementHold.currencyId = journalHold.currencyId;
                    requirementHold.isDeleted = journalHold.isDeleted;
                    requirementHold.update();
                }
            requirement.update();
            }
        }
    }

}
