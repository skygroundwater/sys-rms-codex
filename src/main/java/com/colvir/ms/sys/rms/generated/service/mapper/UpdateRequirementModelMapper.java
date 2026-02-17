package com.colvir.ms.sys.rms.generated.service.mapper;

import com.colvir.ms.sys.rms.generated.domain.UpdateRequirementModel;
import com.colvir.ms.sys.rms.generated.service.dto.UpdateRequirementModelDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

/**
 * Mapper for the entity {@link UpdateRequirementModel} and its DTO {@link UpdateRequirementModelDTO}.
 */
@Mapper(
    componentModel = JAKARTA_CDI,
    uses = {},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UpdateRequirementModelMapper {
    UpdateRequirementModelDTO toDto(UpdateRequirementModel updateRequirementModel);

    UpdateRequirementModel toEntity(UpdateRequirementModelDTO updateRequirementModelDTO);

    List<UpdateRequirementModelDTO> toDto(List<UpdateRequirementModel> updateRequirementModelList);

    List<UpdateRequirementModel> toEntity(List<UpdateRequirementModelDTO> updateRequirementModelDtoList);

    default UpdateRequirementModel fromId(Long id) {
        if (id == null) {
            return null;
        }
        return UpdateRequirementModel.getEntityManager().getReference(UpdateRequirementModel.class, id);
    }

    default Set<UpdateRequirementModelDTO> convertSet(Set<UpdateRequirementModel> empSet) {
        return empSet == null ? null : empSet.stream().map(field -> new UpdateRequirementModelDTO(field.id)).collect(Collectors.toSet());
    }

    default Set<UpdateRequirementModel> setUpdateRequirementModelDTOToSetUpdateRequirementModel(Set<UpdateRequirementModelDTO> objSetDto) {
        if (objSetDto == null) {
            return null;
        }
        Set<UpdateRequirementModel> set1 = new LinkedHashSet<UpdateRequirementModel>(Math.max((int) (objSetDto.size() / .75f) + 1, 16));
        for (UpdateRequirementModelDTO updateRequirementModelDTO : objSetDto) {
            set1.add(UpdateRequirementModel.getEntityManager().getReference(UpdateRequirementModel.class, updateRequirementModelDTO.id));
        }
        return set1;
    }
}
