package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.AggregationResult;
import com.colvir.ms.sys.rms.dto.GroupMemberAddDto;
import com.colvir.ms.sys.rms.dto.GroupMemberAddJournalDto;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.generated.domain.GroupMember;
import com.colvir.ms.sys.rms.generated.domain.RequirementsGroup;
import com.colvir.ms.sys.rms.generated.domain.enumeration.GroupPaymentStrategy;
import com.colvir.ms.sys.rms.manual.dao.GroupMemberDao;
import com.colvir.ms.sys.rms.manual.dao.RequirementDao;
import com.colvir.ms.sys.rms.manual.service.RequirementGroupService;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupMemberAddHandlerTest {

    @Mock
    RequirementGroupService requirementGroupService;

    @Mock
    GroupMemberDao groupMemberDao;

    @Mock
    RequirementDao requirementDao;

    @Mock
    Logger log;

    @InjectMocks
    GroupMemberAddHandler handler;

    @Test
    void validatePropertiesThrowsWhenRequirementMissing() {
        GroupMemberAddDto dto = new GroupMemberAddDto();
        dto.requirementGroup = new ReferenceDto(1L, "RequirementsGroup");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> handler.validateProperties(dto));
        assertEquals("groupMemberAddRunner: 'requirement' value should not be empty", ex.getMessage());
    }

    @Test
    void validatePropertiesThrowsWhenRequirementGroupMissing() {
        GroupMemberAddDto dto = new GroupMemberAddDto();
        dto.requirement = new ReferenceDto(1L, "Requirement");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> handler.validateProperties(dto));
        assertEquals("groupMemberAddRunner: 'requirementGroup' value should not be empty", ex.getMessage());
    }

    @Test
    void processReturnsJournalWhenMemberAlreadyExists() {
        GroupMemberAddDto dto = new GroupMemberAddDto();
        dto.requirementGroup = new ReferenceDto(10L, "RequirementsGroup");
        dto.requirement = new ReferenceDto(20L, "Requirement");

        RequirementsGroup group = new RequirementsGroup();
        group.id = 10L;
        group.groupPaymentStrategy = GroupPaymentStrategy.FULL;
        group.members = new HashSet<>();
        GroupMember existing = new GroupMember();
        existing.isDeleted = false;
        existing.requirement = new com.colvir.ms.sys.rms.generated.domain.Requirement();
        existing.requirement.id = 20L;
        group.members.add(existing);

        when(requirementGroupService.findRequirementsGroupById(10L)).thenReturn(group);

        @SuppressWarnings("unchecked")
        StepMethod.RequestItem.Request<GroupMemberAddDto, GroupMemberAddJournalDto> request =
            (StepMethod.RequestItem.Request<GroupMemberAddDto, GroupMemberAddJournalDto>) Mockito.mock(StepMethod.RequestItem.Request.class);
        when(request.getProperties()).thenReturn(dto);

        AggregationResult<GroupMemberAddDto, GroupMemberAddJournalDto, Object> result = handler.process(request);

        assertEquals(10L, result.getJournal().groupId);
        assertNull(result.getJournal().addedGroupMemberId);
        verify(groupMemberDao, never()).countActiveByRequirementId(anyLong());
        verify(requirementDao, never()).getReference(anyLong());
    }

    @Test
    void addMemberThrowsWhenRequirementAlreadyInAnotherGroup() {
        GroupMemberAddDto dto = new GroupMemberAddDto();
        dto.requirementGroup = new ReferenceDto(10L, "RequirementsGroup");
        dto.requirement = new ReferenceDto(20L, "Requirement");

        RequirementsGroup group = new RequirementsGroup();
        group.id = 10L;
        group.groupPaymentStrategy = GroupPaymentStrategy.FULL;
        group.members = new HashSet<>();

        when(requirementGroupService.findRequirementsGroupById(10L)).thenReturn(group);
        when(groupMemberDao.countActiveByRequirementId(20L)).thenReturn(1L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> handler.addMember(dto));
        assertEquals("Requirement (Requirement:20) is already included in another group", ex.getMessage());
    }

    @Test
    void undoMarksGroupMemberAsDeleted() {
        GroupMemberAddJournalDto journal = new GroupMemberAddJournalDto();
        journal.groupId = 10L;
        journal.addedGroupMemberId = 30L;

        GroupMember member = Mockito.spy(new GroupMember());
        member.id = 30L;
        member.isDeleted = false;
        doReturn(member).when(member).update();

        when(groupMemberDao.findById(30L)).thenReturn(member);

        handler.undo(journal);

        assertEquals(true, member.isDeleted);
        verify(member).update();
    }
}
