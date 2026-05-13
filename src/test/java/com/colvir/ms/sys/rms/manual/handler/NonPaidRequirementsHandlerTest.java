package com.colvir.ms.sys.rms.manual.handler;

import com.colvir.ms.sys.opr.api.step.runner.method.StepMethod;
import com.colvir.ms.sys.rms.dto.JournalDto;
import com.colvir.ms.sys.rms.dto.NonPaidRequirementsDto;
import com.colvir.ms.sys.rms.dto.NonPaidRequirementsResultDto;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RequirementIndicatorDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.manual.dao.RequirementDao;
import com.colvir.ms.sys.rms.manual.service.RequirementRouterService;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NonPaidRequirementsHandlerTest {

    @Mock
    RequirementRouterService requirementRouterService;

    @Mock
    RequirementDao requirementDao;

    @Mock
    Logger log;

    @InjectMocks
    NonPaidRequirementsHandler handler;

    @Test
    void validatePropertiesThrowsWhenContractMissing() {
        NonPaidRequirementsDto dto = new NonPaidRequirementsDto();
        dto.businessDate = LocalDate.now();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> handler.validateProperties(dto));
        assertEquals("Contract value should not be empty", ex.getMessage());
    }

    @Test
    void validatePropertiesThrowsWhenBusinessDateMissing() {
        NonPaidRequirementsDto dto = new NonPaidRequirementsDto();
        dto.contract = new ReferenceDto(1L, "Contract");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> handler.validateProperties(dto));
        assertEquals("BusinessDate value should not be empty", ex.getMessage());
    }

    @Test
    void processAggregatesUnpaidAmountAndMapsRequirements() {
        NonPaidRequirementsDto dto = new NonPaidRequirementsDto();
        dto.contract = new ReferenceDto(777L, "Contract");
        dto.businessDate = LocalDate.of(2026, 3, 17);

        Requirement r1 = new Requirement();
        r1.id = 1L;
        r1.unpaidAmount = new BigDecimal("10.50");
        r1.priority = new BigDecimal("2.25");
        r1.indicatorId = 100L;
        r1.state = RequirementStatus.WAIT;
        r1.amount = new BigDecimal("50.00");
        r1.paidAmount = new BigDecimal("5.00");

        Requirement r2 = new Requirement();
        r2.id = 2L;
        r2.unpaidAmount = new BigDecimal("20.00");
        r2.priority = new BigDecimal("3.10");
        r2.indicatorId = 200L;
        r2.state = RequirementStatus.WAIT;
        r2.amount = new BigDecimal("100.00");
        r2.paidAmount = new BigDecimal("0.00");

        when(requirementDao.findWaitByBaseDocumentAndBusinessDate("Contract:777", dto.businessDate))
            .thenReturn(List.of(r1, r2));

        RequirementIndicatorDto indicator = new RequirementIndicatorDto();
        when(requirementRouterService.getRequirementIndicator(Mockito.anyLong())).thenReturn(indicator);

        @SuppressWarnings("unchecked")
        StepMethod.RequestItem.Request<NonPaidRequirementsDto, JournalDto> request =
            (StepMethod.RequestItem.Request<NonPaidRequirementsDto, JournalDto>) Mockito.mock(StepMethod.RequestItem.Request.class);
        when(request.getProperties()).thenReturn(dto);
        when(request.getJournal()).thenReturn(new JournalDto());

        NonPaidRequirementsResultDto result = handler.process(request).getResult();

        assertEquals(new BigDecimal("30.50"), result.unpaidAmount);
        assertEquals(2, result.requirements.size());

        RequirementStateInfoDto first = result.requirements.get(0);
        assertEquals(RequirementAction.SAVE, first.action);
        assertEquals(r1.id, first.requirementId);
        assertEquals(indicator, first.indicator);
    }
}
