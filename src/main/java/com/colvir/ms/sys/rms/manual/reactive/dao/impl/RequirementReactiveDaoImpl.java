package com.colvir.ms.sys.rms.manual.reactive.dao.impl;

import com.colvir.ms.sys.rms.generated.domain.Requirement;
import com.colvir.ms.sys.rms.manual.dao.RequirementDao;
import com.colvir.ms.sys.rms.manual.reactive.dao.RequirementReactiveDao;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class RequirementReactiveDaoImpl implements RequirementReactiveDao {

    @Inject
    RequirementDao requirementDao;

    /**
     * Existing Panache/JDBC DAO is blocking, so run it on the worker pool to avoid blocking
     * the event-loop while the project is not migrated to Hibernate Reactive.
     */
    @Override
    public Uni<Long> countByIds(List<Long> ids) {
        return Uni.createFrom()
            .item(() -> QuarkusTransaction.joiningExisting()
                .call(() -> requirementDao.countByIds(ids)))
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    @Override
    public Uni<Void> bulkInsert(List<Requirement> requirements) {
        return Uni.createFrom()
            .item(() -> {
                QuarkusTransaction.joiningExisting()
                    .run(() -> requirementDao.bulkInsert(requirements));
                return true;
            })
            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
            .replaceWithVoid();
    }
}
