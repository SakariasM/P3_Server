package com.p3.Server.notes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class NoteControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private String apiKey;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        // Get brian api key
        ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl + "/api/user/apiKey/brian", Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        apiKey = (String) response.getBody().get("api_key");
        assertThat(apiKey).isNotNull();
    }

    private HttpHeaders createHeadersWithApiKey() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("API-Key", apiKey);
        return headers;
    }

    @Test
    void testNoteExistsForDateAndUser() {
        // get brian note from 2024-11-01
        LocalDate noteDate = LocalDate.of(2024, 11, 1);
        int userId = 1;

        HttpHeaders headers = createHeadersWithApiKey();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/note/exists?noteDate=" + noteDate + "&userId=" + userId;
        ResponseEntity<Boolean> response = restTemplate.exchange(
                url, HttpMethod.GET, requestEntity, Boolean.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isTrue();
    }

    @Test
    void testNoteDoesNotExistForFakeUser() {
        // checks if there is notes for non existing users, should return false
        LocalDate noteDate = LocalDate.of(2024, 11, 1);
        int nonExistingUserId = 999;

        HttpHeaders headers = createHeadersWithApiKey();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/note/exists?noteDate=" + noteDate + "&userId=" + nonExistingUserId;
        ResponseEntity<Boolean> response = restTemplate.exchange(
                url, HttpMethod.GET, requestEntity, Boolean.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isFalse();
    }

    @Test
    void testGetDayNotes() {
        // find known note
        LocalDate date = LocalDate.of(2024, 12, 2);
        int userId = 3;

        HttpHeaders headers = createHeadersWithApiKey();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/note/day?date=" + date + "&userId=" + userId;
        ResponseEntity<List<Note>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Note>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Note> notes = response.getBody();
        assertThat(notes).isNotEmpty();
        assertThat(notes.stream().anyMatch(n -> n.getFull_name().equals("Emilie Nutella"))).isTrue();
    }

    @Test
    void testGetWeekNotes() {
        // get entire weeks notes for dorte
        LocalDate date = LocalDate.of(2024, 12, 2);
        int userId = 2;

        HttpHeaders headers = createHeadersWithApiKey();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/note/week/history?date=" + date + "&userId=" + userId;
        ResponseEntity<List<List<Note>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<List<Note>>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<List<Note>> weekNotes = response.getBody();
        assertThat(weekNotes).hasSize(7); // 7 days of the week

        // check if dorte notes are found for date 2024-12-04
        boolean foundDorteNote = weekNotes.stream()
                .flatMap(List::stream)
                .anyMatch(n -> "Dorte Johannes".equals(n.getFull_name()) &&
                        n.getNote_date().equals(LocalDate.of(2024, 12, 4)));
        assertThat(foundDorteNote).isTrue();
    }

    @Test
    void testAddNewNote() {
        // create a new note
        HttpHeaders headers = createHeadersWithApiKey();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Note newNote = new Note(
                LocalDate.of(2024, 12, 10),
                3,
                1,
                "Emilie Nutella",
                "Husk at checke ud i dag!"
        );

        HttpEntity<Note> requestEntity = new HttpEntity<>(newNote, headers);

        ResponseEntity<Note> response = restTemplate.exchange(
                baseUrl + "/api/note/addNewNote",
                HttpMethod.POST,
                requestEntity,
                Note.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Note createdNote = response.getBody();
        assertThat(createdNote).isNotNull();
        assertThat(createdNote.getNote_id()).isGreaterThan(0);
        assertThat(createdNote.getFull_name()).isEqualTo("Emilie Nutella");

        // verify that the new note can be found
        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        ResponseEntity<List<Note>> getDayResponse = restTemplate.exchange(
                baseUrl + "/api/note/day?date=2024-12-10&userId=3",
                HttpMethod.GET,
                getRequest,
                new ParameterizedTypeReference<List<Note>>() {}
        );

        assertThat(getDayResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Note> notesOnDay = getDayResponse.getBody();
        assertThat(notesOnDay.stream().anyMatch(n -> n.getFull_name().equals("Emilie Nutella"))).isTrue();
    }
}