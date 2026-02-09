package com.colvir.ms.sys.rms.generated.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import com.colvir.ms.sys.rms.generated.domain.*;
import com.colvir.ms.sys.rms.generated.service.dto.SysAcc000WithdrawalRuleDTO;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link SysAcc000WithdrawalRule} and its DTO {@link SysAcc000WithdrawalRuleDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {RequirementMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysAcc000WithdrawalRuleMapper {
    @Mapping(source = "requirementOfWithdrawalRules.id", target = "requirementOfWithdrawalRulesId")
    SysAcc000WithdrawalRuleDTO toDto(SysAcc000WithdrawalRule sysAcc000WithdrawalRule);

    @Mapping(source = "requirementOfWithdrawalRulesId", target = "requirementOfWithdrawalRules")
    SysAcc000WithdrawalRule toEntity(SysAcc000WithdrawalRuleDTO sysAcc000WithdrawalRuleDTO);

    List<SysAcc000WithdrawalRuleDTO> toDto(List<SysAcc000WithdrawalRule> sysAcc000WithdrawalRuleList);

    List<SysAcc000WithdrawalRule> toEntity(List<SysAcc000WithdrawalRuleDTO> sysAcc000WithdrawalRuleDtoList);

    default SysAcc000WithdrawalRule fromId(Long id) {
        if (id == null) {
            return null;
        }
        return SysAcc000WithdrawalRule.getEntityManager().getReference(SysAcc000WithdrawalRule.class, id);
    }

    default Set<SysAcc000WithdrawalRuleDTO> convertSet(Set<SysAcc000WithdrawalRule> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new SysAcc000WithdrawalRuleDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<SysAcc000WithdrawalRule> setSysAcc000WithdrawalRuleDTOToSetSysAcc000WithdrawalRule(Set<SysAcc000WithdrawalRuleDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<SysAcc000WithdrawalRule> set1 = new LinkedHashSet<SysAcc000WithdrawalRule>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (SysAcc000WithdrawalRuleDTO sysAcc000WithdrawalRuleDTO : objSetDto) {
            set1.add(SysAcc000WithdrawalRule.getEntityManager().getReference(SysAcc000WithdrawalRule.class, sysAcc000WithdrawalRuleDTO.id));
        }
        return set1;
    }
}
