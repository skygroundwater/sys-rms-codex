package com.colvir.ms.sys.rms.manual.reactive.service.impl;

import com.colvir.ms.common.Constants;
import com.colvir.ms.sys.rms.dto.BaseProcessResultDto;
import com.colvir.ms.sys.rms.dto.BuildRequirementsDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementTypeDTO;
import com.colvir.ms.sys.rms.generated.service.mapper.RequirementTypeMapper;
import com.colvir.ms.sys.rms.manual.constant.RmsConstants;
import com.colvir.ms.sys.rms.manual.reactive.dao.RequirementReactiveDao;
import com.colvir.ms.sys.rms.manual.reactive.service.RequirementReactiveService;
import com.colvir.ms.sys.rms.manual.service.RequirementRouterService;
import com.colvir.ms.sys.rms.manual.service.RequirementTypeService;
import com.colvir.ms.sys.rms.manual.service.impl.SystemParameterService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.colvir.ms.sys.rms.manual.util.RequirementMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class RequirementReactiveServiceImpl implements RequirementReactiveService {

    @Inject
    Logger log;

    @Inject
    RequirementReactiveDao requirementReactiveDao;

    @Inject
    RequirementTypeService requirementTypeService;

    @Inject
    RequirementRouterService requirementRouterService;

    @Inject
    SystemParameterService systemParameterService;

    @Inject
    RequirementTypeMapper requirementTypeMapper;

    public static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.LOCAL_DATE_FORMAT);

    @Override
    public Uni<Void> checkBuildRequirements(BuildRequirementsDto request) {
        return Uni.createFrom().item(() -> validateBuildRequirements(request))
            .chain(newRequirementIds -> {
                if (newRequirementIds.isEmpty()) {
                    return Uni.createFrom().voidItem();
                }
                return requirementReactiveDao.countByIds(newRequirementIds)
                    .invoke(count -> {
                        if (count > 0) {
                            throw new RuntimeException("Some of new requirements already exist");
                        }
                    })
                    .replaceWithVoid();
            });
    }

    @Override
    public Uni<List<RequirementStateInfoDto>> createRequirements(BuildRequirementsDto request, List<String> initialBbpStates) {
        if (request == null || request.getPaymentData() == null || request.getPaymentData().isEmpty()) {
            return Uni.createFrom().item(List.of());
        }

        List<RequirementStateInfoDto> paymentData = request.getPaymentData();

        if (initialBbpStates == null || initialBbpStates.size() != paymentData.size()) {
            return Uni.createFrom().failure(new RuntimeException(String.format(
                "Created bbp states list size is not equal payment data size initialBbpStates = %s, paymentData = %s",
                initialBbpStates == null ? null : initialBbpStates.size(),
                paymentData.size()
            )));
        }

        return getSystemLocale()
            .chain(systemLocale -> Multi.createFrom().range(0, paymentData.size())
                .onItem().transformToUniAndMerge(index -> buildRequirement(
                    index,
                    paymentData.get(index),
                    initialBbpStates.get(index),
                    request,
                    systemLocale
                ))
                .collect().asList())
            .chain(buildResults -> {
                List<BuildRequirementResult> orderedBuildResults = buildResults.stream()
                    .sorted(Comparator.comparingInt(BuildRequirementResult::index))
                    .toList();
                List<Requirement> builtRequirements = orderedBuildResults.stream()
                    .map(BuildRequirementResult::requirement)
                    .toList();
                List<RequirementStateInfoDto> result = orderedBuildResults.stream()
                    .map(BuildRequirementResult::stateInfo)
                    .toList();

                return requirementReactiveDao.bulkInsert(builtRequirements)
                    .replaceWith(result);
            });
    }

    @Override
    public Uni<Void> deleteRequirements(List<Long> requirementIdList) {
        return Uni.createFrom()
            .item(() -> {
                try {
                    List<ObjectNode> response = requirementRouterService.deleteRequirements(requirementIdList);
                    log.infof("Delete requirements modifyResponse: \r\n" + response);
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(String.format("Error while deleting Requirements ids= %s: %s", requirementIdList, e));
                }
            })
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
            .replaceWithVoid();
    }

    private List<Long> validateBuildRequirements(BuildRequirementsDto request) {
        if (request.getContract() == null || request.getContract().id == null) {
            throw new RuntimeException("Contract is null or does not contain id");
        }
        if (request.getCurrency() == null || request.getCurrency().id == null) {
            throw new RuntimeException("Currency is null or does not contain id");
        }
        if (request.getClient() == null || request.getClient().id == null) {
            throw new RuntimeException("Client is null or does not contain id");
        }

        List<Long> newRequirementIds = new ArrayList<>();

        for (RequirementStateInfoDto r : request.getPaymentData()) {
            BigDecimal amount = Objects.requireNonNullElse(r.amount, BigDecimal.ZERO);
            BigDecimal payedAmount = Objects.requireNonNullElse(r.payedAmount, BigDecimal.ZERO);

            boolean incorrect =
                !RequirementAction.CREATE.equals(r.action) ||
                    !RequirementStatus.NONEXISTENT.equals(r.status) ||
                    amount.signum() <= 0 ||
                    payedAmount.signum() != 0;

            if (incorrect) {
                throw new RuntimeException("Incorrect PaymentData: " + r);
            }

            if (r.requirementId != null) {
                newRequirementIds.add(r.requirementId);
            }
        }

        return newRequirementIds;
    }

    private Uni<Long> getSystemLocale() {
        return Uni.createFrom()
            .item(() -> systemParameterService.getSystemLocale(RmsConstants.SYSTEM_LOCALE_PARAM))
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    private Uni<BuildRequirementResult> buildRequirement(
        Integer index,
        RequirementStateInfoDto paymentData,
        String initialBbpState,
        BuildRequirementsDto request,
        Long systemLocale) {
        return Uni.createFrom()
            .item(() -> {
                Requirement requirement = new Requirement();

                requirement.id = paymentData.requirementId;
                requirement.state = RequirementStatus.WAIT;
                requirement.amount = paymentData.amount;
                requirement.unpaidAmount = paymentData.amount;
                requirement.paidAmount = BigDecimal.ZERO;
                requirement.writeOffAmount = BigDecimal.ZERO;
                requirement.currencyId = request.getCurrency().id;
                requirement.clientId = request.getClient().id;
                requirement.indicatorId = paymentData.indicator.id;
                requirement.date = request.getBusinessDate();
                requirement.startPaymentDate = request.getBusinessDate();
                requirement.paymentEndDate = request.getBusinessDate();
                requirement.isContractBound = true;
                requirement.baseDocument = request.getContract().toString();
                fillBbpState(requirement, initialBbpState);
                RequirementTypeDTO requirementTypeDTO = requirementTypeService.getRequirementType(paymentData.indicator.indicatorDescr, systemLocale);
                requirement.priority = calculatePriority(paymentData, requirementTypeDTO);
                requirement.requirementType = requirementTypeMapper.toEntity(requirementTypeDTO);

                return new BuildRequirementResult(
                    index,
                    requirement,
                    RequirementMapperUtils.mapRequirementStateInfoDto(
                        paymentData,
                        requirement.id,
                        RequirementAction.SAVE,
                        RequirementStatus.WAIT
                    )
                );
            })
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    private void fillBbpState(Requirement requirement, String initialBbpState) {
        try {
            BaseProcessResultDto bbpState = ContextObjectMapper.get()
                .readValue(initialBbpState, BaseProcessResultDto.class);

            if (bbpState.stateCode == null || bbpState.stateCode.isBlank()) {
                JsonNode root = ContextObjectMapper.get().readTree(initialBbpState);
                requirement.bbpState000StateCode = root.path("state").asText("wait_pay");
            } else {
                requirement.bbpState000StateCode = bbpState.stateCode;
            }

            requirement.bbpState000ProcessId = bbpState.processId;
            requirement.bbpState000JournalId = String.valueOf(bbpState.journalId);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                String.format("Не удалось прочесть состояние бизнес процесса. bbState=%s", initialBbpState),
                e
            );
        }
    }

    private BigDecimal calculatePriority(RequirementStateInfoDto paymentData, RequirementTypeDTO requirementTypeDTO) {
        BigDecimal integerPriority = BigDecimal.ZERO;

        BigDecimal decimalPriority = new BigDecimal(paymentData.priority)
            .divide(new BigDecimal("100"), 2, RoundingMode.UP).add(BigDecimal.valueOf(requirementTypeDTO.priority));

        return integerPriority.add(decimalPriority);
    }

    private record BuildRequirementResult(Integer index, Requirement requirement, RequirementStateInfoDto stateInfo) {}
}
