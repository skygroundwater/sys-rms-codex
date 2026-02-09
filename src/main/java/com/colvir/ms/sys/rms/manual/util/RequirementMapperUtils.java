package com.colvir.ms.sys.rms.manual.util;

import com.colvir.ms.sys.rms.dto.ReferenceDto;
import com.colvir.ms.sys.rms.dto.RequirementIndicatorDto;
import com.colvir.ms.sys.rms.dto.RequirementJournalDto;
import com.colvir.ms.sys.rms.dto.RequirementStateInfoDto;
import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementAction;
import com.colvir.ms.sys.rms.generated.domain.enumeration.RequirementStatus;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RequirementMapperUtils {

    public static RequirementJournalDto fillRequirementJournal(Requirement requirement) {
        RequirementJournalDto requirementJournal = new RequirementJournalDto();
        requirementJournal.id = requirement.id;
        requirementJournal.paidAmount = requirement.paidAmount;
        requirementJournal.unpaidAmount = requirement.unpaidAmount;
        requirementJournal.writeOffAmount = requirement.writeOffAmount;
        requirementJournal.amount = requirement.amount;
        requirementJournal.bbpStateCode = requirement.bbpState000StateCode;
        requirementJournal.bbpProcessId = requirement.bbpState000ProcessId;
        requirementJournal.bbpJournalId = requirement.bbpState000JournalId;
        requirementJournal.state = requirement.state;
        requirementJournal.priority = requirement.priority;
        requirementJournal.actualPaymentDate = requirement.actualPaymentDate;
        requirementJournal.paymentEndDate = requirement.paymentEndDate;
        requirementJournal.indicator = new RequirementIndicatorDto();
        requirementJournal.indicator.id = requirement.indicatorId;
        return requirementJournal;
    }

    public static RequirementStateInfoDto fillRequirementInfo(Requirement requirement, RequirementAction action, RequirementIndicatorDto requirementIndicatorDto) {
        RequirementStateInfoDto requirementInfo = new RequirementStateInfoDto();
        requirementInfo.action = action;
        requirementInfo.status = requirement.state;
        requirementInfo.requirementId = requirement.id;
        requirementInfo.amount = requirement.amount;
        requirementInfo.payedAmount = requirement.paidAmount;
        requirementInfo.priority = getPriorityDecimal(requirement.priority);
        requirementInfo.indicator = requirementIndicatorDto;
        return requirementInfo;
    }

    public static RequirementStateInfoDto fillRequirementInfo(Long requirementId, RequirementAction action, RequirementStatus status,
                                                              BigDecimal amount, BigDecimal paidAmount, BigDecimal priority, Long indicatorId) {
        RequirementStateInfoDto requirementInfo = new RequirementStateInfoDto();
        requirementInfo.action = action;
        requirementInfo.status = status;
        requirementInfo.requirementId = requirementId;
        RequirementIndicatorDto indicator = new RequirementIndicatorDto();
        if (indicatorId != null) {
            indicator.id = indicatorId;
            // TODO доделать заполнение indicatorDescr
            indicator.indicatorDescr = new ReferenceDto();
        }
        requirementInfo.indicator = indicator;
        requirementInfo.amount = amount;
        requirementInfo.payedAmount = paidAmount;
        // дробная часть приоритета из текущего требования
        requirementInfo.priority = getPriorityDecimal(priority);
        return requirementInfo;
    }

    public static RequirementStateInfoDto mapRequirementStateInfoDto(RequirementStateInfoDto paymentData, Long requirementId,
                                                               RequirementAction action, RequirementStatus status) {

        RequirementStateInfoDto requirementInfo = new RequirementStateInfoDto();
        requirementInfo.payedAmount = paymentData.payedAmount;
        requirementInfo.priority = paymentData.priority;
        RequirementIndicatorDto indicator = new RequirementIndicatorDto();
        if (paymentData.indicator != null) {
            indicator.id = paymentData.indicator.id;
            indicator.code = paymentData.indicator.code;
            indicator.__objectType = paymentData.indicator.__objectType;
            if (paymentData.indicator.indicatorDescr != null) {
                indicator.indicatorDescr = new ReferenceDto(paymentData.indicator.indicatorDescr.id, paymentData.indicator.indicatorDescr.__objectType);
            }
        }
        requirementInfo.paymentEndDate = paymentData.paymentEndDate;
        requirementInfo.indicator = indicator;
        requirementInfo.amount = paymentData.amount;
        requirementInfo.requirementId = requirementId;
        requirementInfo.action = action;
        requirementInfo.status = status;
        return requirementInfo;
    }

    public static Integer getPriorityDecimal(BigDecimal priority) {
        // дробная часть приоритета (устанавливается по усмотрению Банка)
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal newPriority = priority.multiply(hundred).remainder(hundred);
        return newPriority.intValue();
    }

    public static BigInteger getPriorityInteger(BigDecimal priority) {
        if (priority == null) {
            return BigInteger.valueOf(Integer.MAX_VALUE);
        }
        // целая часть приоритета (законодательно установленный приоритет)
        return priority.toBigInteger();
    }

}
