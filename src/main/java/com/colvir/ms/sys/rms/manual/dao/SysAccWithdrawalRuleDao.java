package com.colvir.ms.sys.rms.manual.dao;

import java.util.List;

public interface SysAccWithdrawalRuleDao {

    List<Long> findActiveRequirementIdsByAccountNumbersAndWithdrawalType(List<String> accountNumbers,
                                                                          Long withdrawalTypeId,
                                                                          Long excludedRequirementId);
}
