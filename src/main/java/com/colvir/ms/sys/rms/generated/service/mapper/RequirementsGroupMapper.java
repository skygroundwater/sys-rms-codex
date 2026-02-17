package com.colvir.ms.sys.rms.generated.service.mapper;

import com.colvir.ms.sys.rms.generated.domain.RequirementsGroup;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementsGroupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

/**
 * Mapper for the entity {@link RequirementsGroup} and its DTO {@link RequirementsGroupDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {GroupMemberMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequirementsGroupMapper {
    RequirementsGroupDTO toDto(RequirementsGroup requirementsGroup);

    RequirementsGroup toEntity(RequirementsGroupDTO requirementsGroupDTO);

    List<RequirementsGroupDTO> toDto(List<RequirementsGroup> requirementsGroupList);

    List<RequirementsGroup> toEntity(List<RequirementsGroupDTO> requirementsGroupDtoList);

    default RequirementsGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        return RequirementsGroup.getEntityManager().getReference(RequirementsGroup.class, id);
    }

    default Set<RequirementsGroupDTO> convertSet(Set<RequirementsGroup> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new RequirementsGroupDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<RequirementsGroup> setRequirementsGroupDTOToSetRequirementsGroup(Set<RequirementsGroupDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<RequirementsGroup> set1 = new LinkedHashSet<RequirementsGroup>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (RequirementsGroupDTO requirementsGroupDTO : objSetDto) {
            set1.add(RequirementsGroup.getEntityManager().getReference(RequirementsGroup.class, requirementsGroupDTO.id));
        }
        return set1;
    }
}
