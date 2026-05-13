package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.common.router.DDCRouterClient;
import com.colvir.ms.sys.rms.dto.BaseProcessResultDto;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.manual.service.BaseProcessService;
import com.colvir.ms.sys.rms.manual.constant.RmsConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

import static com.colvir.ms.common.router.dto.DDCCallRequest.post;

@ApplicationScoped
public class BaseProcessServiceImpl implements BaseProcessService {

    @Inject
    @RestClient
    DDCRouterClient ddcRouterClient;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    Logger log;

    private static final Duration MAX_TIMEOUT = Duration.ofSeconds(10);


    @Override
    public String startProcess(String machineId, Object mainObject) {
        var startBbpPath = "/state-machine/" + machineId + "/start";
        ObjectNode variables = objectMapper.createObjectNode();
        if (mainObject != null) {
            variables.set(
                "main",
                objectMapper.valueToTree(mainObject)
            );
        }
        Map<String, Object> body = Map.of("variables", variables);

        // из результата убираем variables
        var bbpState = ddcRouterClient.callReactive(post("/SYS/BP/BBP", startBbpPath).body(body))
            .map(this::peekVariables)
            .map(JsonNode::toString)
            .await().atMost(MAX_TIMEOUT);

        if (StringUtil.isNullOrEmpty(bbpState)) {
            throw new RuntimeException(String.format("State machine with machineId=%s start error", machineId));
        }
        return bbpState;
    }

    @Override
    public String sendEvent(String bbpEvent, String bbpProcessId, Object mainObject) {
        String bbpPath = String.format("/state-machine/%s/send/%s", bbpProcessId, bbpEvent);
        ObjectNode variables = objectMapper.createObjectNode();
        if (mainObject != null) {
            variables.set(
                "main",
                objectMapper.valueToTree(mainObject)
            );
        }
        Map<String, Object> body = Map.of("variables", variables);

        // из результата убираем variables
        var bbpState = ddcRouterClient.callReactive(post("/SYS/BP/BBP", bbpPath).body(body))
            .map(this::peekVariables)
            .map(JsonNode::toString)
            .await().atMost(MAX_TIMEOUT);

        if (StringUtil.isNullOrEmpty(bbpState)) {
            throw new RuntimeException(String.format("Send Base Process event %s (%s) error", bbpEvent, bbpProcessId));
        }
        return bbpState;
    }

    private JsonNode peekVariables(JsonNode jsonNode) {
        if (jsonNode != null && jsonNode.isObject() && jsonNode.has("variables")) {
            ((ObjectNode) jsonNode).remove("variables");
        }
        return jsonNode;
    }


    @Override
    public String getProcessId(String bbpStateText) {
        String processId;
        if (!StringUtil.isNullOrEmpty(bbpStateText)) {
            try {
                var state = objectMapper.readValue(bbpStateText, BaseProcessResultDto.class);
                processId = state.processId;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Base Process state is empty");
        }
        log.info(String.format("bbpProcessId=%s", processId));
        return processId;
    }


    @Override
    public Long getJournalId(String bbpStateText) {
        Long journalId;
        if (!StringUtil.isNullOrEmpty(bbpStateText)) {
            try {
                var state = objectMapper.readValue(bbpStateText, BaseProcessResultDto.class);
                journalId = state.journalId;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Base Process state is empty");
        }
        log.info(String.format("bbpJournalId=%s", journalId));
        return journalId;
    }


    /**
     * New format of BBPState doesn't contain the machineId
     * @param bbpStateText
     * @return
     */
    @Deprecated
    @Override
    public String getMachineId(String bbpStateText) {
        String machineId;
        if (!StringUtil.isNullOrEmpty(bbpStateText)) {
            try {
                var state = objectMapper.readValue(bbpStateText, BaseProcessResultDto.class);
                machineId = state.machineId;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Base Process state is empty");
        }
        log.info(String.format("bbpMachineId=%s", machineId));
        return machineId;
    }


    @Override
    public String getProcessState(String bbpStateText) {
        String stateCode;
        if (!StringUtil.isNullOrEmpty(bbpStateText)) {
            try {
                var state = objectMapper.readValue(bbpStateText, BaseProcessResultDto.class);
                stateCode = state.state;
                if(StringUtil.isNullOrEmpty(stateCode)) {
                    stateCode = state.stateCode;
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Base Process state is empty");
        }
        log.info(String.format("bbpProcessState=%s", stateCode));
        return stateCode;
    }


    @Override
    public String cancelExecution(String bbpProcessId, Long bbpJournalId) {
        return cancelExecution(bbpProcessId, bbpJournalId, true);
    }

    @Override
    public String cancelExecution(String bbpProcessId, Long bbpJournalId, boolean exclusive) {
        // исключаем из отката переданный bbpJournalId (exclusive=true)
        String bbpPath = String.format("/state-machine/%s/cancel/%s?exclusive=%s", bbpProcessId, bbpJournalId, exclusive);
        // из результата убираем variables
        var bbpState = ddcRouterClient.callReactive(post("/SYS/BP/BBP", bbpPath))
            .map(this::peekVariables)
            .map(JsonNode::toString)
            .await().atMost(MAX_TIMEOUT);

        if (StringUtil.isNullOrEmpty(bbpState)) {
            throw new RuntimeException(String.format("Cancel Base Process Execution error (processId=%s, journalId=%s)",
                bbpProcessId, bbpJournalId));
        }
        return bbpState;
    }

    @Override
    public String getBbpUpdateEvent(RequirementStateInfoDto newReqState, RequirementJournalDto prevReqState) {
        RequirementStatus newStatus = newReqState.status;
        RequirementStatus prevStatus = prevReqState.state;

        if (newStatus == null || prevStatus == null) throw new RuntimeException("New and previous requirements status cannot to be undefined");

        String errorMessage = String.format("Combination of states %s : %s is not allowed", prevStatus, newStatus);

        log.infof("getBbpUpdateEvent: newReqState = %s , prevReqState = %s", newReqState, prevReqState);

        // 1. Если статус не поменялся — проверяем движение суммы
        if (newStatus.equals(prevStatus)) {
            if (newReqState.payedAmount.compareTo(BigDecimal.ZERO) > 0
                && newReqState.payedAmount.compareTo(newReqState.amount) < 0) {
                return RmsConstants.BBP_RUN_PAYMENT_EVENT;
            }
            return "";
        }

        // 2. Если статус изменился — обрабатываем по переходам
        return switch (prevStatus) {
            case WAIT -> switch (newStatus) {
                case CANCELED -> RmsConstants.BBP_CANCEL_EVENT;
                case SUSPENDED -> RmsConstants.BBP_SUSPEND_EVENT;
                case WRITTEN_OFF -> RmsConstants.BBP_WRITEOFF_EVENT;
                case PAID -> RmsConstants.BBP_RUN_PAYMENT_EVENT;
                default -> throw new RuntimeException(errorMessage);
            };
            case SUSPENDED -> switch (newStatus) {
                case WAIT -> RmsConstants.BBP_RENEW_EVENT;
                case CANCELED -> RmsConstants.BBP_CANCEL_EVENT;
                case WRITTEN_OFF -> RmsConstants.BBP_WRITEOFF_EVENT;
                default -> throw new RuntimeException(errorMessage);
            };
            case PAID -> switch (newStatus) {
                case CANCELED -> RmsConstants.BBP_CANCEL_EVENT;
                case WAIT -> RmsConstants.BBP_RUN_REFUND_EVENT;
                default -> throw new RuntimeException(errorMessage);
            };
            default -> throw new RuntimeException(errorMessage);
        };
    }

}
