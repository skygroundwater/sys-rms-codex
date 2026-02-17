package com.colvir.ms.sys.rms.generated.service.mapper;

import com.colvir.ms.sys.rms.generated.domain.MonitoringRule;
import com.colvir.ms.sys.rms.generated.service.dto.MonitoringRuleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

/**
 * Mapper for the entity {@link MonitoringRule} and its DTO {@link MonitoringRuleDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MonitoringRuleMapper {
    MonitoringRuleDTO toDto(MonitoringRule monitoringRule);

    MonitoringRule toEntity(MonitoringRuleDTO monitoringRuleDTO);

    List<MonitoringRuleDTO> toDto(List<MonitoringRule> monitoringRuleList);

    List<MonitoringRule> toEntity(List<MonitoringRuleDTO> monitoringRuleDtoList);

    default MonitoringRule fromId(Long id) {
        if (id == null) {
            return null;
        }
        return MonitoringRule.getEntityManager().getReference(MonitoringRule.class, id);
    }

    default Set<MonitoringRuleDTO> convertSet(Set<MonitoringRule> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new MonitoringRuleDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<MonitoringRule> setMonitoringRuleDTOToSetMonitoringRule(Set<MonitoringRuleDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<MonitoringRule> set1 = new LinkedHashSet<MonitoringRule>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (MonitoringRuleDTO monitoringRuleDTO : objSetDto) {
            set1.add(MonitoringRule.getEntityManager().getReference(MonitoringRule.class, monitoringRuleDTO.id));
        }
        return set1;
    }
}
