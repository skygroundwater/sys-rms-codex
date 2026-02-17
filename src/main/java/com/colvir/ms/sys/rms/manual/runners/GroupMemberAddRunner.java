package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.GroupMemberAddDto;
import com.colvir.ms.sys.rms.dto.GroupMemberAddJournalDto;
import com.colvir.ms.sys.rms.manual.constant.StepsNames;
import com.colvir.ms.sys.rms.manual.handler.GroupMemberAddHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@Step(StepsNames.SYS_RMS_GROUP_MEMBER_ADD)
@ApplicationScoped
public class GroupMemberAddRunner implements StepRunner<GroupMemberAddDto, GroupMemberAddJournalDto, Object> {

    @Inject
    GroupMemberAddHandler handler;

    @Override
    public ProcessStageResponse<GroupMemberAddJournalDto, Object> process(RequestItem.Request<GroupMemberAddDto, GroupMemberAddJournalDto> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<GroupMemberAddJournalDto> compensate(RequestItem.Request<GroupMemberAddDto, GroupMemberAddJournalDto> request) {
        return handler.undoHandle(request);
    }

}
