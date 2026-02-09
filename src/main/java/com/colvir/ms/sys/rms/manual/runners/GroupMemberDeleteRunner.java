package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.GroupMemberDeleteDto;
import com.colvir.ms.sys.rms.dto.GroupMemberDeleteJournalDto;
import com.colvir.ms.sys.rms.manual.handler.GroupMemberDeleteHandler;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Step(StepsNames.SYS_RMS_GROUP_MEMBER_DELETE)
@ApplicationScoped
public class GroupMemberDeleteRunner implements StepRunner<GroupMemberDeleteDto, GroupMemberDeleteJournalDto, Object> {

    @Inject
    GroupMemberDeleteHandler handler;

    @Override
    public ProcessStageResponse<GroupMemberDeleteJournalDto, Object> process(RequestItem.Request<GroupMemberDeleteDto, GroupMemberDeleteJournalDto> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<GroupMemberDeleteJournalDto> compensate(RequestItem.Request<GroupMemberDeleteDto, GroupMemberDeleteJournalDto> request) {
        return handler.undoHandle(request);
    }
}
