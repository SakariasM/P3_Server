package com.p3.Server.timelog;

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
public class TimelogControllerIntegrationTest {

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
    void testGetTimelogsByDate() {
        LocalDate date = LocalDate.of(2024, 11, 1); // from our database

        HttpHeaders headers = createHeadersWithApiKey();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/timelog/getTimelogsByDate?date=" + date;
        ResponseEntity<List<Timelog>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Timelog>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Timelog> timelogs = response.getBody();
        assertThat(timelogs).isNotEmpty();
        // checks if there is a check in event for brian
        boolean found = timelogs.stream().anyMatch(t -> t.getUser_id() == 1 && "check_in".equals(t.getEvent_type()));
        assertThat(found).isTrue();
    }

    @Test
    void testGetTimelogsByDateAndId() {
        LocalDate date = LocalDate.of(2024, 11, 1);
        int userId = 1;

        HttpHeaders headers = createHeadersWithApiKey();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/timelog/day?date=" + date + "&userId=" + userId;
        ResponseEntity<List<Timelog>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Timelog>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Timelog> timelogs = response.getBody();
        assertThat(timelogs).isNotEmpty();
        // check that timelogs are from the user and date
        for (Timelog t : timelogs) {
            assertThat(t.getUser_id()).isEqualTo(userId);
            assertThat(t.getShift_date()).isEqualTo(date);
        }
    }

    @Test
    void testGetLastCheckOutEvent() {
        // find brians last checkout equal to 2024-11-01 23:00:00
        int userId = 1;

        HttpHeaders headers = createHeadersWithApiKey();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/timelog/lastCheckOut?user_id=" + userId;
        ResponseEntity<Timelog> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Timelog.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Timelog lastCheckout = response.getBody();
        assertThat(lastCheckout).isNotNull();
        // check that event_type is 'check_out' or 'no_event'
        assertThat(lastCheckout.getEvent_type()).isIn("check_out", "no_event");
    }

    @Test
    void testGetWeekTimelogs() {
        LocalDate date = LocalDate.of(2024, 12, 2);
        int userId = 1;

        HttpHeaders headers = createHeadersWithApiKey();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/timelog/weekly?date=" + date + "&userId=" + userId;
        ResponseEntity<List<List<Timelog>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<List<Timelog>>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<List<Timelog>> weekLogs = response.getBody();
        assertThat(weekLogs).hasSize(7);

        // check if any day in that week for brian is not empty
        boolean anyDayNotEmpty = weekLogs.stream().anyMatch(dayLogs -> !dayLogs.isEmpty());
        assertThat(anyDayNotEmpty).isTrue();
    }

    @Test
    void testPostCheckIn() {
        // post a check_in event for brian and verify its added
        HttpHeaders headers = createHeadersWithApiKey();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Timelog newCheckIn = new Timelog();
        newCheckIn.setUser_id(1);
        newCheckIn.setEvent_type("check_in");

        HttpEntity<Timelog> requestEntity = new HttpEntity<>(newCheckIn, headers);
        ResponseEntity<Void> postResponse = restTemplate.exchange(
                baseUrl + "/api/timelog/checkIn",
                HttpMethod.POST,
                requestEntity,
                Void.class
        );

        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // verify the Timelog was created for brian
        LocalDate today = LocalDate.now();
        String url = baseUrl + "/api/timelog/day?date=" + today + "&userId=1";
        ResponseEntity<List<Timelog>> getResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Timelog>>() {}
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Timelog> logs = getResponse.getBody();
        assertThat(logs).isNotEmpty();
        boolean hasCheckIn = logs.stream().anyMatch(t -> "check_in".equals(t.getEvent_type()));
        assertThat(hasCheckIn).isTrue();
    }

    @Test
    void testDownloadCSV() {
        // export csv test between period
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 2);

        HttpHeaders headers = createHeadersWithApiKey();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/timelog/downloadCSV?startDate=" + startDate + "&endDate=" + endDate;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            System.out.println("No timelogs found in that period");
        } else {
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            String csv = response.getBody();
            assertThat(csv).isNotBlank();
            assertThat(csv).contains("ID,User ID,Shift Date,Event Type,Event Time");
        }
    }

    @Test
    void testPutTimelogs() {
        // test updating or adding multiple timelogs at once
        LocalDate date = LocalDate.of(2024, 11, 1);
        int userId = 1;

        HttpHeaders headers = createHeadersWithApiKey();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String getUrl = baseUrl + "/api/timelog/day?date=" + date + "&userId=" + userId;
        ResponseEntity<List<Timelog>> getResponse = restTemplate.exchange(
                getUrl,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Timelog>>() {}
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Timelog> dayLogs = getResponse.getBody();
        assertThat(dayLogs).isNotEmpty();

        Timelog toUpdate = dayLogs.get(0);
        toUpdate.setEvent_type("check_in");

        HttpEntity<List<Timelog>> putEntity = new HttpEntity<>(dayLogs, headers);
        ResponseEntity<Void> putResponse = restTemplate.exchange(
                baseUrl + "/api/timelog/list",
                HttpMethod.PUT,
                putEntity,
                Void.class
        );

        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // verify change
        ResponseEntity<List<Timelog>> verifyResponse = restTemplate.exchange(
                getUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Timelog>>() {}
        );

        List<Timelog> updatedDayLogs = verifyResponse.getBody();
        assertThat(updatedDayLogs).isNotEmpty();
        // check if the updated event_type is present
        boolean updated = updatedDayLogs.stream().anyMatch(t -> t.getLog_id() == toUpdate.getLog_id() && "check_in".equals(t.getEvent_type()));
        assertThat(updated).isTrue();
    }
}