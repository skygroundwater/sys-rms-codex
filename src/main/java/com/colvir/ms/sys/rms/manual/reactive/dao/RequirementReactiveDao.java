package com.colvir.ms.sys.rms.manual.reactive.dao;

import com.colvir.ms.sys.rms.generated.domain.Requirement;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface RequirementReactiveDao {

    Uni<Long> countByIds(List<Long> ids);

    Uni<Void> bulkInsert(List<Requirement> requirements);
}
