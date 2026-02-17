package com.colvir.ms.sys.rms.generated.service.mapper;

import com.colvir.ms.sys.rms.generated.domain.Requirement000PayerDetails;
import com.colvir.ms.sys.rms.generated.service.dto.Requirement000PayerDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

/**
 * Mapper for the entity {@link Requirement000PayerDetails} and its DTO {@link Requirement000PayerDetailsDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {PaymentChannelMapper.class, RequirementMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface Requirement000PayerDetailsMapper {
    @Mapping(source = "paymentChannel.id", target = "paymentChannelId")
    @Mapping(source = "requirementOfPayerDetails.id", target = "requirementOfPayerDetailsId")
    Requirement000PayerDetailsDTO toDto(Requirement000PayerDetails requirement000PayerDetails);

    @Mapping(source = "paymentChannelId", target = "paymentChannel")
    @Mapping(source = "requirementOfPayerDetailsId", target = "requirementOfPayerDetails")
    Requirement000PayerDetails toEntity(Requirement000PayerDetailsDTO requirement000PayerDetailsDTO);

    List<Requirement000PayerDetailsDTO> toDto(List<Requirement000PayerDetails> requirement000PayerDetailsList);

    List<Requirement000PayerDetails> toEntity(List<Requirement000PayerDetailsDTO> requirement000PayerDetailsDtoList);

    default Requirement000PayerDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        return Requirement000PayerDetails.getEntityManager().getReference(Requirement000PayerDetails.class, id);
    }

    default Set<Requirement000PayerDetailsDTO> convertSet(Set<Requirement000PayerDetails> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new Requirement000PayerDetailsDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<Requirement000PayerDetails> setRequirement000PayerDetailsDTOToSetRequirement000PayerDetails(Set<Requirement000PayerDetailsDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<Requirement000PayerDetails> set1 = new LinkedHashSet<Requirement000PayerDetails>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (Requirement000PayerDetailsDTO requirement000PayerDetailsDTO : objSetDto) {
            set1.add(Requirement000PayerDetails.getEntityManager().getReference(Requirement000PayerDetails.class, requirement000PayerDetailsDTO.id));
        }
        return set1;
    }
}
