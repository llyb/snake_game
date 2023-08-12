package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfjg {
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
