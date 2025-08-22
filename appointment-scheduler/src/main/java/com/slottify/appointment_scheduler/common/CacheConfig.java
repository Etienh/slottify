package com.slottify.appointment_scheduler.common;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CaffeineCacheManager cacheManager() {
        var mgr = new CaffeineCacheManager("projectInSessionCache");
        mgr.setCaffeine(com.github.benmanes.caffeine.cache.Caffeine.newBuilder()
                .maximumSize(50_000)
                .expireAfterWrite(java.time.Duration.ofDays(3L)));
        return mgr;
    }
}