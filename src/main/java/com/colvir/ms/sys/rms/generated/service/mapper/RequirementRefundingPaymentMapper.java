package com.colvir.ms.sys.rms.generated.service.mapper;

import com.colvir.ms.sys.rms.generated.domain.RequirementRefundingPayment;
import com.colvir.ms.sys.rms.generated.service.dto.RequirementRefundingPaymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

/**
 * Mapper for the entity {@link RequirementRefundingPayment} and its DTO {@link RequirementRefundingPaymentDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {RefundingPaymentMapper.class, RequirementMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequirementRefundingPaymentMapper {
    @Mapping(source = "refundingPayment.id", target = "refundingPaymentId")
    @Mapping(source = "requirementOfRefundingPayments.id", target = "requirementOfRefundingPaymentsId")
    RequirementRefundingPaymentDTO toDto(RequirementRefundingPayment requirementRefundingPayment);

    @Mapping(source = "refundingPaymentId", target = "refundingPayment")
    @Mapping(source = "requirementOfRefundingPaymentsId", target = "requirementOfRefundingPayments")
    RequirementRefundingPayment toEntity(RequirementRefundingPaymentDTO requirementRefundingPaymentDTO);

    List<RequirementRefundingPaymentDTO> toDto(List<RequirementRefundingPayment> requirementRefundingPaymentList);

    List<RequirementRefundingPayment> toEntity(List<RequirementRefundingPaymentDTO> requirementRefundingPaymentDtoList);

    default RequirementRefundingPayment fromId(Long id) {
        if (id == null) {
            return null;
        }
        return RequirementRefundingPayment.getEntityManager().getReference(RequirementRefundingPayment.class, id);
    }

    default Set<RequirementRefundingPaymentDTO> convertSet(Set<RequirementRefundingPayment> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new RequirementRefundingPaymentDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<RequirementRefundingPayment> setRequirementRefundingPaymentDTOToSetRequirementRefundingPayment(Set<RequirementRefundingPaymentDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<RequirementRefundingPayment> set1 = new LinkedHashSet<RequirementRefundingPayment>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (RequirementRefundingPaymentDTO requirementRefundingPaymentDTO : objSetDto) {
            set1.add(RequirementRefundingPayment.getEntityManager().getReference(RequirementRefundingPayment.class, requirementRefundingPaymentDTO.id));
        }
        return set1;
    }
}
