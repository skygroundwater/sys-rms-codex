package com.colvir.ms.sys.rms.generated.service.mapper;

import com.colvir.ms.sys.rms.generated.domain.RefundingPayment;
import com.colvir.ms.sys.rms.generated.service.dto.RefundingPaymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

/**
 * Mapper for the entity {@link RefundingPayment} and its DTO {@link RefundingPaymentDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {PaymentMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RefundingPaymentMapper {
    @Mapping(source = "paymentOfRefundPayments.id", target = "paymentOfRefundPaymentsId")
    RefundingPaymentDTO toDto(RefundingPayment refundingPayment);

    @Mapping(source = "paymentOfRefundPaymentsId", target = "paymentOfRefundPayments")
    RefundingPayment toEntity(RefundingPaymentDTO refundingPaymentDTO);

    List<RefundingPaymentDTO> toDto(List<RefundingPayment> refundingPaymentList);

    List<RefundingPayment> toEntity(List<RefundingPaymentDTO> refundingPaymentDtoList);

    default RefundingPayment fromId(Long id) {
        if (id == null) {
            return null;
        }
        return RefundingPayment.getEntityManager().getReference(RefundingPayment.class, id);
    }

    default Set<RefundingPaymentDTO> convertSet(Set<RefundingPayment> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new RefundingPaymentDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<RefundingPayment> setRefundingPaymentDTOToSetRefundingPayment(Set<RefundingPaymentDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<RefundingPayment> set1 = new LinkedHashSet<RefundingPayment>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (RefundingPaymentDTO refundingPaymentDTO : objSetDto) {
            set1.add(RefundingPayment.getEntityManager().getReference(RefundingPayment.class, refundingPaymentDTO.id));
        }
        return set1;
    }
}
