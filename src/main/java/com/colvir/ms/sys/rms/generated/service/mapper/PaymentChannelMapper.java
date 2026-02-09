package com.colvir.ms.sys.rms.generated.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import com.colvir.ms.sys.rms.generated.domain.*;
import com.colvir.ms.sys.rms.generated.service.dto.PaymentChannelDTO;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


/**
 * Mapper for the entity {@link PaymentChannel} and its DTO {@link PaymentChannelDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentChannelMapper {
    PaymentChannelDTO toDto(PaymentChannel paymentChannel);

    PaymentChannel toEntity(PaymentChannelDTO paymentChannelDTO);

    List<PaymentChannelDTO> toDto(List<PaymentChannel> paymentChannelList);

    List<PaymentChannel> toEntity(List<PaymentChannelDTO> paymentChannelDtoList);

    default PaymentChannel fromId(Long id) {
        if (id == null) {
            return null;
        }
        return PaymentChannel.getEntityManager().getReference(PaymentChannel.class, id);
    }

    default Set<PaymentChannelDTO> convertSet(Set<PaymentChannel> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new PaymentChannelDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<PaymentChannel> setPaymentChannelDTOToSetPaymentChannel(Set<PaymentChannelDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<PaymentChannel> set1 = new LinkedHashSet<PaymentChannel>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (PaymentChannelDTO paymentChannelDTO : objSetDto) {
            set1.add(PaymentChannel.getEntityManager().getReference(PaymentChannel.class, paymentChannelDTO.id));
        }
        return set1;
    }
}
