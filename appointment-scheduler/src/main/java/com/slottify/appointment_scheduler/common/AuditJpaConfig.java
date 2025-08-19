package com.slottify.appointment_scheduler.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
@RequiredArgsConstructor
public class AuditJpaConfig {

    private final SessionUtils sessionUtils;

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(sessionUtils.getUsernameInSession());
    }

    @Bean
    public org.springframework.data.auditing.DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now());
    }
}
