package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.sys.opr.api.step.runner.method.response.Substep;
import com.colvir.ms.sys.opr.api.step.runner.method.response.SubstepParameter;
import com.colvir.ms.sys.rms.dto.BbpObjectProperties;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.manual.constant.RmsConstants;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.service.BaseProcessService;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.antlr.v4.runtime.misc.Pair;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ApplicationScoped
public class StepCreatorService {

    @Inject
    BaseProcessService baseProcessService;

    @Inject
    Logger log;

    @ConfigProperty(name = "requirements-machine-id", defaultValue = "")
    String requirementsMachineId;

    private static final String SYS_BP_BBP_DP = "/SYS/BP/BBP";

    public Substep createSysBbpPackStateSubStep(List<Pair<RequirementJournalDto, RequirementStateInfoDto>> requirements) {

        List<BbpObjectProperties> properties = requirements.stream()
            .map(this::buildObjectProperties)
            .filter(Objects::nonNull)
            .toList();

        Map<String, SubstepParameter> parameters = Map.of(
            "objectProperties", SubstepParameter.value(properties),
            "processIdStateMap", SubstepParameter.result(RmsConstants.UPDATE_BASE_BUSINESS_PROCESS_RESULT_PATH)
        );

        return createSubStep(StepsNames.SYS_BP_BBP_PACK_STATE, parameters, SYS_BP_BBP_DP);
    }

    public Substep createSysBbpStartSubStep(String stateField) {
        Map<String, SubstepParameter> parameters = new HashMap<>();
        parameters.put("state", SubstepParameter.result(stateField));
        parameters.put("machineId", SubstepParameter.value(requirementsMachineId));
        parameters.put("stateField", SubstepParameter.value(stateField));
        parameters.put("objectResult", SubstepParameter.value(true));

        return createSubStep(StepsNames.SYS_BP_BBP_START, parameters, SYS_BP_BBP_DP);
    }

    private BbpObjectProperties buildObjectProperties(Pair<RequirementJournalDto, RequirementStateInfoDto> pair) {
        RequirementJournalDto journal = pair.a;
        RequirementStateInfoDto state = pair.b;

        log.infof("links states of requirement prevReqState=%s and newReqState=%s", journal, state);

        if (journal == null || state == null) {
            return null;
        }

        String event = baseProcessService.getBbpUpdateEvent(state, journal);
        if (event == null || event.isEmpty()) {
            return null;
        }

        log.infof("----create property from: %s and %s", journal, state);

        ObjectMapper mapper = ContextObjectMapper.get();
        ObjectNode mainNode = mapper.createObjectNode()
            .put("state", state.status.toString())
            .put("paidAmount", state.payedAmount)
            .put("amount", state.amount)
            .put("unpaidAmount", state.amount.subtract(state.payedAmount));

        ObjectNode variablesNode = mapper.createObjectNode().set("main", mainNode);
        ObjectNode contextNode = mapper.createObjectNode().set("variables", variablesNode);

        BbpObjectProperties obj = new BbpObjectProperties();
        obj.machineContext = contextNode;
        obj.event = event;
        obj.processId = journal.bbpProcessId;

        return obj;
    }

    public Substep createSubStep(String subStepName, Map<String, SubstepParameter> parameters, String domainPath) {
        return Substep.builder()
            .name(subStepName)
            .domainPath(domainPath)
            .parameters(parameters)
            .build();
    }

    public Substep createSubStep(String subStepName, Map<String, SubstepParameter> parameters) {
        return Substep.builder()
            .name(subStepName)
            .parameters(parameters)
            .build();
    }
}
