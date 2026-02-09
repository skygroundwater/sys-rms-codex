package com.colvir.ms.sys.rms.manual.dao;

import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RequirementDao {

    long countByIds(List<Long> ids);

    Requirement findById(Long id);

    Requirement findByIdOrThrow(Long id);

    Requirement getReference(Long id);

    List<Requirement> findActiveByIds(Set<Long> ids);

    List<Requirement> findActiveByContractOrClient(String contractRef, Long clientId);

    List<Requirement> findWaitByBaseDocumentAndBusinessDate(String baseDocument, LocalDate businessDate);

    List<Requirement> findByIdsAndStateWithPriorityLessThan(List<Long> requirementIdList, RequirementStatus state, BigDecimal priority);

    Requirement findOverdueByRequirementIdAndBusinessDate(Long requirementId, LocalDate businessDate);

    void refresh(Requirement requirement);
}
