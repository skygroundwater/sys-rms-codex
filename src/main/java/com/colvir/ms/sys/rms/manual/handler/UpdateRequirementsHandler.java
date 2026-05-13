package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.opr.api.step.runner.method.response.Substep;
import com.colvir.ms.sys.opr.api.step.runner.method.response.SubstepParameter;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.JournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.dto.UpdateRequirementsDto;
import com.colvir.ms.sys.rms.dto.UpdateRequirementsResultDto;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.manual.service.impl.StepCreatorService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UpdateRequirementsHandler extends AbstractStepRunnerHandler<UpdateRequirementsDto, JournalDto, UpdateRequirementsResultDto>{

    StepCreatorService stepCreatorService;

    public UpdateRequirementsHandler(StepCreatorService stepCreatorService,
                                     Logger log) {
        super(StepsNames.SYS_RMS_UPDATE_REQUIREMENTS, log);
        this.stepCreatorService = stepCreatorService;
    }

    private static final String UPDATE_REQUIREMENT_RESULT_PATH = "__step_singleRequirementUpdate_";

    @Override
    public void validateProperties(UpdateRequirementsDto properties) {
        if (properties.paymentData != null && !properties.paymentData.isEmpty()) {
            List<RequirementStateInfoDto> incorrectPaymentData = properties.paymentData.stream()
                .filter(r -> !RequirementAction.UPDATE.equals(r.action) || r.requirementId == null)
                .toList();
            if (!incorrectPaymentData.isEmpty()) {
                throw new RuntimeException(String.format("Incorrect PaymentData: %s", incorrectPaymentData));
            }
        }
    }

    @Override
    public AggregationResult<UpdateRequirementsDto, JournalDto, UpdateRequirementsResultDto> process(StepMethod.RequestItem.Request<UpdateRequirementsDto, JournalDto> request) {
        JournalDto journal = request.getJournal();
        UpdateRequirementsDto properties = request.getProperties();

        if (journal.isFirstRun) {
            journal.isFirstRun = false;

            if (properties.paymentData == null || properties.paymentData.isEmpty()) {
                return new AggregationResult<>(journal, List.of());
            }

            List<Substep> subSteps = properties.paymentData.stream()
                .map(pd -> stepCreatorService.createSubStep(
                    StepsNames.SYS_RMS_UPDATE_SINGLE_REQUIREMENT,
                    Map.of(
                        "paymentData", SubstepParameter.value(pd),
                        "businessDate", SubstepParameter.value(properties.businessDate),
                        "contract", SubstepParameter.value(properties.contract),
                        "requirement", SubstepParameter.result(UPDATE_REQUIREMENT_RESULT_PATH + pd.requirementId)
                    ))
                )
                .toList();
            return new AggregationResult<>(journal, subSteps);
        } else {

            List<RequirementStateInfoDto> subStepResults = properties.paymentData.stream()
                .map(pd -> request.getContextMapper().findNode(UPDATE_REQUIREMENT_RESULT_PATH + pd.requirementId))
                .filter(r -> r != null && !r.isMissingNode())
                .map(Unchecked.function(jn -> ContextObjectMapper.get().treeToValue(jn, RequirementStateInfoDto.class)))
                .toList();

            UpdateRequirementsResultDto result = new UpdateRequirementsResultDto();
            result.requirements.addAll(subStepResults);

            return new AggregationResult<>(journal, result, List.of());
        }
    }

}
