package com.colvir.ms.sys.rms.generated.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import com.colvir.ms.sys.rms.generated.domain.*;
import com.colvir.ms.sys.rms.generated.service.dto.SysAcc000WithdrawalRuleLsAccountNumbersDTO;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link SysAcc000WithdrawalRuleLsAccountNumbers} and its DTO {@link SysAcc000WithdrawalRuleLsAccountNumbersDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {SysAcc000WithdrawalRuleMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysAcc000WithdrawalRuleLsAccountNumbersMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    SysAcc000WithdrawalRuleLsAccountNumbersDTO toDto(SysAcc000WithdrawalRuleLsAccountNumbers sysAcc000WithdrawalRuleLsAccountNumbers);

    @Mapping(source = "ownerId", target = "owner")
    SysAcc000WithdrawalRuleLsAccountNumbers toEntity(SysAcc000WithdrawalRuleLsAccountNumbersDTO sysAcc000WithdrawalRuleLsAccountNumbersDTO);

    List<SysAcc000WithdrawalRuleLsAccountNumbersDTO> toDto(List<SysAcc000WithdrawalRuleLsAccountNumbers> sysAcc000WithdrawalRuleLsAccountNumbersList);

    List<SysAcc000WithdrawalRuleLsAccountNumbers> toEntity(List<SysAcc000WithdrawalRuleLsAccountNumbersDTO> sysAcc000WithdrawalRuleLsAccountNumbersDtoList);

    default SysAcc000WithdrawalRuleLsAccountNumbers fromId(Long id) {
        if (id == null) {
            return null;
        }
        return SysAcc000WithdrawalRuleLsAccountNumbers.getEntityManager().getReference(SysAcc000WithdrawalRuleLsAccountNumbers.class, id);
    }

    default Set<SysAcc000WithdrawalRuleLsAccountNumbersDTO> convertSet(Set<SysAcc000WithdrawalRuleLsAccountNumbers> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new SysAcc000WithdrawalRuleLsAccountNumbersDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<SysAcc000WithdrawalRuleLsAccountNumbers> setSysAcc000WithdrawalRuleLsAccountNumbersDTOToSetSysAcc000WithdrawalRuleLsAccountNumbers(Set<SysAcc000WithdrawalRuleLsAccountNumbersDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<SysAcc000WithdrawalRuleLsAccountNumbers> set1 = new LinkedHashSet<SysAcc000WithdrawalRuleLsAccountNumbers>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (SysAcc000WithdrawalRuleLsAccountNumbersDTO sysAcc000WithdrawalRuleLsAccountNumbersDTO : objSetDto) {
            set1.add(SysAcc000WithdrawalRuleLsAccountNumbers.getEntityManager().getReference(SysAcc000WithdrawalRuleLsAccountNumbers.class, sysAcc000WithdrawalRuleLsAccountNumbersDTO.id));
        }
        return set1;
    }
}
