package com.feb_prject.open_share_application.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class NearbySharesService {

    @Autowired
    private final WebClient supabaseWebClient;

    public NearbySharesService(WebClient supabaseWebClient) {
        this.supabaseWebClient = supabaseWebClient;
    }

    public List<Map<String, Object>> getNearbyShares(
            String jwt, double lat, double lng, double radius) {

        return supabaseWebClient.post()
                .uri("/rpc/nearby_shares")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .header(HttpHeaders.ACCEPT, "application/json")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue(Map.of(
                        "lat", lat,
                        "lng", lng,
                        "radius_meters", radius
                ))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();
    }
}
