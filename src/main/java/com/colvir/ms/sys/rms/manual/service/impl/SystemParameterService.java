package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.common.router.DDCRouterClient;
import com.colvir.ms.sys.opr.ContextMapper;
import com.colvir.ms.sys.rms.manual.constant.RmsConstants;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.colvir.ms.common.router.dto.DDCCallRequest.post;

@ApplicationScoped
public class SystemParameterService {
    final public static String SYS_PRM_NAMESPACE = "/SYS/PRM";
    final public static String SYS_PRM_VALUE_PATH = "parameter/find-par-val";

    @Inject
    @RestClient
    DDCRouterClient ddcRouterClient;

    @Inject
    Logger log;

    @CacheResult(cacheName = "systemLocale")
    public Long getSystemLocale(final String parameterCode) {
        return Long.valueOf(getStringValue(parameterCode));
    }

    @CacheResult(cacheName = "holdTypeForRequirement")
    public Long getHoldType() {
        return Long.valueOf(getStringValue(RmsConstants.DEFAULT_HOLD_TYPE));
    }

    public Object getValue(final String parameterCode) {
        try {
            final Map<String, Object> requestBody = new HashMap<>() {{
                put("parameterCode", parameterCode);
            }};
            var request = post(SYS_PRM_NAMESPACE, SYS_PRM_VALUE_PATH).body(requestBody);
            if (log.isDebugEnabled()) {
                log.debugf("callRequest = %s", ContextObjectMapper.get().convertValue(request, ObjectNode.class));
            }
            List<Map<String, Object>> response = ddcRouterClient.call(request);
            return response.get(0).get("value");
        } catch (Exception e) {
            throw new RuntimeException(String.format("Exception while fetching value of system parameter \"%s\"", parameterCode));
        }
    }

    public <N> N mapValue(ContextMapper contextMapper, String parameterCode, Class<N> convertClass) {
        JsonNode jsonNode = contextMapper.findNode(parameterCode);
        log.infof(parameterCode + ": " + jsonNode);
        return ContextObjectMapper.get().convertValue(contextMapper.findNode(parameterCode), convertClass);
    }

    private String getStringValue(final String parameterCode) {
        return getValue(parameterCode).toString();
    }
}
