package com.colvir.ms.sys.rms.manual.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.SysAcc000WithdrawalRule;
import com.colvir.ms.sys.rms.manual.dao.SysAccWithdrawalRuleDao;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.BooleanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.colvir.ms.sys.acc.generated.domain.enumeration.ClientAccountSelectionType.SPECIFIED;

@ApplicationScoped
public class SysAccWithdrawalRuleDaoImpl implements SysAccWithdrawalRuleDao {

    @Override
    public List<Long> findActiveRequirementIdsByAccountNumbersAndWithdrawalType(List<String> accountNumbers,
                                                                                 Long withdrawalTypeId,
                                                                                 Long excludedRequirementId) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountNumbers", accountNumbers);
        params.put("withdrawalTypeId", withdrawalTypeId);
        params.put("accountSelectionType", SPECIFIED);
        String query = """
            select distinct wr from SysAcc000WithdrawalRule wr
            join fetch wr.accountNumbers an where
            an.value in (:accountNumbers)
            and wr.withdrawalTypeId = :withdrawalTypeId
            and wr.accountSelectionType = :accountSelectionType
            and (wr.isDeleted is null or wr.isDeleted = false)""";
        return SysAcc000WithdrawalRule.list(query, params)
            .stream()
            .map(wr -> ((SysAcc000WithdrawalRule) wr))
            .filter(wr -> BooleanUtils.isNotTrue(wr.requirementOfWithdrawalRules.isDeleted))
            .map(wr -> wr.requirementOfWithdrawalRules.id)
            .filter(id -> !Objects.equals(id, excludedRequirementId))
            .toList();
    }
}
