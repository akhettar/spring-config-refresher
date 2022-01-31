package cirta.dev.spring.config.refresher;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;


@Component
public class PropertyRefresher {

    private static final String ACTUATOR_REFRESH = "/actuator/refresh";
    private final String context;
    private Logger logger = LoggerFactory.getLogger(PropertyRefresher.class);
    private final RestTemplate restTemplate;

    @Autowired
    public PropertyRefresher(final RestTemplate restTemplate,
                             final @Value("${server.servlet.context-path:}") String context) {
        this.restTemplate = restTemplate;
        this.context = context;
    }

    @Scheduled(fixedRateString = "${propertiesRefreshRate:900000}")
    public void cronJobSch() {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> request = new HttpEntity<>(headers);
        try {
            String response = restTemplate.postForObject(StringUtils.hasText(context)?
                    String.format("/%s%s", context, ACTUATOR_REFRESH) :
                    ACTUATOR_REFRESH, request, String.class);
            logger.info("Successfully refreshed the app properties" + response);
        }catch (Exception ex) {
            logger.warn("failed to refresh the app properties");
        }
    }
}

