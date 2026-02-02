package com.feb_prject.open_share_application.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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


    @Bean
    public WebClient supabaseWebClient(WebClient.Builder builder) {
//        System.out.println("SUPABASE_KEY length: " +
//                (this.key != null ? this.key.length() : "null"));

        return builder
                .baseUrl("https://rjsgaildagzjvmemaqbc.supabase.co/rest/v1")
                .defaultHeader("apiKey", this.getKey())
                .build();
    }
}