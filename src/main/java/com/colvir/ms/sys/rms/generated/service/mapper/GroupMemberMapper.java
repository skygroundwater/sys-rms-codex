package com.colvir.ms.sys.rms.generated.service.mapper;

import com.colvir.ms.sys.rms.generated.domain.GroupMember;
import com.colvir.ms.sys.rms.generated.service.dto.GroupMemberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

/**
 * Mapper for the entity {@link GroupMember} and its DTO {@link GroupMemberDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {RequirementMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMemberMapper {
    @Mapping(source = "requirement.id", target = "requirementId")
    GroupMemberDTO toDto(GroupMember groupMember);

    @Mapping(source = "requirementId", target = "requirement")
    @Mapping(target = "requirementsGroupOfMembers", ignore = true)
    GroupMember toEntity(GroupMemberDTO groupMemberDTO);

    List<GroupMemberDTO> toDto(List<GroupMember> groupMemberList);

    List<GroupMember> toEntity(List<GroupMemberDTO> groupMemberDtoList);

    default GroupMember fromId(Long id) {
        if (id == null) {
            return null;
        }
        return GroupMember.getEntityManager().getReference(GroupMember.class, id);
    }

    default Set<GroupMemberDTO> convertSet(Set<GroupMember> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new GroupMemberDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<GroupMember> setGroupMemberDTOToSetGroupMember(Set<GroupMemberDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<GroupMember> set1 = new LinkedHashSet<GroupMember>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (GroupMemberDTO groupMemberDTO : objSetDto) {
            set1.add(GroupMember.getEntityManager().getReference(GroupMember.class, groupMemberDTO.id));
        }
        return set1;
    }
}
