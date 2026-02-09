package com.colvir.ms.sys.rms.manual.util;

import com.colvir.ms.common.cache.CacheInvalidateService;
import io.quarkus.arc.Unremovable;
import io.quarkus.cache.CacheInvalidateAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;


@ApplicationScoped
@Unremovable
public class CacheInvalidateServiceImpl implements CacheInvalidateService {
    public static final String REQUIREMENT_INDICATOR_CACHE = "requirementIndicatorCache";
    private final Logger log;

    @Inject
    public CacheInvalidateServiceImpl(Logger log) {
        this.log = log;
    }

    @Override
    public void cacheInvalidateAll() {
        invalidateAllCaches();
    }

    @CacheInvalidateAll(cacheName = REQUIREMENT_INDICATOR_CACHE)
    public void invalidateAppCache() {
        log.infof("Cache invalidation triggered for: %s", REQUIREMENT_INDICATOR_CACHE);
    }

    @CacheInvalidateAll(cacheName = REQUIREMENT_INDICATOR_CACHE)
    public void invalidateAllCaches() {
        log.infof("Cache invalidation triggered for: %s", REQUIREMENT_INDICATOR_CACHE);
    }
}
