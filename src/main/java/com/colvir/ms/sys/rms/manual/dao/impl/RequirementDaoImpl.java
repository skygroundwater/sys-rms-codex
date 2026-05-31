package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.colvir.ms.sys.rms.manual.dao.RequirementDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class RequirementDaoImpl implements RequirementDao {

    private static final String REQUIREMENT_NOT_FOUND_OR_DELETED = "Requirement with id=%s is not found or marked as deleted";

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
    public Requirement findByIdOrThrow(Long id) {
        Requirement requirement = findById(id);
        if (requirement == null || Boolean.TRUE.equals(requirement.isDeleted)) {
            throw new RuntimeException(String.format(REQUIREMENT_NOT_FOUND_OR_DELETED, id));
        }
        return requirement;
    }

    @Override
    public Requirement getReference(Long id) {
        return entityManager.getReference(Requirement.class, id);
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
    public List<Requirement> findWaitByBaseDocumentAndBusinessDate(String baseDocument, LocalDate businessDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("baseDocument", baseDocument);
        params.put("state", RequirementStatus.WAIT);
        params.put("businessDate", businessDate);
        String query = "select r from Requirement r where r.baseDocument = :baseDocument " +
            " and r.state = :state " +
            " and ((r.startPaymentDate is null and r.date <= :businessDate) " +
            " or (r.startPaymentDate is not null and r.startPaymentDate <= :businessDate)) " +
            " and (r.isDeleted is null or r.isDeleted = false) " +
            " order by r.priority, r.serialNumber ";
        return Requirement.list(query, params);
    }

    @Override
    public List<Requirement> findByIdsAndStateWithPriorityLessThan(List<Long> requirementIdList, RequirementStatus state, BigDecimal priority) {
        Map<String, Object> params = new HashMap<>();
        params.put("requirementIdList", requirementIdList);
        params.put("state", state);
        params.put("priority", priority);
        String query = "select r from Requirement r where " +
            " r.id in (:requirementIdList) " +
            " and r.state = :state " +
            " and (r.isDeleted is null or r.isDeleted = false) " +
            " and r.priority < :priority";
        return Requirement.list(query, params);
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

    @Override
    public void bulkInsert(List<Requirement> requirements) {
        if (requirements == null || requirements.isEmpty()) {
            return;
        }

        Session session = entityManager.unwrap(Session.class);

        session.doWork(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("""
            insert into requirement (
                id,
                state,
                amount,
                unpaid_amount,
                paid_amount,
                write_off_amount,
                currency_id,
                client_id,
                indicator_id,
                date,
                start_payment_date,
                payment_end_date,
                is_contract_bound,
                base_document,
                bbp_state_000_state_code,
                bbp_state_000_process_id,
                bbp_state_000_journal_id,
                priority,
                requirement_type_id
            ) values (
                ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
            )
            """)) {

                for (Requirement r : requirements) {
                    int i = 1;

                    ps.setLong(i++, r.id);

                    if (r.state != null) {
                        ps.setString(i++, r.state.name());
                    } else {
                        ps.setNull(i++, Types.VARCHAR);
                    }

                    ps.setBigDecimal(i++, r.amount);
                    ps.setBigDecimal(i++, r.unpaidAmount);
                    ps.setBigDecimal(i++, r.paidAmount);
                    ps.setBigDecimal(i++, r.writeOffAmount);

                    setLongOrNull(ps, i++, r.currencyId);
                    setLongOrNull(ps, i++, r.clientId);
                    setLongOrNull(ps, i++, r.indicatorId);

                    setLocalDateOrNull(ps, i++, r.date);
                    setLocalDateOrNull(ps, i++, r.startPaymentDate);
                    setLocalDateOrNull(ps, i++, r.paymentEndDate);

                    if (r.isContractBound != null) {
                        ps.setBoolean(i++, r.isContractBound);
                    } else {
                        ps.setNull(i++, Types.BOOLEAN);
                    }

                    ps.setString(i++, r.baseDocument);
                    ps.setString(i++, r.bbpState000StateCode);
                    ps.setString(i++, r.bbpState000ProcessId);
                    ps.setString(i++, r.bbpState000JournalId);

                    ps.setBigDecimal(i++, r.priority);

                    if (r.requirementType != null && r.requirementType.id != null) {
                        ps.setLong(i++, r.requirementType.id);
                    } else {
                        ps.setNull(i++, Types.BIGINT);
                    }

                    ps.addBatch();
                }

                ps.executeBatch();
            }
        });
    }

    private static void setLongOrNull(PreparedStatement ps, int index, Long value) throws java.sql.SQLException {
        if (value != null) {
            ps.setLong(index, value);
        } else {
            ps.setNull(index, Types.BIGINT);
        }
    }

    private static void setLocalDateOrNull(PreparedStatement ps, int index, LocalDate value) throws java.sql.SQLException {
        if (value != null) {
            ps.setDate(index, Date.valueOf(value));
        } else {
            ps.setNull(index, Types.DATE);
        }
    }
}
