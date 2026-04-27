package hr.algebra.iisCoreBackend.config;

import hr.algebra.iisCoreBackend.security.ReqResApiKeyInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestClientConfig {

    @Value("${app.reqres.api.key}")
    private String reqresApiKey;

    @Bean
    @Qualifier("reqresRestTemplate")
    public RestTemplate reqresRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new ReqResApiKeyInterceptor(reqresApiKey)));
        return restTemplate;
    }

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}