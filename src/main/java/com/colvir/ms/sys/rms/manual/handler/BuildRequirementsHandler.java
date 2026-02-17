package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.opr.api.step.runner.method.response.Substep;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.BuildRequirementsDto;
import com.colvir.ms.sys.rms.dto.BuildRequirementsJournalDto;
import com.colvir.ms.sys.rms.dto.BuildRequirementsResultDto;
import com.colvir.ms.sys.rms.dto.CreateRequirementDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.manual.constant.RmsConstants;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BuildRequirementsHandler extends AbstractStepRunnerHandler<BuildRequirementsDto, BuildRequirementsJournalDto, BuildRequirementsResultDto> {

    RequirementService requirementService;

    StepCreatorService stepCreatorService;

    @Inject
    public BuildRequirementsHandler(RequirementService requirementService,
                                    StepCreatorService stepCreatorService,
                                    Logger log) {
        super(StepsNames.SYS_RMS_BUILD_REQUIREMENTS, log);
        this.requirementService = requirementService;
        this.stepCreatorService = stepCreatorService;
    }

    @Override
    public AggregationResult<BuildRequirementsDto, BuildRequirementsJournalDto, BuildRequirementsResultDto> process(StepMethod.RequestItem.Request<BuildRequirementsDto, BuildRequirementsJournalDto> request) {
        BuildRequirementsJournalDto journal = request.getJournal();
        if (journal == null) {
            journal = new BuildRequirementsJournalDto();
        }
        BuildRequirementsDto properties = request.getProperties();
        BuildRequirementsResultDto result = new BuildRequirementsResultDto();

        final boolean isFirstRun = journal.isFirstRun();
        journal.setFirstRun(false);

        log.infof("rms-build-requirements process isFirstRun: %s properties:%n%s", isFirstRun, properties);
        if (properties.getPaymentData() == null || properties.getPaymentData().isEmpty()) {
            return new AggregationResult<>(properties, journal, result);
        }

        if (isFirstRun) {
            requirementService.checkBuildRequirements(properties);
            ObjectNode context = request.getContextMapper().getContext();
            List<Substep> subSteps = new ArrayList<>();
            for (RequirementStateInfoDto reqStateInfo : properties.getPaymentData()) {
                if (reqStateInfo != null && reqStateInfo.requirementId != null) {
                    String id = UUID.randomUUID().toString();
                    String stateField = RmsConstants.START_BASE_BUSINESS_PROCESS_STATE_FIELD_PREFIX + id;
                    context.set(stateField, ContextObjectMapper.get().createObjectNode());
                    subSteps.add(stepCreatorService.createSysBbpStartSubStep(stateField));
                    journal.getProcessStateIds().add(stateField);
                }
            }
            return new AggregationResult<>(journal, subSteps);
        }

        List<String> initialBppStates = journal.getProcessStateIds().stream()
            .map(stateField -> request.getContextMapper().getContext().at("/" + stateField + "/" + stateField))
            .filter(jn -> jn != null && jn.isTextual())
            .map(JsonNode::asText)
            .toList();
        if (initialBppStates.size() != properties.getPaymentData().size()) {
            throw new RuntimeException(String.format(
                "Created bpp states list size is not equal payment data size initialBppStates = %s, paymentData = %s",
                initialBppStates.size(), properties.getPaymentData().size()
            ));
        }
        List<CreateRequirementDto> createRequirements = new ArrayList<>(properties.getPaymentData().size());

        for (int i = 0; i < properties.getPaymentData().size(); i++) {
            CreateRequirementDto createRequirementDto = new CreateRequirementDto()
                .setBusinessDate(properties.getBusinessDate())
                .setContract(properties.getContract())
                .setPaymentData(properties.getPaymentData().get(i))
                .setInitialBbpState(initialBppStates.get(i))
                .setClient(properties.getClient())
                .setCurrency(properties.getCurrency());
            createRequirements.add(createRequirementDto);
        }

        List<RequirementStateInfoDto> requirements = requirementService.createRequirements(createRequirements);
        result.getRequirements().addAll(requirements);
        journal.getRequirementIdList().addAll(requirements.stream().map(req -> req.requirementId).toList());

        return new AggregationResult<>(properties, journal, result);
    }

    @Override
    public void undo(BuildRequirementsJournalDto journal) {
        log.infof("rms-build-requirements compensate:%n%s", journal);
        Optional.ofNullable(journal)
            .map(BuildRequirementsJournalDto::getRequirementIdList)
            .filter(req -> !req.isEmpty())
            .ifPresent(requirementService::deleteRequirements);
    }
}
