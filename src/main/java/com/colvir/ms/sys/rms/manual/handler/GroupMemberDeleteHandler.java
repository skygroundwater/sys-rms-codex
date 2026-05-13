package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.GroupMemberDeleteDto;
import com.colvir.ms.sys.rms.dto.GroupMemberDeleteJournalDto;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.generated.domain.GroupMember;
import com.colvir.ms.sys.rms.generated.domain.RequirementsGroup;
import com.colvir.ms.sys.rms.manual.dao.GroupMemberDao;
import com.colvir.ms.sys.rms.manual.service.RequirementGroupService;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GroupMemberDeleteHandler extends AbstractStepRunnerHandler<GroupMemberDeleteDto, GroupMemberDeleteJournalDto, Object>{

    RequirementGroupService requirementGroupService;

    GroupMemberDao groupMemberDao;

    @Inject
    public GroupMemberDeleteHandler(RequirementGroupService requirementGroupService,
                                    GroupMemberDao groupMemberDao,
                                    Logger log) {
        super(StepsNames.SYS_RMS_GROUP_MEMBER_DELETE, log);
        this.requirementGroupService = requirementGroupService;
        this.groupMemberDao = groupMemberDao;
    }

    @Override
    public void validateProperties(GroupMemberDeleteDto properties) {
        if (properties.requirement == null || properties.requirement.id == null) {
            throw new RuntimeException("groupMemberDeleteRunner: 'requirement' value should not be empty");
        }
    }

    @Override
    public AggregationResult<GroupMemberDeleteDto, GroupMemberDeleteJournalDto, Object> process(StepMethod.RequestItem.Request<GroupMemberDeleteDto, GroupMemberDeleteJournalDto> request) {
        GroupMemberDeleteJournalDto journal = request.getJournal();
        GroupMemberDeleteDto properties = request.getProperties();
        GroupMemberDeleteDto deleteRequest = new GroupMemberDeleteDto();
        deleteRequest.requirement = properties.requirement;
        // если не передана группа, пробуем определить ее по требованию
        if (properties.requirementGroup == null || properties.requirementGroup.id == null) {
            List<RequirementsGroup> requirementGroups = requirementGroupService.getRequirementGroups(properties.requirement.id);
            if (requirementGroups.isEmpty()) {
                log.infof("--- rms-group-member-del --- return on: requirement (%s) is not included in any group", properties.requirement.id);
                return new AggregationResult<>(journal, List.of());
            } else if (requirementGroups.size() > 1) {
                throw new RuntimeException(String.format("groupMemberDeleteRunner: Requirement (%s) is included in several groups. Choose a 'requirementGroup' value", properties.requirement.id));
            } else {
                deleteRequest.requirementGroup = new ReferenceDto(requirementGroups.getFirst().id, "/SYS/RMS/RequirementsGroup");
            }
        } else {
            deleteRequest.requirementGroup = properties.requirementGroup;
        }
        journal = this.deleteMember(deleteRequest);
        return new AggregationResult<>(journal, List.of());
    }

    @Override
    public void undo(GroupMemberDeleteJournalDto journal) {
        if (journal != null) {
            if (journal.groupId != null && journal.deletedGroupMemberId != null) {
                GroupMember groupMember = groupMemberDao.findById(journal.deletedGroupMemberId);
                if (groupMember != null && Boolean.TRUE.equals(groupMember.isDeleted)) {
                    groupMember.isDeleted = false;
                    groupMember.update();
                }
            }
        }
    }

    private GroupMemberDeleteJournalDto deleteMember (GroupMemberDeleteDto request) {
        GroupMemberDeleteJournalDto journal = new GroupMemberDeleteJournalDto();

        RequirementsGroup group = requirementGroupService.findRequirementsGroupById(request.requirementGroup.id);
        journal.groupId = group.id;

        if (group.members != null && !group.members.isEmpty()) {
            // в группе не может быть двух одинаковых требований
            Optional<GroupMember> foundGroupMember = group.members.stream()
                .filter(
                    g -> !Boolean.TRUE.equals(g.isDeleted) && request.requirement.id.equals(g.requirement.id)
                )
                .findFirst();
            if (foundGroupMember.isPresent()) {
                GroupMember groupMember = foundGroupMember.get();
                journal.deletedGroupMemberId = groupMember.id;
                groupMember.isDeleted = true;
                groupMember.update();
            }
        }
        return journal;
    }

}
