package com.colvir.ms.sys.rms.generated.service.mapper;

import com.colvir.ms.sys.rms.generated.domain.RelatedPayment;
import com.colvir.ms.sys.rms.generated.service.dto.RelatedPaymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

/**
 * Mapper for the entity {@link RelatedPayment} and its DTO {@link RelatedPaymentDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {RequirementMapper.class, PaymentMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RelatedPaymentMapper {
    @Mapping(source = "requirementOfRelatedPayments.id", target = "requirementOfRelatedPaymentsId")
    @Mapping(source = "payment.id", target = "paymentId")
    RelatedPaymentDTO toDto(RelatedPayment relatedPayment);

    @Mapping(source = "requirementOfRelatedPaymentsId", target = "requirementOfRelatedPayments")
    @Mapping(source = "paymentId", target = "payment")
    RelatedPayment toEntity(RelatedPaymentDTO relatedPaymentDTO);

    List<RelatedPaymentDTO> toDto(List<RelatedPayment> relatedPaymentList);

    List<RelatedPayment> toEntity(List<RelatedPaymentDTO> relatedPaymentDtoList);

    default RelatedPayment fromId(Long id) {
        if (id == null) {
            return null;
        }
        return RelatedPayment.getEntityManager().getReference(RelatedPayment.class, id);
    }

    default Set<RelatedPaymentDTO> convertSet(Set<RelatedPayment> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new RelatedPaymentDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<RelatedPayment> setRelatedPaymentDTOToSetRelatedPayment(Set<RelatedPaymentDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<RelatedPayment> set1 = new LinkedHashSet<RelatedPayment>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (RelatedPaymentDTO relatedPaymentDTO : objSetDto) {
            set1.add(RelatedPayment.getEntityManager().getReference(RelatedPayment.class, relatedPaymentDTO.id));
        }
        return set1;
    }
}
