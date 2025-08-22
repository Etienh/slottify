package com.slottify.appointment_scheduler.service;

import com.slottify.appointment_scheduler.common.SessionUtils;
import com.slottify.appointment_scheduler.dto.RegisterUserRequest;
import com.slottify.appointment_scheduler.entity.User;
import com.slottify.appointment_scheduler.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Keycloak keycloak;
    private final String keycloakRealm;
    private final UserRepository userRepository;
    private final SessionUtils sessionUtils;

    public void registerUser(RegisterUserRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);

        Response response = keycloak.realm(keycloakRealm).users().create(user);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user: " + response.getStatus());
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(request.getPassword());

        keycloak.realm(keycloakRealm).users().get(userId).resetPassword(credentials);

        User dbUser = User.builder()
                .id(UUID.fromString(userId))
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .status(true)
                .build();

        userRepository.save(dbUser);

    }

    public void updateTokenProjectAttribute(UUID projectId) {
        UUID userId = sessionUtils.getUserInSession().getId();
        UserResource userRes = keycloak.realm(keycloakRealm).users().get(userId.toString());
        UserRepresentation rep = userRes.toRepresentation();

        Map<String, List<String>> attrs = rep.getAttributes();
        if (attrs == null) attrs = new HashMap<>();

        if (projectId == null) {
            attrs.remove("projectId");
        } else {
            attrs.put("projectId", List.of(projectId.toString()));
        }

        rep.setAttributes(attrs);
        userRes.update(rep);
    }


    public User getById(UUID userId){
        return userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
    }


}
