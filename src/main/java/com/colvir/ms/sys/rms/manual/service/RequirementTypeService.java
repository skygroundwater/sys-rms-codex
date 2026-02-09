package com.colvir.ms.sys.rms.manual.service;

import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementTypeDTO;

public interface RequirementTypeService {
    RequirementTypeDTO getRequirementType(ReferenceDto indicatorDesc, Long systemLocale);
}
