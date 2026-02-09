package com.colvir.ms.sys.rms.generated.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import com.colvir.ms.sys.rms.generated.domain.*;
import com.colvir.ms.sys.rms.generated.service.dto.Requirement000RelatedTaxesDTO;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Requirement000RelatedTaxes} and its DTO {@link Requirement000RelatedTaxesDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {RequirementMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface Requirement000RelatedTaxesMapper {
    @Mapping(source = "requirementOfRelatedTaxes.id", target = "requirementOfRelatedTaxesId")
    Requirement000RelatedTaxesDTO toDto(Requirement000RelatedTaxes requirement000RelatedTaxes);

    @Mapping(source = "requirementOfRelatedTaxesId", target = "requirementOfRelatedTaxes")
    Requirement000RelatedTaxes toEntity(Requirement000RelatedTaxesDTO requirement000RelatedTaxesDTO);

    List<Requirement000RelatedTaxesDTO> toDto(List<Requirement000RelatedTaxes> requirement000RelatedTaxesList);

    List<Requirement000RelatedTaxes> toEntity(List<Requirement000RelatedTaxesDTO> requirement000RelatedTaxesDtoList);

    default Requirement000RelatedTaxes fromId(Long id) {
        if (id == null) {
            return null;
        }
        return Requirement000RelatedTaxes.getEntityManager().getReference(Requirement000RelatedTaxes.class, id);
    }

    default Set<Requirement000RelatedTaxesDTO> convertSet(Set<Requirement000RelatedTaxes> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new Requirement000RelatedTaxesDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<Requirement000RelatedTaxes> setRequirement000RelatedTaxesDTOToSetRequirement000RelatedTaxes(Set<Requirement000RelatedTaxesDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<Requirement000RelatedTaxes> set1 = new LinkedHashSet<Requirement000RelatedTaxes>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (Requirement000RelatedTaxesDTO requirement000RelatedTaxesDTO : objSetDto) {
            set1.add(Requirement000RelatedTaxes.getEntityManager().getReference(Requirement000RelatedTaxes.class, requirement000RelatedTaxesDTO.id));
        }
        return set1;
    }
}
