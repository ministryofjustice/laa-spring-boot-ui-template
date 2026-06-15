package uk.gov.justice.laa.springboot.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootUiApplicationIntegrationTest {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
    }

    @Test
    void homePage_returns200() throws Exception {
        HttpResponse<String> response = httpClient.send(
            HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/")).GET().build(),
            HttpResponse.BodyHandlers.ofString()
        );
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void componentsPage_returns200() throws Exception {
        HttpResponse<String> response = httpClient.send(
            HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/components")).GET().build(),
            HttpResponse.BodyHandlers.ofString()
        );
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
