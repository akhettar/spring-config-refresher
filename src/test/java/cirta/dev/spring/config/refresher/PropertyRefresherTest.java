package cirta.dev.spring.config.refresher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;

import org.springframework.boot.web.client.RestTemplateBuilder;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class PropertyRefresherTest {

    private PropertyRefresher propertyRefresher;
    private ClientAndServer mockServer;

    @BeforeEach
    public void startServer() {
        createExpectationForRefreshEndpoint(startClientAndServer(8080));
        RestTemplateBuilder builder = new RestTemplateBuilder();
        propertyRefresher = new PropertyRefresher(builder.rootUri("http://localhost:8080").build(), "");
    }

    @Test
    public void cronJobSch() {
        assertDoesNotThrow(() -> propertyRefresher.cronJobSch());
    }

    private void createExpectationForRefreshEndpoint(final ClientAndServer clientAndServer) {

        clientAndServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/actuator/refresh")
                                .withHeader("\"Content-type\", \"application/json\""))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"))
                                .withBody("{ message: 'application config successfully refreshed' }")
                                .withDelay(TimeUnit.SECONDS,5)
                );
    }
}