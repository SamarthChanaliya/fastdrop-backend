package com.fastdrop.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@ConfigurationProperties(prefix = "supabase")
@Getter
@Setter
@Configuration
public class SupabaseConfig {
    private String url;
    private String key;
    private String rest;


    @Bean
    public WebClient supabaseWebClient(WebClient.Builder builder) {

        return builder
                .baseUrl(this.getRest())
                .defaultHeader("apiKey", this.getKey())
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }
}