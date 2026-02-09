package com.colvir.ms.sys.rms.generated.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import com.colvir.ms.sys.rms.generated.domain.*;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementTypeDTO;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link RequirementType} and its DTO {@link RequirementTypeDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequirementTypeMapper {
    RequirementTypeDTO toDto(RequirementType requirementType);

    @Mapping(target = "withdrawalTypes", ignore = true)
    RequirementType toEntity(RequirementTypeDTO requirementTypeDTO);

    List<RequirementTypeDTO> toDto(List<RequirementType> requirementTypeList);

    List<RequirementType> toEntity(List<RequirementTypeDTO> requirementTypeDtoList);

    default RequirementType fromId(Long id) {
        if (id == null) {
            return null;
        }
        return RequirementType.getEntityManager().getReference(RequirementType.class, id);
    }

    default Set<RequirementTypeDTO> convertSet(Set<RequirementType> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new RequirementTypeDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<RequirementType> setRequirementTypeDTOToSetRequirementType(Set<RequirementTypeDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<RequirementType> set1 = new LinkedHashSet<RequirementType>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (RequirementTypeDTO requirementTypeDTO : objSetDto) {
            set1.add(RequirementType.getEntityManager().getReference(RequirementType.class, requirementTypeDTO.id));
        }
        return set1;
    }
}
