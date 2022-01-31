package cirta.dev.spring.config.refresher;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    private static final String LOCAL_HOST = "http://127.0.0.1:%d";

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder, 
                                     final @Value("${server.port:8080}") int port) {
        return builder.rootUri(String.format(LOCAL_HOST, port)).build();
    }
}
