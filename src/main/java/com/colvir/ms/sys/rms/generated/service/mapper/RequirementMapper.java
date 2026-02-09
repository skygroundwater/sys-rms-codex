package com.colvir.ms.sys.rms.generated.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import com.colvir.ms.sys.rms.generated.domain.*;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementDTO;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Requirement} and its DTO {@link RequirementDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {RequirementTypeMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequirementMapper {
    @Mapping(source = "requirementType.id", target = "requirementTypeId")
    RequirementDTO toDto(Requirement requirement);

    @Mapping(target = "assignedHolds", ignore = true)
    @Mapping(target = "refundingPayments", ignore = true)
    @Mapping(target = "relatedPayments", ignore = true)
    @Mapping(source = "requirementTypeId", target = "requirementType")
    @Mapping(target = "withdrawalRules", ignore = true)
    @Mapping(target = "payerDetails", ignore = true)
    @Mapping(target = "relatedTaxes", ignore = true)
    Requirement toEntity(RequirementDTO requirementDTO);

    List<RequirementDTO> toDto(List<Requirement> requirementList);

    List<Requirement> toEntity(List<RequirementDTO> requirementDtoList);

    default Requirement fromId(Long id) {
        if (id == null) {
            return null;
        }
        return Requirement.getEntityManager().getReference(Requirement.class, id);
    }

    default Set<RequirementDTO> convertSet(Set<Requirement> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new RequirementDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<Requirement> setRequirementDTOToSetRequirement(Set<RequirementDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<Requirement> set1 = new LinkedHashSet<Requirement>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (RequirementDTO requirementDTO : objSetDto) {
            set1.add(Requirement.getEntityManager().getReference(Requirement.class, requirementDTO.id));
        }
        return set1;
    }
}
