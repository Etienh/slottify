package com.slottify.appointment_scheduler.service;

import com.slottify.appointment_scheduler.common.SessionUtils;
import com.slottify.appointment_scheduler.dto.RegisterUserRequest;
import com.slottify.appointment_scheduler.entity.User;
import com.slottify.appointment_scheduler.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String KEYCLOAK_REALM = "test-realm";

    @Mock
    private Keycloak keycloak;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SessionUtils sessionUtils;
    @Mock
    private RealmResource realmResource;
    @Mock
    private UsersResource usersResource;
    @Mock
    private UserResource userResource;
    @Mock
    private Response response;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(keycloak, KEYCLOAK_REALM, userRepository, sessionUtils);
    }

    @Test
    void registerUser() {
        // given
        RegisterUserRequest request = new RegisterUserRequest("jdoe", "jdoe@test.com", "John", "Doe", "secret");
        UUID keycloakUserId = UUID.randomUUID();

        when(keycloak.realm(KEYCLOAK_REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(201);
        when(response.getLocation()).thenReturn(URI.create("http://keycloak/admin/realms/" + KEYCLOAK_REALM + "/users/" + keycloakUserId));
        when(usersResource.get(keycloakUserId.toString())).thenReturn(userResource);

        // when
        userService.registerUser(request);

        // then
        ArgumentCaptor<UserRepresentation> userRepresentationCaptor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(usersResource).create(userRepresentationCaptor.capture());
        UserRepresentation createdUser = userRepresentationCaptor.getValue();
        assertEquals("jdoe", createdUser.getUsername());
        assertEquals("jdoe@test.com", createdUser.getEmail());
        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());
        assertTrue(createdUser.isEnabled());

        ArgumentCaptor<CredentialRepresentation> credentialCaptor = ArgumentCaptor.forClass(CredentialRepresentation.class);
        verify(userResource).resetPassword(credentialCaptor.capture());
        CredentialRepresentation credential = credentialCaptor.getValue();
        assertEquals("secret", credential.getValue());
        assertFalse(credential.isTemporary());
        assertEquals(CredentialRepresentation.PASSWORD, credential.getType());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(keycloakUserId, savedUser.getId());
        assertEquals("jdoe", savedUser.getUsername());
        assertEquals("jdoe@test.com", savedUser.getEmail());
        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        assertTrue(savedUser.isStatus());
    }

    @Test
    void registerUser_ShouldThrowRuntimeException() {
        // given
        RegisterUserRequest request = new RegisterUserRequest("jdoe", "jdoe@test.com", "John", "Doe", "secret");

        when(keycloak.realm(KEYCLOAK_REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(409);

        // when / then
        assertThrows(RuntimeException.class, () -> userService.registerUser(request));
        verify(usersResource, never()).get(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateTokenProjectAttribute() {
        // given
        UUID userId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        User sessionUser = User.builder().id(userId).build();
        UserRepresentation representation = new UserRepresentation();

        when(sessionUtils.getUserInSession()).thenReturn(sessionUser);
        when(keycloak.realm(KEYCLOAK_REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(userId.toString())).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(representation);

        // when
        userService.updateTokenProjectAttribute(projectId);

        // then
        ArgumentCaptor<UserRepresentation> captor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(userResource).update(captor.capture());
        assertEquals(List.of(projectId.toString()), captor.getValue().getAttributes().get("projectId"));
    }

    @Test
    void updateTokenProjectAttribute_ShouldRemoveProjectIdAttribute() {
        // given
        UUID userId = UUID.randomUUID();
        User sessionUser = User.builder().id(userId).build();
        UserRepresentation representation = new UserRepresentation();
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("projectId", List.of(UUID.randomUUID().toString()));
        representation.setAttributes(attributes);

        when(sessionUtils.getUserInSession()).thenReturn(sessionUser);
        when(keycloak.realm(KEYCLOAK_REALM)).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.get(userId.toString())).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(representation);

        // when
        userService.updateTokenProjectAttribute(null);

        // then
        ArgumentCaptor<UserRepresentation> captor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(userResource).update(captor.capture());
        assertFalse(captor.getValue().getAttributes().containsKey("projectId"));
    }

    @Test
    void getById() {
        // given
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = userService.getById(userId);

        // then
        assertEquals(user, result);
    }

    @Test
    void getById_ShouldThrowEntityNotFoundException() {
        // given
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class, () -> userService.getById(userId));
    }
}
