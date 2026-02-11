package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.annotation.Step;
import com.colvir.ms.sys.opr.api.step.runner.StepRunner;
import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.opr.api.step.runner.method.response.CompensateStageResponse;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.AdjustByPastDateDto;
import com.colvir.ms.sys.rms.dto.AdjustByPastDateJournalDto;
import com.colvir.ms.sys.rms.dto.AdjustByPastDateResultDto;
import com.colvir.ms.sys.rms.dto.BuildRequirementsDto;
import com.colvir.ms.sys.rms.dto.BuildRequirementsJournalDto;
import com.colvir.ms.sys.rms.dto.BuildRequirementsResultDto;
import com.colvir.ms.sys.rms.dto.CreateRequirementsGroupDto;
import com.colvir.ms.sys.rms.dto.CreateRequirementsGroupJournalDto;
import com.colvir.ms.sys.rms.dto.CreateRequirementsGroupResultDto;
import com.colvir.ms.sys.rms.dto.DistributePaidAmountsDto;
import com.colvir.ms.sys.rms.dto.DistributePaidAmountsJournalDto;
import com.colvir.ms.sys.rms.dto.DistributePaidAmountsResultDto;
import com.colvir.ms.sys.rms.dto.GroupMemberAddDto;
import com.colvir.ms.sys.rms.dto.GroupMemberAddJournalDto;
import com.colvir.ms.sys.rms.dto.GroupMemberDeleteDto;
import com.colvir.ms.sys.rms.dto.GroupMemberDeleteJournalDto;
import com.colvir.ms.sys.rms.dto.JournalDto;
import com.colvir.ms.sys.rms.dto.NonPaidRequirementsDto;
import com.colvir.ms.sys.rms.dto.NonPaidRequirementsResultDto;
import com.colvir.ms.sys.rms.dto.PaymentRegistrationDto;
import com.colvir.ms.sys.rms.dto.PaymentRegistrationJournalDto;
import com.colvir.ms.sys.rms.dto.PaymentRegistrationResultDto;
import com.colvir.ms.sys.rms.dto.QueueCheckDto;
import com.colvir.ms.sys.rms.dto.QueueCheckResultDto;
import com.colvir.ms.sys.rms.dto.RefundJournalDto;
import com.colvir.ms.sys.rms.dto.RefundOfPaymentResultDto;
import com.colvir.ms.sys.rms.dto.RefundOfPaymentRunnerDto;
import com.colvir.ms.sys.rms.dto.RefundOfRequirementsResultDto;
import com.colvir.ms.sys.rms.dto.RefundOfRequirementsRunnerDto;
import com.colvir.ms.sys.rms.dto.RequirementReviewDto;
import com.colvir.ms.sys.rms.dto.RequirementReviewJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementReviewResultDto;
import com.colvir.ms.sys.rms.dto.SetHoldDto;
import com.colvir.ms.sys.rms.dto.SetHoldJournalDto;
import com.colvir.ms.sys.rms.dto.SetHoldResultDto;
import com.colvir.ms.sys.rms.dto.UpdateHoldDto;
import com.colvir.ms.sys.rms.dto.UpdateHoldJournalDto;
import com.colvir.ms.sys.rms.dto.UpdateHoldResultDto;
import com.colvir.ms.sys.rms.dto.UpdateRequirementsDto;
import com.colvir.ms.sys.rms.dto.UpdateRequirementsResultDto;
import com.colvir.ms.sys.rms.dto.UpdateSingleRequirementDto;
import com.colvir.ms.sys.rms.dto.UpdateSingleRequirementJournalDto;
import com.colvir.ms.sys.rms.dto.UpdateSingleRequirementResultDto;
import com.colvir.ms.sys.rms.dto.WriteOffDto;
import com.colvir.ms.sys.rms.dto.WriteOffJournalDto;
import com.colvir.ms.sys.rms.dto.WriteOffResultDto;
import com.colvir.ms.sys.rms.manual.handler.AbstractStepRunnerHandler;
import com.colvir.ms.sys.rms.manual.handler.AdjustByPastDateHandler;
import com.colvir.ms.sys.rms.manual.handler.BuildRequirementsHandler;
import com.colvir.ms.sys.rms.manual.handler.CreateGroupHandler;
import com.colvir.ms.sys.rms.manual.handler.DistributePaidAmountsHandler;
import com.colvir.ms.sys.rms.manual.handler.GroupMemberAddHandler;
import com.colvir.ms.sys.rms.manual.handler.GroupMemberDeleteHandler;
import com.colvir.ms.sys.rms.manual.handler.NonPaidRequirementsHandler;
import com.colvir.ms.sys.rms.manual.handler.PaymentRegistrationHandler;
import com.colvir.ms.sys.rms.manual.handler.QueueCheckHandler;
import com.colvir.ms.sys.rms.manual.handler.RefundOfPaymentHandler;
import com.colvir.ms.sys.rms.manual.handler.RefundOfRequirementsHandler;
import com.colvir.ms.sys.rms.manual.handler.RequirementReviewHandler;
import com.colvir.ms.sys.rms.manual.handler.SetHoldHandler;
import com.colvir.ms.sys.rms.manual.handler.UpdateHoldHandler;
import com.colvir.ms.sys.rms.manual.handler.UpdateRequirementsHandler;
import com.colvir.ms.sys.rms.manual.handler.UpdateSingleRequirementHandler;
import com.colvir.ms.sys.rms.manual.handler.WriteOffHandler;
import com.colvir.ms.sys.rms.manual.util.StepsNames;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

public final class ManualStepRunnersConfig {
    private ManualStepRunnersConfig() {
    }
}

abstract class DelegatingStepRunner<P, J extends JournalDto, R> implements StepRunner<P, J, R> {

