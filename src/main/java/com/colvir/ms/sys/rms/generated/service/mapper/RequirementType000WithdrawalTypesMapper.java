package com.colvir.ms.sys.rms.generated.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import com.colvir.ms.sys.rms.generated.domain.*;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementType000WithdrawalTypesDTO;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link RequirementType000WithdrawalTypes} and its DTO {@link RequirementType000WithdrawalTypesDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {RequirementTypeMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequirementType000WithdrawalTypesMapper {
    @Mapping(source = "requirementType.id", target = "requirementTypeId")
    RequirementType000WithdrawalTypesDTO toDto(RequirementType000WithdrawalTypes requirementType000WithdrawalTypes);

    @Mapping(source = "requirementTypeId", target = "requirementType")
    RequirementType000WithdrawalTypes toEntity(RequirementType000WithdrawalTypesDTO requirementType000WithdrawalTypesDTO);

    List<RequirementType000WithdrawalTypesDTO> toDto(List<RequirementType000WithdrawalTypes> requirementType000WithdrawalTypesList);

    List<RequirementType000WithdrawalTypes> toEntity(List<RequirementType000WithdrawalTypesDTO> requirementType000WithdrawalTypesDtoList);

    default RequirementType000WithdrawalTypes fromId(Long id) {
        if (id == null) {
            return null;
        }
        return RequirementType000WithdrawalTypes.getEntityManager().getReference(RequirementType000WithdrawalTypes.class, id);
    }

    default Set<RequirementType000WithdrawalTypesDTO> convertSet(Set<RequirementType000WithdrawalTypes> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new RequirementType000WithdrawalTypesDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<RequirementType000WithdrawalTypes> setRequirementType000WithdrawalTypesDTOToSetRequirementType000WithdrawalTypes(Set<RequirementType000WithdrawalTypesDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<RequirementType000WithdrawalTypes> set1 = new LinkedHashSet<RequirementType000WithdrawalTypes>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (RequirementType000WithdrawalTypesDTO requirementType000WithdrawalTypesDTO : objSetDto) {
            set1.add(RequirementType000WithdrawalTypes.getEntityManager().getReference(RequirementType000WithdrawalTypes.class, requirementType000WithdrawalTypesDTO.id));
        }
        return set1;
    }
}
