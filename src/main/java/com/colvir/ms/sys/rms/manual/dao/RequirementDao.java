package com.colvir.ms.sys.rms.manual.dao;

import com.colvir.ms.sys.rms.generated.domain.Requirement;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RequirementDao {

    long countByIds(List<Long> ids);

    Requirement findById(Long id);

    Requirement findByIdOrThrow(Long id, String messageTemplate);

    List<Requirement> findActiveByIds(Set<Long> ids);

    List<Requirement> findActiveByContractOrClient(String contractRef, Long clientId);

    Requirement findOverdueByRequirementIdAndBusinessDate(Long requirementId, LocalDate businessDate);

    void refresh(Requirement requirement);
}
