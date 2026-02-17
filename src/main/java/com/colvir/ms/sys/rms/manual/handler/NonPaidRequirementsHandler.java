package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.JournalDto;
import com.colvir.ms.sys.rms.dto.NonPaidRequirementsDto;
import com.colvir.ms.sys.rms.dto.NonPaidRequirementsResultDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.dao.RequirementDao;
import com.colvir.ms.sys.rms.manual.service.RequirementRouterService;
import com.colvir.ms.sys.rms.manual.util.RequirementMapperUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class NonPaidRequirementsHandler extends AbstractStepRunnerHandler<NonPaidRequirementsDto, JournalDto, NonPaidRequirementsResultDto> {

    RequirementRouterService requirementRouterService;

    RequirementDao requirementDao;

    @Inject
    public NonPaidRequirementsHandler(RequirementRouterService requirementRouterService,
                                      RequirementDao requirementDao,
                                      Logger log) {
        super(StepsNames.SYS_RMS_GET_NON_PAID, log);
        this.requirementRouterService = requirementRouterService;
        this.requirementDao = requirementDao;
    }

    @Override
    public void validateProperties(NonPaidRequirementsDto properties) {
        if (properties.contract == null) {
            throw new RuntimeException("Contract value should not be empty");
        }
        if (properties.businessDate == null) {
            throw new RuntimeException("BusinessDate value should not be empty");
        }
    }

    @Override
    public AggregationResult<NonPaidRequirementsDto, JournalDto, NonPaidRequirementsResultDto> process(StepMethod.RequestItem.Request<NonPaidRequirementsDto, JournalDto> request) {
        NonPaidRequirementsResultDto result = new NonPaidRequirementsResultDto();
        NonPaidRequirementsDto properties = request.getProperties();

        List<Requirement> requirementList = requirementDao.findWaitByBaseDocumentAndBusinessDate(
            properties.contract.toString(),
            properties.businessDate
        );

        result.unpaidAmount = requirementList.stream()
            .map(r -> r.unpaidAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        result.requirements.addAll(requirementList.stream().map(
            requirement -> RequirementMapperUtils.fillRequirementInfo(requirement, RequirementAction.SAVE, requirementRouterService.getRequirementIndicator(requirement.indicatorId))
            ).toList()
        );
        return new AggregationResult<>(request.getJournal(), result, List.of());
    }
}
