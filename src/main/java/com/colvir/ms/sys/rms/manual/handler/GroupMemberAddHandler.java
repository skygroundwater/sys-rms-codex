package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.GroupMemberAddDto;
import com.colvir.ms.sys.rms.dto.GroupMemberAddJournalDto;
import com.colvir.ms.sys.rms.generated.domain.GroupMember;
import com.colvir.ms.sys.rms.generated.domain.RequirementsGroup;
import com.colvir.ms.sys.rms.manual.dao.GroupMemberDao;
import com.colvir.ms.sys.rms.manual.dao.RequirementDao;
import com.colvir.ms.sys.rms.generated.domain.enumeration.GroupPaymentStrategy;
import com.colvir.ms.sys.rms.manual.service.RequirementGroupService;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GroupMemberAddHandler extends AbstractStepRunnerHandler<GroupMemberAddDto, GroupMemberAddJournalDto, Object> {

    RequirementGroupService requirementGroupService;

    GroupMemberDao groupMemberDao;

    RequirementDao requirementDao;

    @Inject
    public GroupMemberAddHandler(RequirementGroupService requirementGroupService,
                                 GroupMemberDao groupMemberDao,
                                 RequirementDao requirementDao,
                                 Logger log) {
        super(StepsNames.SYS_RMS_GROUP_MEMBER_ADD, log);
        this.requirementGroupService = requirementGroupService;
        this.groupMemberDao = groupMemberDao;
        this.requirementDao = requirementDao;
    }

    @Override
    public void validateProperties(GroupMemberAddDto properties) {
        if (properties.requirement == null || properties.requirement.id == null) {
            throw new RuntimeException("groupMemberAddRunner: 'requirement' value should not be empty");
        }
        if (properties.requirementGroup == null || properties.requirementGroup.id == null) {
            throw new RuntimeException("groupMemberAddRunner: 'requirementGroup' value should not be empty");
        }
    }

    @Override
    public AggregationResult<GroupMemberAddDto, GroupMemberAddJournalDto, Object> process(StepMethod.RequestItem.Request<GroupMemberAddDto, GroupMemberAddJournalDto> request) {
        GroupMemberAddDto properties = request.getProperties();

        GroupMemberAddJournalDto journal = this.addMember(properties);
        return new AggregationResult<>(journal, List.of());
    }

    @Override
    public void undo(GroupMemberAddJournalDto journal) {
        if (journal != null) {
            if (journal.groupId != null && journal.addedGroupMemberId != null) {
                GroupMember groupMember = groupMemberDao.findById(journal.addedGroupMemberId);
                if (groupMember != null && !Boolean.TRUE.equals(groupMember.isDeleted)) {
                    groupMember.isDeleted = true;
                    groupMember.update();
                }
            }
        }
    }

    public GroupMemberAddJournalDto addMember(GroupMemberAddDto request) {
        GroupMemberAddJournalDto journal = new GroupMemberAddJournalDto();
        RequirementsGroup group = requirementGroupService.findRequirementsGroupById(request.requirementGroup.id);
        journal.groupId = group.id;

        // требование уже включено в эту же группу
        if (group.members != null && !group.members.isEmpty()) {
            Optional<GroupMember> foundGroupMember = group.members.stream()
                .filter(
                    g -> !Boolean.TRUE.equals(g.isDeleted) && request.requirement.id.equals(g.requirement.id)
                )
                .findFirst();
            if (foundGroupMember.isPresent()) {
                return journal;
            }
        }
        if (!GroupPaymentStrategy.INAPPLICABLE.equals(group.groupPaymentStrategy)) {
            // требование может входить не более чем в одну группу
            // за исключением групп, в которых правило оплаты = "не применяется"
            long countExisting = groupMemberDao.countActiveByRequirementId(request.requirement.id);
            if (countExisting > 0) {
                throw new RuntimeException(String.format("Requirement (%s) is already included in another group", request.requirement));
            }
        }

        GroupMember newMember = new GroupMember();
        newMember.num = request.num;
        newMember.part = request.part;
        newMember.requirement = requirementDao.getReference(request.requirement.id);
        newMember.persist();
        journal.addedGroupMemberId = newMember.id;
        group.members.add(newMember);
        group.update();
        return journal;
    }

}
