package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.IndicatorRequirementTypeMap;
import com.colvir.ms.sys.rms.generated.domain.RequirementType;
import com.colvir.ms.sys.rms.manual.dao.RequirementTypeDao;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RequirementTypeDaoImpl implements RequirementTypeDao {

    @Override
    public IndicatorRequirementTypeMap findByLocaleId(Long localeId) {
        return IndicatorRequirementTypeMap.find("localeId", localeId).firstResult();
    }

    @Override
    public RequirementType findById(Long id) {
        return RequirementType.findById(id);
    }

    @Override
    public RequirementType findByIdOrThrow(Long id, String messageTemplate) {
        RequirementType requirementType = findById(id);
        if (requirementType == null) {
            throw new RuntimeException(String.format(messageTemplate, id));
        }
        return requirementType;
    }
}
