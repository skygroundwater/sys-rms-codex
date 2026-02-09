package com.colvir.ms.sys.rms.manual.service.impl;

import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.generated.domain.IndicatorRequirementTypeMap;
import com.colvir.ms.sys.rms.generated.domain.RequirementType;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementTypeDTO;
import com.colvir.ms.sys.rms.generated.service.mapper.RequirementTypeMapper;
import com.colvir.ms.sys.rms.manual.service.RequirementTypeService;
import com.colvir.ms.sys.rms.manual.util.AlgorithmHelpers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.cache.CacheResult;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RequirementTypeServiceImpl implements RequirementTypeService {
    private final Logger log;
    private final ObjectMapper objectMapper;
    private final AlgorithmHelpers algorithmHelpers;
    private final  RequirementTypeMapper requirementTypeMapper;


    public RequirementTypeServiceImpl(Logger log, ObjectMapper objectMapper,
                                      AlgorithmHelpers algorithmHelpers,
                                      RequirementTypeMapper requirementTypeMapper) {
        this.log = log;
        this.objectMapper = objectMapper;
        this.algorithmHelpers = algorithmHelpers;
        this.requirementTypeMapper = requirementTypeMapper;
    }

    public IndicatorRequirementTypeMap findRequirementTypeMap(Long systemLocale) {
        return IndicatorRequirementTypeMap.find("localeId", systemLocale).firstResult();
    }

    @CacheResult(cacheName = "requirement-type-result-cache")
    public RequirementType getRequirementTypeFromCache(Long requirementTypeId) {
        return RequirementType.findById(requirementTypeId);
    }

    @CacheResult(cacheName = "requirementType-cache")
    public RequirementTypeDTO getRequirementType(ReferenceDto indicatorDesc, Long systemLocale) {
        // вид требования вычисляется с помощью настроенного алгоритма
        // который определяет его в зависимости от "расчетной категории" из записи массива
        log.infof("--- read RequirementType from DB:  %s - %s ---", indicatorDesc, systemLocale);
        if (indicatorDesc == null || indicatorDesc.id == null) {
            throw new RuntimeException("Indicator Description is not defined");
        }

        // получение правила для вычисления вида требования
        IndicatorRequirementTypeMap requirementTypeMap = findRequirementTypeMap(systemLocale);
        if (requirementTypeMap == null) {
            throw new RuntimeException(String.format("RequirementType mapping on Indicator is not found (localeId=%s)", systemLocale));
        }
        if (StringUtil.isNullOrEmpty(requirementTypeMap.requirementTypeRule)) {
            throw new RuntimeException(String.format("RequirementType mapping on Indicator rule is null (localeId=%s)", systemLocale));
        }

        JsonNode algContext = objectMapper.createObjectNode()
            .set("indicator", objectMapper.createObjectNode()
                .put("id", String.valueOf(indicatorDesc.id))
                .put("__objectType", indicatorDesc.__objectType));
        Long requirementTypeId;
        try {
            requirementTypeId = algorithmHelpers.evaluateAlgorithmAsId(requirementTypeMap.requirementTypeRule, algContext);
        }catch (Exception e) {
            log.error("Failed to evaluate algorithm: algorithm = %s, context = %s"
                .formatted(requirementTypeMap.requirementTypeRule, algContext.toPrettyString()), e);
            throw new RuntimeException("Failed to evaluate algorithm: " + requirementTypeMap.requirementTypeRule, e);
        }
        if (requirementTypeId == null) {
            throw new RuntimeException("RequirementType id is not defined for indicatorDesc: " + indicatorDesc);
        }
        RequirementType requirementType = getRequirementTypeFromCache(requirementTypeId);
        if (requirementType != null) {
            return requirementTypeMapper.toDto(requirementType);
        } else {
            throw new RuntimeException(String.format("RequirementType with id=%s is not found", requirementTypeId));
        }
    }

}
