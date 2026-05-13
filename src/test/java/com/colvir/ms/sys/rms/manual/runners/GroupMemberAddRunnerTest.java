package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.GroupMemberAddDto;
import com.colvir.ms.sys.rms.dto.GroupMemberAddJournalDto;
import com.colvir.ms.sys.rms.manual.handler.GroupMemberAddHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupMemberAddRunnerTest {

    @Mock
    GroupMemberAddHandler handler;

    @InjectMocks
    GroupMemberAddRunner runner;

    @Test
    void processDelegatesToHandler() {
        @SuppressWarnings("unchecked")
        StepMethod.RequestItem.Request<GroupMemberAddDto, GroupMemberAddJournalDto> request =
            (StepMethod.RequestItem.Request<GroupMemberAddDto, GroupMemberAddJournalDto>) Mockito.mock(StepMethod.RequestItem.Request.class);
        ProcessStageResponse<GroupMemberAddJournalDto, Object> response = Mockito.mock(ProcessStageResponse.class);
        when(handler.handle(request)).thenReturn(response);

        runner.process(request);

        verify(handler).handle(request);
    }

    @Test
    void compensateDelegatesToHandler() {
        @SuppressWarnings("unchecked")
        StepMethod.RequestItem.Request<GroupMemberAddDto, GroupMemberAddJournalDto> request =
            (StepMethod.RequestItem.Request<GroupMemberAddDto, GroupMemberAddJournalDto>) Mockito.mock(StepMethod.RequestItem.Request.class);
        CompensateStageResponse<GroupMemberAddJournalDto> response = Mockito.mock(CompensateStageResponse.class);
        when(handler.undoHandle(request)).thenReturn(response);

        runner.compensate(request);

        verify(handler).undoHandle(request);
    }
}