    private final AbstractStepRunnerHandler<P, J, R> handler;

    protected DelegatingStepRunner(AbstractStepRunnerHandler<P, J, R> handler) {
        this.handler = handler;
    }

    @Override
    public ProcessStageResponse<J, R> process(StepMethod.RequestItem.Request<P, J> request) {
        return handler.handle(request);
    }

    @Override
    public CompensateStageResponse<J> compensate(StepMethod.RequestItem.Request<P, J> request) {
        return handler.undoHandle(request);
    }
}

@Step(StepsNames.SYS_RMS_ADJUST_BY_PAST_DATE)
@ApplicationScoped
class AdjustByPastDateRunner extends DelegatingStepRunner<AdjustByPastDateDto, AdjustByPastDateJournalDto, AdjustByPastDateResultDto> {
    @Inject
    AdjustByPastDateRunner(AdjustByPastDateHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_BUILD_REQUIREMENTS)
@ApplicationScoped
class BuildRequirementsRunner extends DelegatingStepRunner<BuildRequirementsDto, BuildRequirementsJournalDto, BuildRequirementsResultDto> {
    @Inject
    BuildRequirementsRunner(BuildRequirementsHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_CREATE_GROUP)
@ApplicationScoped
class CreateGroupRunner extends DelegatingStepRunner<CreateRequirementsGroupDto, CreateRequirementsGroupJournalDto, CreateRequirementsGroupResultDto> {
    @Inject
    CreateGroupRunner(CreateGroupHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_DISTRIBUTE_PAID_AMOUNTS)
@ApplicationScoped
class DistributePaidAmountsRunner extends DelegatingStepRunner<DistributePaidAmountsDto, DistributePaidAmountsJournalDto, DistributePaidAmountsResultDto> {
    @Inject
    DistributePaidAmountsRunner(DistributePaidAmountsHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_GROUP_MEMBER_ADD)
@ApplicationScoped
class GroupMemberAddRunner extends DelegatingStepRunner<GroupMemberAddDto, GroupMemberAddJournalDto, Object> {
    @Inject
    GroupMemberAddRunner(GroupMemberAddHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_GROUP_MEMBER_DELETE)
@ApplicationScoped
class GroupMemberDeleteRunner extends DelegatingStepRunner<GroupMemberDeleteDto, GroupMemberDeleteJournalDto, Object> {
    @Inject
    GroupMemberDeleteRunner(GroupMemberDeleteHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_GET_NON_PAID)
@ApplicationScoped
class NonPaidRequirementsRunner extends DelegatingStepRunner<NonPaidRequirementsDto, JournalDto, NonPaidRequirementsResultDto> {
    @Inject
    NonPaidRequirementsRunner(NonPaidRequirementsHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_PAYMENT_REGISTRATION)
@ApplicationScoped
class PaymentRegistrationRunner extends DelegatingStepRunner<PaymentRegistrationDto, PaymentRegistrationJournalDto, PaymentRegistrationResultDto> {
    @Inject
    PaymentRegistrationRunner(PaymentRegistrationHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_QUEUE_CHECK)
@ApplicationScoped
class QueueCheckRunner extends DelegatingStepRunner<QueueCheckDto, JournalDto, QueueCheckResultDto> {
    @Inject
    QueueCheckRunner(QueueCheckHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_REFUND_PAYMENT)
@ApplicationScoped
class RefundOfPaymentRunner extends DelegatingStepRunner<RefundOfPaymentRunnerDto, RefundJournalDto, RefundOfPaymentResultDto> {
    @Inject
    RefundOfPaymentRunner(RefundOfPaymentHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_REFUND_REQUIREMENTS)
@ApplicationScoped
class RefundOfRequirementsRunner extends DelegatingStepRunner<RefundOfRequirementsRunnerDto, RefundJournalDto, RefundOfRequirementsResultDto> {
    @Inject
    RefundOfRequirementsRunner(RefundOfRequirementsHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_REVIEW)
@ApplicationScoped
class RequirementReviewRunner extends DelegatingStepRunner<RequirementReviewDto, RequirementReviewJournalDto, RequirementReviewResultDto> {
    @Inject
    RequirementReviewRunner(RequirementReviewHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_SET_HOLD)
@ApplicationScoped
class SetHoldRunner extends DelegatingStepRunner<SetHoldDto, SetHoldJournalDto, SetHoldResultDto> {
    @Inject
    SetHoldRunner(SetHoldHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_UPDATE_HOLD)
@ApplicationScoped
class UpdateHoldRunner extends DelegatingStepRunner<UpdateHoldDto, UpdateHoldJournalDto, UpdateHoldResultDto> {
    @Inject
    UpdateHoldRunner(UpdateHoldHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_UPDATE_REQUIREMENTS)
@ApplicationScoped
class UpdateRequirementsRunner extends DelegatingStepRunner<UpdateRequirementsDto, JournalDto, UpdateRequirementsResultDto> {
    @Inject
    UpdateRequirementsRunner(UpdateRequirementsHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_UPDATE_SINGLE_REQUIREMENT)
@ApplicationScoped
class UpdateSingleRequirementRunner extends DelegatingStepRunner<UpdateSingleRequirementDto, UpdateSingleRequirementJournalDto, UpdateSingleRequirementResultDto> {
    @Inject
    UpdateSingleRequirementRunner(UpdateSingleRequirementHandler handler) {
        super(handler);
    }
}

@Step(StepsNames.SYS_RMS_WRITE_OFF)
@ApplicationScoped
class WriteOffRunner extends DelegatingStepRunner<WriteOffDto, WriteOffJournalDto, WriteOffResultDto> {
    @Inject
    WriteOffRunner(WriteOffHandler handler) {
        super(handler);
    }
}
