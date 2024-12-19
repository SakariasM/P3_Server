package com.p3.Server.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.client.TestRestTemplate;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private Environment env;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private String apiKey;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/user";

        // Get brian api key
        ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl + "/apiKey/brian", Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        apiKey = (String) response.getBody().get("api_key");
        assertThat(apiKey).isNotNull();
    }

    @Test
    void testGetUsers() {
        // test getting the list of all users
        HttpHeaders headers = new HttpHeaders();
        headers.set("API-Key", apiKey);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<User[]> response = restTemplate.exchange(
                baseUrl + "/info/users",
                HttpMethod.GET,
                requestEntity,
                User[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        User[] users = response.getBody();
        assertThat(users).isNotEmpty();
        // check for brian exists
        boolean containsBrian = false;
        for (User u : users) {
            if ("brian".equals(u.getUsername())) {
                containsBrian = true;
                break;
            }
        }
        assertThat(containsBrian).isTrue();
    }

    @Test
    void testGetUserInformationByUsername() {
        // test fetching user role by username
        HttpHeaders headers = new HttpHeaders();
        headers.set("API-Key", apiKey);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/brian/role",
                HttpMethod.GET,
                requestEntity,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("role");
        assertThat(response.getBody().get("role")).isEqualTo("manager");
    }

    @Test
    void testCreateNewUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("API-Key", apiKey);

        User newUser = new User();
        newUser.setUsername("testUser");
        newUser.setFullName("Test User Fullname");
        newUser.setClockedIn(false);
        newUser.setOnBreak(false);
        newUser.setLoggedIn(false);
        newUser.setPassword("pass123");
        newUser.setRole("employee");

        HttpEntity<User> requestEntity = new HttpEntity<>(newUser, headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/newUser",
                HttpMethod.POST,
                requestEntity,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // verify user created
        ResponseEntity<User[]> getUsersResponse = restTemplate.exchange(
                baseUrl + "/info/users",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                User[].class
        );

        User[] users = getUsersResponse.getBody();
        assertThat(users).extracting(User::getUsername).contains("testUser");
    }

    @Test
    void testUpdateUserInfo() {
        // create new user
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("API-Key", apiKey);

        User userToUpdate = new User();
        userToUpdate.setUsername("updateUser");
        userToUpdate.setFullName("Update User Name");
        userToUpdate.setClockedIn(false);
        userToUpdate.setOnBreak(false);
        userToUpdate.setLoggedIn(false);
        userToUpdate.setPassword("initPass");
        userToUpdate.setRole("employee");

        restTemplate.exchange(
                baseUrl + "/newUser",
                HttpMethod.POST,
                new HttpEntity<>(userToUpdate, headers),
                Void.class
        );

        // fetch the user_id of new user
        ResponseEntity<User[]> getUsersResponse = restTemplate.exchange(
                baseUrl + "/info/users",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                User[].class
        );
        int userId = -1;
        for (User u : getUsersResponse.getBody()) {
            if ("updateUser".equals(u.getUsername())) {
                userId = u.getUserId();
                break;
            }
        }
        assertThat(userId).isNotEqualTo(-1);

        // update user password and role
        User updatedInfo = new User();
        updatedInfo.setUserId(userId);
        updatedInfo.setPassword("newPass");
        updatedInfo.setRole("manager");

        restTemplate.exchange(
                baseUrl + "/update",
                HttpMethod.PUT,
                new HttpEntity<>(updatedInfo, headers),
                Void.class
        );

        // verify changes
        getUsersResponse = restTemplate.exchange(
                baseUrl + "/info/users",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                User[].class
        );

        User updatedUser = null;
        for (User u : getUsersResponse.getBody()) {
            if (u.getUserId() == userId) {
                updatedUser = u;
                break;
            }
        }

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getPassword()).isEqualTo("newPass");
        assertThat(updatedUser.getRole()).isEqualTo("manager");
    }

    @Test
    void testDeleteUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("API-Key", apiKey);

        // create a user to delete
        User userToDelete = new User();
        userToDelete.setUsername("deleteMe");
        userToDelete.setFullName("Delete Me");
        userToDelete.setClockedIn(false);
        userToDelete.setOnBreak(false);
        userToDelete.setLoggedIn(false);
        userToDelete.setPassword("tempPass");
        userToDelete.setRole("employee");

        restTemplate.exchange(
                baseUrl + "/newUser",
                HttpMethod.POST,
                new HttpEntity<>(userToDelete, headers),
                Void.class
        );

        // find the user_id
        ResponseEntity<User[]> getUsersResponse = restTemplate.exchange(
                baseUrl + "/info/users",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                User[].class
        );
        int userIdToDelete = -1;
        for (User u : getUsersResponse.getBody()) {
            if ("deleteMe".equals(u.getUsername())) {
                userIdToDelete = u.getUserId();
                break;
            }
        }
        assertThat(userIdToDelete).isNotEqualTo(-1);

        // delete the user
        restTemplate.exchange(
                baseUrl + "/" + userIdToDelete,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );

        // verify deletion
        getUsersResponse = restTemplate.exchange(
                baseUrl + "/info/users",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                User[].class
        );
        assertThat(getUsersResponse.getBody()).extracting(User::getUsername).doesNotContain("deleteMe");
    }
}