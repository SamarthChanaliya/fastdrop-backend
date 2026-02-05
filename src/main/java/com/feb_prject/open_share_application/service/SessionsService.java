package com.feb_prject.open_share_application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feb_prject.open_share_application.constant.SupabaseSelects;
import com.feb_prject.open_share_application.dto.session.SessionCreateRequestDTO;
import com.feb_prject.open_share_application.dto.session.SessionResponseDTO;
import com.feb_prject.open_share_application.exception.supabase.SupabaseErrorHandler;
import com.feb_prject.open_share_application.utils.JoinCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SessionsService {

    @Autowired
    private final WebClient supabaseWebClient;

    public SessionsService(WebClient supabaseWebClient, ObjectMapper objectMapper) {
        this.supabaseWebClient = supabaseWebClient;
    }

    public SessionResponseDTO createSession(String jwt, SessionCreateRequestDTO createRequestDTO){

        return supabaseWebClient.post()
                .uri("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + jwt)
                .bodyValue(toSupabaseMap(createRequestDTO))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        SupabaseErrorHandler.error("Session was not created.")
                )
                .bodyToMono(SessionResponseDTO.class)
                .block();
    }

    public List<SessionResponseDTO> getNearbySessions(double lat, double lng, double radius) {

        return supabaseWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/rpc/get_nearby_sessions")
                        .queryParam("select", SupabaseSelects.SESSION_DETAILS_SELECT)
                        .queryParam("order", "created_at.desc")
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue(Map.of(
                        "lat", lat,
                        "lng", lng,
                        "radius_meters", radius
                ))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        SupabaseErrorHandler.error("Supabase RPC failed: ")
                )
                .bodyToFlux(SessionResponseDTO.class)
                .collectList()
                .block();
    }

    private Map<String, Object> toSupabaseMap(SessionCreateRequestDTO dto) {
        String postGisLocation = String.format("POINT(%f %f)", dto.getLng(), dto.getLat());
        Map<String, Object> map = new HashMap<>();

        //create join_code if needed
        Boolean requiresJoinCode = dto.getRequires_join_code();
        if (requiresJoinCode){
            String joinCode = generateUniqueJoinCode();
            map.put("join_code",joinCode);
        }
        map.put("title", dto.getTitle());
        map.put("expires_at", dto.getExpires_at());
        map.put("location", postGisLocation);
        map.put("radius_meters", dto.getRadius_meters());
        map.put("discoverability", dto.getDiscoverability().getDbValue());
        map.put("host_id", dto.getHost_id());
        return map;
    }

    public String generateUniqueJoinCode() {

        for (int i = 0; i < 5; i++) {
            String code = JoinCodeGenerator.generate();
            try {
                return code;
            } catch (DuplicateKeyException ignored) {
            }
        }

        throw new IllegalStateException("Unable to generate unique join code");
    }


}
