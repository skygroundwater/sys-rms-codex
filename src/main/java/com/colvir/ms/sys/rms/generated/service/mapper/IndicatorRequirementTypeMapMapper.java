package com.colvir.ms.sys.rms.generated.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import com.colvir.ms.sys.rms.generated.domain.*;
import com.colvir.ms.sys.rms.generated.service.dto.IndicatorRequirementTypeMapDTO;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link IndicatorRequirementTypeMap} and its DTO {@link IndicatorRequirementTypeMapDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndicatorRequirementTypeMapMapper {
    IndicatorRequirementTypeMapDTO toDto(IndicatorRequirementTypeMap indicatorRequirementTypeMap);

    IndicatorRequirementTypeMap toEntity(IndicatorRequirementTypeMapDTO indicatorRequirementTypeMapDTO);

    List<IndicatorRequirementTypeMapDTO> toDto(List<IndicatorRequirementTypeMap> indicatorRequirementTypeMapList);

    List<IndicatorRequirementTypeMap> toEntity(List<IndicatorRequirementTypeMapDTO> indicatorRequirementTypeMapDtoList);

    default IndicatorRequirementTypeMap fromId(Long id) {
        if (id == null) {
            return null;
        }
        return IndicatorRequirementTypeMap.getEntityManager().getReference(IndicatorRequirementTypeMap.class, id);
    }

    default Set<IndicatorRequirementTypeMapDTO> convertSet(Set<IndicatorRequirementTypeMap> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new IndicatorRequirementTypeMapDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<IndicatorRequirementTypeMap> setIndicatorRequirementTypeMapDTOToSetIndicatorRequirementTypeMap(Set<IndicatorRequirementTypeMapDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<IndicatorRequirementTypeMap> set1 = new LinkedHashSet<IndicatorRequirementTypeMap>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (IndicatorRequirementTypeMapDTO indicatorRequirementTypeMapDTO : objSetDto) {
            set1.add(IndicatorRequirementTypeMap.getEntityManager().getReference(IndicatorRequirementTypeMap.class, indicatorRequirementTypeMapDTO.id));
        }
        return set1;
    }
}
