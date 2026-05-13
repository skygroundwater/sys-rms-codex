package com.colvir.ms.sys.rms.manual.runners;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.opr.api.step.runner.method.response.ProcessStageResponse;
import com.colvir.ms.sys.rms.dto.JournalDto;
import com.colvir.ms.sys.rms.dto.NonPaidRequirementsDto;
import com.colvir.ms.sys.rms.dto.NonPaidRequirementsResultDto;
import com.colvir.ms.sys.rms.manual.handler.NonPaidRequirementsHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NonPaidRequirementsRunnerTest {

    @Mock
    NonPaidRequirementsHandler handler;

    @InjectMocks
    NonPaidRequirementsRunner runner;

    @Test
    void processDelegatesToHandler() {
        @SuppressWarnings("unchecked")
        StepMethod.RequestItem.Request<NonPaidRequirementsDto, JournalDto> request =
            (StepMethod.RequestItem.Request<NonPaidRequirementsDto, JournalDto>) Mockito.mock(StepMethod.RequestItem.Request.class);
        ProcessStageResponse<JournalDto, NonPaidRequirementsResultDto> response = Mockito.mock(ProcessStageResponse.class);
        when(handler.handle(request)).thenReturn(response);

        runner.process(request);

        verify(handler).handle(request);
    }
}
