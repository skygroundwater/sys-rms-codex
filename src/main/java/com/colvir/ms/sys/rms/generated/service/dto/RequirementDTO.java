package com.colvir.ms.sys.rms.generated.service.dto;

import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.colvir.ms.sys.rms.generated.domain.Requirement} entity.
 */
@Schema(name = "RequirementDTO", description = "Требование")
@RegisterForReflection
public class RequirementDTO implements Serializable {

    public Long id;

    /**
     * фактическая дата оплаты
     */
    @SchemaProperty(name = "actualPaymentDate", description = "фактическая дата оплаты", nullable = true)
    public LocalDate actualPaymentDate;

    /**
     * сумма
     */
    @SchemaProperty(name = "amount", description = "сумма", nullable = true)
    public BigDecimal amount;

    /**
     * документ-основание
     */
    @SchemaProperty(name = "baseDocument", description = "документ-основание", nullable = true)
    public String baseDocument;

    /**
     * идентификатор записи в журнале
     */
    @SchemaProperty(name = "bbpState000JournalId", description = "идентификатор записи в журнале", nullable = true)
    public String bbpState000JournalId;

    /**
     * идентификатор процесса
     */
    @SchemaProperty(name = "bbpState000ProcessId", description = "идентификатор процесса", nullable = true)
    public String bbpState000ProcessId;

    /**
     * код состояния
     */
    @SchemaProperty(name = "bbpState000StateCode", description = "код состояния", nullable = true)
    public String bbpState000StateCode;

    /**
     * плательщик
     */
    @SchemaProperty(name = "clientId", description = "плательщик", nullable = true)
    public Long clientId;

    /**
     * валюта
     */
    @SchemaProperty(name = "currencyId", description = "валюта", nullable = true)
    public Long currencyId;

    /**
     * дата
     */
    @SchemaProperty(name = "date", description = "дата", nullable = true)
    public LocalDate date;

    /**
     * дополнительная информация
     */
    @SchemaProperty(name = "descr", description = "дополнительная информация", nullable = true)
    public String descr;

    /**
     * дата редактирования
     */
    @SchemaProperty(name = "editDate", description = "дата редактирования", nullable = true)
    public Instant editDate;

    /**
     * редактор
     */
    @SchemaProperty(name = "editorId", description = "редактор", nullable = true)
    public Long editorId;

    /**
     * исполнитель
     */
    @SchemaProperty(name = "executorId", description = "исполнитель", nullable = true)
    public Long executorId;

    /**
     * референс во внешней системе
     */
    @SchemaProperty(name = "externalReference", description = "референс во внешней системе", nullable = true)
    public String externalReference;

    /**
     * фиксированный курс
     */
    @SchemaProperty(name = "fixRate", description = "фиксированный курс", nullable = true)
    public BigDecimal fixRate;

    /**
     * расчетная категория
     */
    @SchemaProperty(name = "indicatorId", description = "расчетная категория", nullable = true)
    public Long indicatorId;

    /**
     * связано с договором
     */
    @NotNull
    @SchemaProperty(name = "isContractBound", description = "связано с договором")
    public Boolean isContractBound;

    /**
     * является удалённым объектом
     */
    @SchemaProperty(name = "isDeleted", description = "является удалённым объектом", nullable = true)
    public Boolean isDeleted;

    /**
     * холдировать средства
     */
    @SchemaProperty(name = "isHolding", description = "холдировать средства", nullable = true)
    public Boolean isHolding;

    /**
     * запись об операции
     */
    @SchemaProperty(name = "operationRecordId", description = "запись об операции", nullable = true)
    public Long operationRecordId;

    /**
     * оплаченная сумма
     */
    @SchemaProperty(name = "paidAmount", description = "оплаченная сумма", nullable = true)
    public BigDecimal paidAmount;

    /**
     * текст назначения платежа
     */
    @SchemaProperty(name = "paymentDetails000Descr", description = "текст назначения платежа", nullable = true)
    public String paymentDetails000Descr;

    /**
     * код назначения платежа
     */
    @SchemaProperty(name = "paymentDetails000Knp", description = "код назначения платежа", nullable = true)
    public String paymentDetails000Knp;

    /**
     * номер платежного документа
     */
    @SchemaProperty(name = "paymentDetails000PaymentNum", description = "номер платежного документа", nullable = true)
    public String paymentDetails000PaymentNum;

    /**
     * дата окончания оплаты
     */
    @SchemaProperty(name = "paymentEndDate", description = "дата окончания оплаты", nullable = true)
    public LocalDate paymentEndDate;

    /**
     * разрешена автоматическая оплата
     */
    @SchemaProperty(name = "paymentStrategy000AllowAutoPay", description = "разрешена автоматическая оплата", nullable = true)
    public Boolean paymentStrategy000AllowAutoPay;

    /**
     * разрешена оплата группой
     */
    @SchemaProperty(name = "paymentStrategy000AllowGroupPay", description = "разрешена оплата группой", nullable = true)
    public Boolean paymentStrategy000AllowGroupPay;

    /**
     * использовать счет только при наличии баланса на полную оплату
     */
    @SchemaProperty(name = "paymentStrategy000UseAccOnlyFullPay", description = "использовать счет только при наличии баланса на полную оплату", nullable = true)
    public Boolean paymentStrategy000UseAccOnlyFullPay;

    /**
     * получатель
     */
    @SchemaProperty(name = "personId", description = "получатель", nullable = true)
    public Long personId;

    /**
     * приоритет
     */
    @SchemaProperty(name = "priority", description = "приоритет", nullable = true)
    public BigDecimal priority;

    /**
     * тип курса
     */
    @SchemaProperty(name = "rateTypeId", description = "тип курса", nullable = true)
    public Long rateTypeId;

    /**
     * реквизиты получателя
     */
    @SchemaProperty(name = "recipientDetails", description = "реквизиты получателя", nullable = true)
    public String recipientDetails;

    /**
     * референс
     */
    @SchemaProperty(name = "reference", description = "референс", nullable = true)
    public String reference;

    /**
     * порядковый номер
     */
    @SchemaProperty(name = "serialNumber", description = "порядковый номер", nullable = true)
    public Long serialNumber;

    /**
     * дата начала оплаты
     */
    @SchemaProperty(name = "startPaymentDate", description = "дата начала оплаты", nullable = true)
    public LocalDate startPaymentDate;

    /**
     * состояние
     */
    @NotNull
    @SchemaProperty(name = "state", description = "состояние")
    public RequirementStatus state;

    /**
     * неоплаченная сумма
     */
    @SchemaProperty(name = "unpaidAmount", description = "неоплаченная сумма", nullable = true)
    public BigDecimal unpaidAmount;

    /**
     * номер версии
     */
    @SchemaProperty(name = "version", description = "номер версии", nullable = true)
    public Integer version;

    /**
     * списанная сумма
     */
    @SchemaProperty(name = "writeOffAmount", description = "списанная сумма", nullable = true)
    public BigDecimal writeOffAmount;

    public Long requirementTypeId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequirementDTO)) {
            return false;
        }

        return id != null && id.equals(((RequirementDTO) o).id);
    }

    public Long getId() {
        return id;
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "RequirementDTO(" +
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

    public RequirementDTO() {
    }

    public RequirementDTO(Long id) {
        this.id = id;
    }
}
