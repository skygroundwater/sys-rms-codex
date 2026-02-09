package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.common.router.DDCRouterUtil;
import com.colvir.ms.common.router.dto.DDCModifyRequest;
import com.colvir.ms.common.router.dto.DDCReadRequest;
import com.colvir.ms.sys.rms.manual.service.RouterService;
import com.colvir.ms.router.agent.api.service.RouterAgentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.List;

@ApplicationScoped
public class RouterAgentClientServiceImpl implements RouterService {

    @ConfigProperty(name = "router.agent.maxResponseTime", defaultValue = "180000")
    Long maxResponseTime;

    @Inject
    RouterAgentService routerAgentService;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    Logger log;

    Long maxRetryAttempts = 2L;
    Duration initialRetryBackoff = Duration.ofMillis(100L);

    @Override
    public ObjectNode modify(String namespace, String entityName, ObjectNode body) {
        DDCModifyRequest modifyRequest = DDCRouterUtil.createDDCModifyRequest(namespace,entityName, body);
        log.infof("modifyRequest: \r\n %s", objectMapper.convertValue(modifyRequest, ObjectNode.class));
        try {
            ObjectNode response = this.modify(modifyRequest).getFirst();
            log.infof("modifyResponse: \r\n" + response);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error while restore %s id=%s: %s", entityName, body.get("id"), e));
        }
    }

    @Override
    public List<ObjectNode> modify(DDCModifyRequest modifyRequest) {
        return routerAgentService.modify(modifyRequest)
            .onFailure()
            .retry().withBackOff(initialRetryBackoff, Duration.ofMillis(maxResponseTime / 2)).atMost(maxRetryAttempts)
            .ifNoItem().after(Duration.ofMillis(maxResponseTime))
            .failWith(() -> new RuntimeException("Call didn't return any result in %s ms".formatted(maxResponseTime)))
            .await().indefinitely()
            .result;
    }

    @Override
    public List<ObjectNode> modify(List<DDCModifyRequest> modifyRequests) {
        return modifyRequests.stream()
            .reduce((o, n) -> {
                o.DDCActionRequests.addAll(n.DDCActionRequests);
                return o;
            })
            .map(this::modify)
            .orElseGet(List::of);
    }

    @Override
    public List<ObjectNode> read(DDCReadRequest readRequest) {
        return  routerAgentService.read(readRequest)
            .onFailure()
            .retry().withBackOff(initialRetryBackoff, Duration.ofMillis(maxResponseTime / 2)).atMost(maxRetryAttempts)
            .ifNoItem().after(Duration.ofMillis(maxResponseTime))
            .failWith(() -> new RuntimeException("Call didn't return any result in %s ms".formatted(maxResponseTime)))
            .await().indefinitely()
            .result;
    }

    @Override
    public Long extractIdFromResult(ObjectNode result) {
        if (result.hasNonNull("id")) {
            return result.path("id").asLong();
        } else {
            throw new RuntimeException(String.format("The router response does not contain correct result: %s", result.toPrettyString()));
        }
    }

}
