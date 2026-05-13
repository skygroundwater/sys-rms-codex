package com.colvir.ms.sys.rms.manual.service;

import com.colvir.ms.sys.rms.dto.BbpStateResult;
import com.colvir.ms.sys.rms.dto.BuildRequirementsDto;
import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RequirementAmountResponse;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementJournalDto;
import com.colvir.ms.sys.rms.dto.ReviewRequirementResponse;
import com.colvir.ms.sys.rms.generated.domain.Requirement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RequirementService {

    List<RequirementStateInfoDto> createRequirements(BuildRequirementsDto request, List<String> initialBbpStates);

    void checkBuildRequirements(BuildRequirementsDto request);

    void deleteRequirements(List<Long> requirementIdList);

    void restoreWithoutBbpCancelExecution(List<RequirementJournalDto> requirementJournal,
                                          List<Long> createdPayments,
                                          List<Long> createdRelatedPayments,
                                          List<Long> createdRefundingPayments,
                                          List<Long> createdReqRefundingPayments);

    void updateRequirementsUndo (List<RequirementJournalDto> journal);

    ReviewRequirementResponse requirementReview (ReviewRequirementDto request);

    void requirementReviewUndo (ReviewRequirementJournalDto request);

    void updateRequirementBbpStates(List<RequirementJournalDto> values, Map<String, BbpStateResult> processIdStateMap);

    Requirement getRequirementById(Long id);

    List<Requirement> getRequirementsByIds(Set<Long> ids);

    // получение оплаченной суммы
    // предполагается что все требования в валюте договора
    BigDecimal getPaidAmount(ReferenceDto requirement, ReferenceDto contract, ReferenceDto client);

    // получение оплаченной суммы в разбивке по расчетным категориям
    // предполагается что все требования в валюте договора
    RequirementAmountResponse getPaidAmounts(ReferenceDto requirement, ReferenceDto contract, ReferenceDto client);

    // получение неоплаченной суммы
    BigDecimal getUnpaidAmount(ReferenceDto requirement, ReferenceDto contract, ReferenceDto client);

    // получение неоплаченной суммы в разбивке по расчетным категориям
    // предполагается что все требования в валюте договора
    RequirementAmountResponse getUnpaidAmounts(ReferenceDto requirement, ReferenceDto contract, ReferenceDto client);

    // получение признака просрочки на дату
    Boolean getIsOverdue(ReferenceDto requirement, LocalDate businessDate);

    // получение даты начала просрочки
    LocalDate getOverdueStartDate(ReferenceDto requirement, LocalDate businessDate);

    // получение даты окончания просрочки
    LocalDate getOverdueEndDate (ReferenceDto requirement, LocalDate businessDate);
}
