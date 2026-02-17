package com.colvir.ms.sys.rms.generated.domain;

import com.colvir.ms.common.generator.id.runtime.service.IdGeneratorService;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Требование
 */
@Entity
@Table(name = "requirement")
@RegisterForReflection
public class Requirement extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    /**
     * фактическая дата оплаты
     */
    @Column(name = "actual_payment_date")
    public LocalDate actualPaymentDate;

    /**
     * сумма
     */
    @Column(name = "amount")
    public BigDecimal amount;

    /**
     * документ-основание
     */
    @Size(max = 511)
    @Column(name = "base_document")
    public String baseDocument;

    /**
     * идентификатор записи в журнале
     */
    @Size(max = 511)
    @Column(name = "bbp_state_000_journal_id")
    public String bbpState000JournalId;

    /**
     * идентификатор процесса
     */
    @Size(max = 511)
    @Column(name = "bbp_state_000_process_id")
    public String bbpState000ProcessId;

    /**
     * код состояния
     */
    @Size(max = 511)
    @Column(name = "bbp_state_000_state_code")
    public String bbpState000StateCode;

    /**
     * плательщик
     */
    @Column(name = "client_id")
    public Long clientId;

    /**
     * валюта
     */
    @Column(name = "currency_id")
    public Long currencyId;

    /**
     * дата
     */
    @Column(name = "date")
    public LocalDate date;

    /**
     * дополнительная информация
     */
    @Size(max = 511)
    @Column(name = "descr")
    public String descr;

    /**
     * дата редактирования
     */
    @Column(name = "edit_date")
    public Instant editDate;

    /**
     * редактор
     */
    @Column(name = "editor_id")
    public Long editorId;

    /**
     * исполнитель
     */
    @Column(name = "executor_id")
    public Long executorId;

    /**
     * референс во внешней системе
     */
    @Size(max = 511)
    @Column(name = "external_reference")
    public String externalReference;

    /**
     * фиксированный курс
     */
    @Column(name = "fix_rate")
    public BigDecimal fixRate;

    /**
     * расчетная категория
     */
    @Column(name = "indicator_id")
    public Long indicatorId;

    /**
     * связано с договором
     */
    @NotNull
    @Column(name = "is_contract_bound", nullable = false)
    public Boolean isContractBound;

    /**
     * является удалённым объектом
     */
    @Column(name = "is_deleted")
    public Boolean isDeleted;

    /**
     * холдировать средства
     */
    @Column(name = "is_holding")
    public Boolean isHolding;

    /**
     * запись об операции
     */
    @Column(name = "operation_record_id")
    public Long operationRecordId;

    /**
     * оплаченная сумма
     */
    @Column(name = "paid_amount")
    public BigDecimal paidAmount;

    /**
     * текст назначения платежа
     */
    @Size(max = 511)
    @Column(name = "payment_details_000_descr")
    public String paymentDetails000Descr;

    /**
     * код назначения платежа
     */
    @Size(max = 511)
    @Column(name = "payment_details_000_knp")
    public String paymentDetails000Knp;

    /**
     * номер платежного документа
     */
    @Size(max = 511)
    @Column(name = "payment_details_000_payment_num")
    public String paymentDetails000PaymentNum;

    /**
     * дата окончания оплаты
     */
    @Column(name = "payment_end_date")
    public LocalDate paymentEndDate;

    /**
     * разрешена автоматическая оплата
     */
    @Column(name = "payment_strategy_000_allow_auto_pay")
    public Boolean paymentStrategy000AllowAutoPay;

    /**
     * разрешена оплата группой
     */
    @Column(name = "payment_strategy_000_allow_group_pay")
    public Boolean paymentStrategy000AllowGroupPay;

    /**
     * использовать счет только при наличии баланса на полную оплату
     */
    @Column(name = "payment_strategy_000_use_acc_only_full_pay")
    public Boolean paymentStrategy000UseAccOnlyFullPay;

    /**
     * получатель
     */
    @Column(name = "person_id")
    public Long personId;

    /**
     * приоритет
     */
    @Column(name = "priority")
    public BigDecimal priority;

    /**
     * тип курса
     */
    @Column(name = "rate_type_id")
    public Long rateTypeId;

    /**
     * реквизиты получателя
     */
    @Size(max = 511)
    @Column(name = "recipient_details")
    public String recipientDetails;

    /**
     * референс
     */
    @Size(max = 511)
    @Column(name = "reference")
    public String reference;

    /**
     * порядковый номер
     */
    @Column(name = "serial_number")
    public Long serialNumber;

    /**
     * дата начала оплаты
     */
    @Column(name = "start_payment_date")
    public LocalDate startPaymentDate;

    /**
     * состояние
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    public RequirementStatus state;

    /**
     * неоплаченная сумма
     */
    @Column(name = "unpaid_amount")
    public BigDecimal unpaidAmount;

    /**
     * номер версии
     */
    @Version
    @Column(name = "version")
    public Integer version = 0;

    /**
     * списанная сумма
     */
    @Column(name = "write_off_amount")
    public BigDecimal writeOffAmount;

    @OneToMany(mappedBy = "requirementOfAssignedHolds")
    public Set<RequirementHold> assignedHolds = new HashSet<>();

    @OneToMany(mappedBy = "requirementOfRefundingPayments")
    public Set<RequirementRefundingPayment> refundingPayments = new HashSet<>();

    @OneToMany(mappedBy = "requirementOfRelatedPayments")
    public Set<RelatedPayment> relatedPayments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_type_id")
    @JsonIgnore
    public RequirementType requirementType;

    @OneToMany(mappedBy = "requirementOfWithdrawalRules")
    public Set<SysAcc000WithdrawalRule> withdrawalRules = new HashSet<>();

    @OneToMany(mappedBy = "requirementOfPayerDetails")
    public Set<Requirement000PayerDetails> payerDetails = new HashSet<>();

    @OneToMany(mappedBy = "requirementOfRelatedTaxes")
    public Set<Requirement000RelatedTaxes> relatedTaxes = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Requirement)) {
            return false;
        }
        return id != null && id.equals(((Requirement) o).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return "Requirement(" +
            "id='" + id + "', " +
            "actualPaymentDate='" + actualPaymentDate + "', " +
            "amount='" + amount + "', " +
            "baseDocument='" + baseDocument + "', " +
            "bbpState000JournalId='" + bbpState000JournalId + "', " +
            "bbpState000ProcessId='" + bbpState000ProcessId + "', " +
            "bbpState000StateCode='" + bbpState000StateCode + "', " +
            "clientId='" + clientId + "', " +
            "currencyId='" + currencyId + "', " +
            "date='" + date + "', " +
            "descr='" + descr + "', " +
            "editDate='" + editDate + "', " +
            "editorId='" + editorId + "', " +
            "executorId='" + executorId + "', " +
            "externalReference='" + externalReference + "', " +
            "fixRate='" + fixRate + "', " +
            "indicatorId='" + indicatorId + "', " +
            "isContractBound='" + isContractBound + "', " +
            "isDeleted='" + isDeleted + "', " +
            "isHolding='" + isHolding + "', " +
            "operationRecordId='" + operationRecordId + "', " +
            "paidAmount='" + paidAmount + "', " +
            "paymentDetails000Descr='" + paymentDetails000Descr + "', " +
            "paymentDetails000Knp='" + paymentDetails000Knp + "', " +
            "paymentDetails000PaymentNum='" + paymentDetails000PaymentNum + "', " +
            "paymentEndDate='" + paymentEndDate + "', " +
            "paymentStrategy000AllowAutoPay='" + paymentStrategy000AllowAutoPay + "', " +
            "paymentStrategy000AllowGroupPay='" + paymentStrategy000AllowGroupPay + "', " +
            "paymentStrategy000UseAccOnlyFullPay='" + paymentStrategy000UseAccOnlyFullPay + "', " +
            "personId='" + personId + "', " +
            "priority='" + priority + "', " +
            "rateTypeId='" + rateTypeId + "', " +
            "recipientDetails='" + recipientDetails + "', " +
            "reference='" + reference + "', " +
            "serialNumber='" + serialNumber + "', " +
            "startPaymentDate='" + startPaymentDate + "', " +
            "state='" + state + "', " +
            "unpaidAmount='" + unpaidAmount + "', " +
            "version='" + version + "', " +
            "writeOffAmount='" + writeOffAmount + "'" +
            ")";
    }

    private static IdGeneratorService idGeneratorService__ = null;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            if (idGeneratorService__ == null) {
                idGeneratorService__ = CDI.current().select(IdGeneratorService.class).get();
            }
            this.id = idGeneratorService__.next();
        }
        isContractBound = isContractBound == null ? false : isContractBound;
        isDeleted = isDeleted == null ? false : isDeleted;
        isHolding = isHolding == null ? false : isHolding;
        paymentStrategy000AllowAutoPay = paymentStrategy000AllowAutoPay == null ? false : paymentStrategy000AllowAutoPay;
        paymentStrategy000AllowGroupPay = paymentStrategy000AllowGroupPay == null ? false : paymentStrategy000AllowGroupPay;
        paymentStrategy000UseAccOnlyFullPay = paymentStrategy000UseAccOnlyFullPay == null ? false : paymentStrategy000UseAccOnlyFullPay;
        state = state == null ? RequirementStatus.WAIT : state;
    }

    /**
     * Updates the current state of the entity in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the current entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @return the updated Requirement entity
     * @throws IllegalArgumentException if the given Requirement entity is null, or its id is null, or no existing entity with the same id is found
     */
    public Requirement update() {
        return update(this);
    }

    /**
     * Persists or updates the current state of the entity in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the current entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @return the persisted or updated Requirement entity
     * @throws IllegalArgumentException if the given Requirement entity is null
     */
    public Requirement persistOrUpdate() {
        return persistOrUpdate(this);
    }

    /**
     * Updates the current state of the given Requirement entity state in the database.
     * The entity must have a non-null id and there must be an existing entity in database with the same id.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirement the Requirement entity to be updated
     * @return the updated Requirement entity
     * @throws IllegalArgumentException if the given Requirement entity is null, or its id is null, or no existing entity with the same id is found
     */
    public static Requirement update(Requirement requirement) {
        if (requirement == null) {
            throw new IllegalArgumentException("requirement can't be null");
        }
        if (requirement.id == null) {
            throw new IllegalArgumentException("requirement id must be specified");
        }
        if (Requirement.<Requirement>findById(requirement.id) == null) {
            throw new IllegalArgumentException(String.format("Existing method with id='%s' not found", requirement.id));
        }
        final Requirement merged = Requirement.getEntityManager().merge(requirement);
        Requirement.getEntityManager().flush();
        return merged;
    }

    /**
     * Persists or updates the current state of the given Requirement entity state in the database.
     * If the entity has a null id or no existing entity with the same id is found, it persists the new entity.
     * Otherwise, it merges the given entity with the existing one and updates it.
     * <br><br>
     * Note: The method <b>does not modify the given entity</b>.
     * Instead, the method returns a new entity that reflects the update.
     *
     * @param requirement the Requirement entity to be persisted or updated
     * @return the persisted or updated Requirement entity
     * @throws IllegalArgumentException if the given Requirement entity is null
     */
    public static Requirement persistOrUpdate(Requirement requirement) {
        if (requirement == null) {
            throw new IllegalArgumentException("requirement can't be null");
        }
        Requirement entity = null;
        if (requirement.id != null) {
            entity = Requirement.<Requirement>findById(requirement.id);
        }
        if (requirement.id == null || entity == null) {
            persist(requirement);
            return requirement;
        } else {
            final Requirement merged = Requirement.getEntityManager().merge(requirement);
            Requirement.getEntityManager().flush();
            return merged;
        }
    }
}
