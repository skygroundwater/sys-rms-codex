package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.manual.dao.RequirementDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class RequirementDaoImpl implements RequirementDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public long countByIds(List<Long> ids) {
        return Requirement.count("id in (?1)", ids);
    }

    @Override
    public Requirement findById(Long id) {
        return Requirement.findById(id);
    }


    @Override
    public Requirement findByIdOrThrow(Long id, String messageTemplate) {
        Requirement requirement = findById(id);
        if (requirement == null || Boolean.TRUE.equals(requirement.isDeleted)) {
            throw new RuntimeException(String.format(messageTemplate, id));
        }
        return requirement;
    }

    @Override
    public List<Requirement> findActiveByIds(Set<Long> ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        return Requirement.list(" id in (:ids) and (isDeleted is null or isDeleted = false) ", params);
    }

    @Override
    public List<Requirement> findActiveByContractOrClient(String contractRef, Long clientId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder("select r from Requirement r where (r.isDeleted is null or r.isDeleted = false) ");
        if (contractRef != null) {
            params.put("baseDocument", contractRef);
            query.append(" and r.baseDocument = :baseDocument ");
        } else if (clientId != null) {
            params.put("clientId", clientId);
            query.append(" and r.clientId = :clientId ");
        }
        query.append(" order by r.priority, r.serialNumber ");
        return Requirement.list(query.toString(), params);
    }

    @Override
    public Requirement findOverdueByRequirementIdAndBusinessDate(Long requirementId, LocalDate businessDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("businessDate", businessDate);
        params.put("id", requirementId);
        String query = "select r from Requirement r where r.id = :id" +
            " and (" +
            "  (r.paymentEndDate is null and r.date < :businessDate)" +
            "  or" +
            "  (r.paymentEndDate is not null and r.paymentEndDate < :businessDate)" +
            " ) " +
            " and (" +
            "  (r.unpaidAmount > 0)" +
            "  or" +
            "  (r.actualPaymentDate is not null and r.actualPaymentDate > :businessDate )" +
            " )" +
            " and (r.isDeleted is null or r.isDeleted = false)";
        return Requirement.find(query, params).firstResult();
    }

    @Override
    public void refresh(Requirement requirement) {
        entityManager.refresh(requirement);
    }
}
