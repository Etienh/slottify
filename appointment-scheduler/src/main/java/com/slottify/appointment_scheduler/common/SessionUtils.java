package com.slottify.appointment_scheduler.common;

import com.slottify.appointment_scheduler.entity.Project;
import com.slottify.appointment_scheduler.entity.User;
import com.slottify.appointment_scheduler.repository.UserRepository;
import com.slottify.appointment_scheduler.service.CacheService;
import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.NotAcceptableException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionUtils {

    private final UserRepository userRepository;
    private final CacheService cacheService;

    public String getUsernameInSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getClaimAsString("preferred_username");
        }
        throw new NotAcceptableException("User not found in session");
    }


    public UUID getProjectIdInSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            String projectId = jwtAuth.getToken().getClaimAsString("projectId");
            if (projectId != null) {
                return UUID.fromString(projectId);
            }
        }
        throw new NotAcceptableException("Project not found in session");
    }

    public User getUserInSession() {
        return userRepository.findByUsername(getUsernameInSession()).orElseThrow(EntityExistsException::new);
    }

    public Project getProjectInSession() throws Exception{
        return cacheService.getUserProjectInSessionByUsername(getUsernameInSession(), getProjectIdInSession());
    }
}
