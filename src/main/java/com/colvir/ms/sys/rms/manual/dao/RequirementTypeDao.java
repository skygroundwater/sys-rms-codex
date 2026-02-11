package com.colvir.ms.sys.rms.manual.dao;

import com.colvir.ms.sys.rms.generated.domain.IndicatorRequirementTypeMap;
import com.colvir.ms.sys.rms.generated.domain.RequirementType;

public interface RequirementTypeDao {

    IndicatorRequirementTypeMap findByLocaleId(Long localeId);

    RequirementType findById(Long id);

    RequirementType findByIdOrThrow(Long id);
}
