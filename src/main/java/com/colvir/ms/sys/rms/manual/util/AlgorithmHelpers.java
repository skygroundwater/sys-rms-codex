package com.colvir.ms.sys.rms.manual.util;

import com.colvir.ms.common.alg.extension.api.dto.CalculateRequestDTO;
import com.colvir.ms.common.alg.extension.runtime.CalculationProcessorInternal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;

@ApplicationScoped
public class AlgorithmHelpers {

    @Inject
    CalculationProcessorInternal processor;

    @Inject
    ObjectMapper objectMapper;

    public <T> T evaluateAlgorithm(String algorithm, Map<String, Object> context, Class<T> valueType) {
        JsonNode contextNode = objectMapper.valueToTree(context);
        return evaluateAlgorithm(algorithm, contextNode, valueType);
    }

    public <T> T evaluateAlgorithm(String algorithm, JsonNode context, Class<T> valueType) {
        CalculateRequestDTO request = new CalculateRequestDTO();
        try {
            request.calculate = objectMapper.readTree(algorithm);
            request.context = context;
            Uni<JsonNode> result = processor.process(request);
            JsonNode resultNode = result.await().indefinitely();
            if (resultNode.has("result")) {
                resultNode = resultNode.get("result");
            } else {
                throw new RuntimeException("Unknown result");
            }
            if (resultNode.isNull() || resultNode.isMissingNode()) {
                return null;
            } else {
                if (!resultNode.isArray()) {
                    return objectMapper.treeToValue(resultNode, valueType);
                } else {
                    if (resultNode.has(0)) {
                        JsonNode first = resultNode.get(0);
                        if (first.isObject()) {
                            var firstField = first.fields().next();
                            if (firstField != null) {
                                return objectMapper.treeToValue(firstField.getValue(), valueType);
                            } else {
                                throw new RuntimeException("Decision has no columns");
                            }
                        } else {
                            throw new RuntimeException("Bad decision result structure");
                        }
                    } else {
                        return null;
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Long evaluateAlgorithmAsId(String algorithm, JsonNode context) {
        CalculateRequestDTO request = new CalculateRequestDTO();
        try {
            request.calculate = objectMapper.readTree(algorithm);
            request.context = context;
            Uni<JsonNode> result = processor.process(request);
            JsonNode resultNode = result.await().indefinitely();
            if (resultNode.has("result")) {
                resultNode = resultNode.get("result");
            } else {
                throw new RuntimeException("Unknown result");
            }
            if (resultNode.isNull() || resultNode.isMissingNode()) {
                return null;
            } else {
                if (!resultNode.isArray()) {
                    if (resultNode.hasNonNull("id")) {
                        return resultNode.get("id").asLong();
                    }
                } else {
                    if (resultNode.has(0)) {
                        JsonNode first = resultNode.get(0);
                        if (first.isObject()) {
                            // рассчитано на таблицу решений с одним результирующим столбцом и hitPolicy = UNIQUE
                            if (first.size() == 1) {
                                var firstField = first.fields().next();
                                if (firstField != null && firstField.getValue().hasNonNull("id")) {
                                    return firstField.getValue().get("id").asLong();
                                } else {
                                    throw new RuntimeException("Decision has no columns");
                                }
                            } else {
                                throw new RuntimeException("Bad decision result structure");
                            }
                        } else {
                            return null;
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
