package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.opr.api.step.runner.method.response.Substep;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.BuildRequirementsDto;
import com.colvir.ms.sys.rms.dto.BuildRequirementsJournalDto;
import com.colvir.ms.sys.rms.dto.BuildRequirementsResultDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.dto.StartBbpRunnerProperties;
import com.colvir.ms.sys.rms.manual.constant.RmsConstants;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.service.RequirementService;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

            String batchResultField = RmsConstants.START_BASE_BUSINESS_PROCESS_STATE_FIELD_PREFIX;

            Map<String, StartBbpRunnerProperties> startDataProperties = new LinkedHashMap<>();

            List<Substep> subSteps = new ArrayList<>();
            for (RequirementStateInfoDto reqStateInfo : properties.getPaymentData()) {
                if (reqStateInfo != null && reqStateInfo.requirementId != null) {
                    String requirementId = reqStateInfo.requirementId.toString();
                    startDataProperties.put(requirementId, new StartBbpRunnerProperties(null, null, null, null, true));
                    journal.getProcessStateIds().add(requirementId);
                }
            }
            subSteps.add(stepCreatorService.createSysBbpBatchStartSubStep(batchResultField, startDataProperties));
            return new AggregationResult<>(journal, subSteps);
        }

        JsonNode batchResultNode = request.getContextMapper()
            .getContext()
            .at("/" + RmsConstants.START_BASE_BUSINESS_PROCESS_STATE_FIELD_PREFIX);

        if (batchResultNode.isMissingNode() || batchResultNode.isNull() || !batchResultNode.isObject()) {
            throw new RuntimeException(String.format("Created batch bpp state is missing or invalid by field = %s", RmsConstants.START_BASE_BUSINESS_PROCESS_STATE_FIELD_PREFIX));
        }

        List<String> initialBppStates = journal.getProcessStateIds().stream()
            .map(requirementId -> batchResultNode.at("/" + requirementId))
            .filter(node -> !node.isMissingNode() && !node.isNull() && node.isObject())
            .map(JsonNode::toString)
            .toList();

        List<RequirementStateInfoDto> requirements = requirementService.createRequirements(properties, initialBppStates);
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
