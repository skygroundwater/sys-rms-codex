package com.colvir.ms.sys.rms.generated.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import com.colvir.ms.sys.rms.generated.domain.*;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementHoldDTO;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link RequirementHold} and its DTO {@link RequirementHoldDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {RequirementMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequirementHoldMapper {
    @Mapping(source = "requirementOfAssignedHolds.id", target = "requirementOfAssignedHoldsId")
    RequirementHoldDTO toDto(RequirementHold requirementHold);

    @Mapping(source = "requirementOfAssignedHoldsId", target = "requirementOfAssignedHolds")
    RequirementHold toEntity(RequirementHoldDTO requirementHoldDTO);

    List<RequirementHoldDTO> toDto(List<RequirementHold> requirementHoldList);

    List<RequirementHold> toEntity(List<RequirementHoldDTO> requirementHoldDtoList);

    default RequirementHold fromId(Long id) {
        if (id == null) {
            return null;
        }
        return RequirementHold.getEntityManager().getReference(RequirementHold.class, id);
    }

    default Set<RequirementHoldDTO> convertSet(Set<RequirementHold> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new RequirementHoldDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<RequirementHold> setRequirementHoldDTOToSetRequirementHold(Set<RequirementHoldDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<RequirementHold> set1 = new LinkedHashSet<RequirementHold>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (RequirementHoldDTO requirementHoldDTO : objSetDto) {
            set1.add(RequirementHold.getEntityManager().getReference(RequirementHold.class, requirementHoldDTO.id));
        }
        return set1;
    }
}
