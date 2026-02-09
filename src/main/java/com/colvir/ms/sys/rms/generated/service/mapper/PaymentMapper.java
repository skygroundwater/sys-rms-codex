package com.colvir.ms.sys.rms.generated.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import com.colvir.ms.sys.rms.generated.domain.*;
import com.colvir.ms.sys.rms.generated.service.dto.PaymentDTO;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {
    PaymentDTO toDto(Payment payment);

    @Mapping(target = "refundPayments", ignore = true)
    Payment toEntity(PaymentDTO paymentDTO);

    List<PaymentDTO> toDto(List<Payment> paymentList);

    List<Payment> toEntity(List<PaymentDTO> paymentDtoList);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        return Payment.getEntityManager().getReference(Payment.class, id);
    }

    default Set<PaymentDTO> convertSet(Set<Payment> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new PaymentDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<Payment> setPaymentDTOToSetPayment(Set<PaymentDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<Payment> set1 = new LinkedHashSet<Payment>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (PaymentDTO paymentDTO : objSetDto) {
            set1.add(Payment.getEntityManager().getReference(Payment.class, paymentDTO.id));
        }
        return set1;
    }
}
