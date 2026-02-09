package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.CreateRequirementsGroupDto;
import com.colvir.ms.sys.rms.dto.CreateRequirementsGroupJournalDto;
import com.colvir.ms.sys.rms.dto.CreateRequirementsGroupResultDto;
import com.colvir.ms.sys.rms.dto.GroupMemberDto;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.generated.domain.GroupMember;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.RequirementsGroup;
import com.colvir.ms.sys.rms.manual.util.ContextObjectMapper;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CreateGroupHandler extends AbstractStepRunnerHandler<CreateRequirementsGroupDto, CreateRequirementsGroupJournalDto, CreateRequirementsGroupResultDto>{

    @Inject
    public CreateGroupHandler(Logger log) {
        super(StepsNames.SYS_RMS_CREATE_GROUP, log);
    }

    @Override
    public void validateProperties(CreateRequirementsGroupDto properties) {
        if (properties.groupPaymentStrategy == null) {
            throw new RuntimeException("createRequirementsGroupRunner: groupPaymentStrategy should not be empty");
        }
    }

    @Override
    public AggregationResult<CreateRequirementsGroupDto, CreateRequirementsGroupJournalDto, CreateRequirementsGroupResultDto> process(StepMethod.RequestItem.Request<CreateRequirementsGroupDto, CreateRequirementsGroupJournalDto> request) {
        CreateRequirementsGroupJournalDto journal = request.getJournal();
        CreateRequirementsGroupDto properties = request.getProperties();
        CreateRequirementsGroupResultDto result = new CreateRequirementsGroupResultDto();
        List<GroupMemberDto> groupMembers = new ArrayList<>();
        JsonNode groupMembersNode = properties.groupMembers;

        if (!groupMembersNode.isNull() && !groupMembersNode.isMissingNode()) {
            try {
                if (!groupMembersNode.isArray()) {
                    groupMembers.add(
                        ContextObjectMapper.get().treeToValue(groupMembersNode, GroupMemberDto.class)
                    );
                } else {
                    for (var node : groupMembersNode) {
                        groupMembers.add(
                            ContextObjectMapper.get().treeToValue(node, GroupMemberDto.class)
                        );
                    }
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        RequirementsGroup group = new RequirementsGroup();
        group.groupPaymentStrategy = properties.groupPaymentStrategy;
        group.persist();
        result.requirementGroup = new ReferenceDto(group.id, "/SYS/RMS/RequirementsGroup");
        if (!groupMembers.isEmpty()) {
            for (var member : groupMembers) {
                GroupMember newMember = new GroupMember();
                newMember.num = member.num;
                newMember.part = member.part;
                if (member.requirement != null && member.requirement.id != null) {
                    newMember.requirement = Requirement.getEntityManager().getReference(Requirement.class, member.requirement.id);
                } else {
                    throw new RuntimeException("GroupMember 'requirement' should not be empty");
                }
                newMember.persist();
                group.members.add(newMember);
            }
            group.update();
        }

        journal.createdGroupId = result.requirementGroup.id;
        return new AggregationResult<>(properties, journal, result);
    }

    @Override
    public void undo(CreateRequirementsGroupJournalDto journal) {
        if (journal != null && journal.createdGroupId != null) {
            Long groupId = journal.createdGroupId;
            RequirementsGroup group = RequirementsGroup.findById(groupId);
            if (group != null && !Boolean.TRUE.equals(group.isDeleted)) {
                group.isDeleted = true;
                if (group.members != null && !group.members.isEmpty()) {
                    for (GroupMember groupMember : group.members) {
                        groupMember.isDeleted = true;
                        groupMember.update();
                    }
                }
                group.update();
            }
        }
    }
}
