package com.colvir.ms.sys.rms.algorithm.specs;

import com.colvir.ms.common.alg.extension.api.AlgorithmSpec;
import com.colvir.ms.common.alg.extension.api.AlgorithmSpecResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@AlgorithmSpec("/SYS/RMS/IndicatorRequirementTypeMap.requirementTypeRule")
@ApplicationScoped
public class IndicatorRequirementTypeMapRule extends AlgorithmSpecResult {

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Uni<JsonNode> algorithmSpecByContext(JsonNode context) {
        return Uni.createFrom().item((ObjectNode) objectMapper.createObjectNode().set("algorithmParams", objectMapper.createArrayNode()))
            .onItem().transformToUni(result -> getResultType(context, result))
            .onItem().transformToUni(result -> addParams(context, result));
    }

    // тип выходного значения алгоритма - вид требования
    private Uni<ObjectNode> getResultType(JsonNode context, ObjectNode result) {
        result.put("resultType", "/SYS/RMS/RequirementType");
        return Uni.createFrom().item(result);
    }


    private Uni<ObjectNode> addParams(JsonNode context, ObjectNode result) {
        ArrayNode params = (ArrayNode) result.get("algorithmParams");
        params.add(objectMapper.createObjectNode()
            .put("code", "indicator")
            .put("label", "Шаблонная расчетная категория")
            .put("dataType", "/SYS/CMPT/IndicatorDescr"));
        return Uni.createFrom().item(result);
    }

}
