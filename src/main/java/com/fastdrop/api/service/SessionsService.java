package com.fastdrop.api.service;

import com.fastdrop.api.dto.session.response.SessionRowDTO;
import com.fastdrop.api.exception.SupabaseException;
import com.fastdrop.api.utils.JoinCodeGenerator;
import com.fastdrop.api.wrapper.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastdrop.api.dto.session.request.SessionCreateRequestDTO;
import com.fastdrop.api.dto.session.request.SessionJoinValidationRequestDTO;
import com.fastdrop.api.dto.session.response.SessionJoinValidationResponseDTO;
import com.fastdrop.api.dto.session.response.NearbySessionResponseDTO;
import com.fastdrop.api.exception.supabase.SupabaseErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
public class SessionsService {

    @Autowired
    private final WebClient supabaseWebClient;

    public SessionsService(WebClient supabaseWebClient, ObjectMapper objectMapper) {
        this.supabaseWebClient = supabaseWebClient;
    }

    public Mono<NearbySessionResponseDTO> createSession(String jwt, SessionCreateRequestDTO createRequestDTO) {
        return supabaseWebClient.post()
                .uri("/sessions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .header("Prefer", "return=representation")
                .bodyValue(toSupabaseMap(createRequestDTO))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        SupabaseErrorHandler.error("Failed to create session").apply(response)
                )
                .bodyToMono(NearbySessionResponseDTO[].class)
                .map(rows -> rows[0])
                .retryWhen(Retry.max(3)
                        .filter(throwable -> isDuplicateKeyError(throwable))
                        .onRetryExhaustedThrow((spec, signal) -> signal.failure())
                );
    }

    private boolean isDuplicateKeyError(Throwable throwable) {
        return throwable instanceof SupabaseException && throwable.getMessage().contains("23505");
    }

    public Flux<NearbySessionResponseDTO> getNearbySessions(double lng, double lat) {

        return supabaseWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/rpc/get_nearby_sessions")
                        .queryParam("order", "created_at.desc")
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue(Map.of(
                        "lng", lng,
                        "lat", lat
                ))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        SupabaseErrorHandler.error("Supabase RPC failed: ")
                )
                .bodyToFlux(NearbySessionResponseDTO.class);
    }

    private Map<String, Object> toSupabaseMap(SessionCreateRequestDTO createRequestDTO) {
        String postGisLocation = String.format("POINT(%f %f)", createRequestDTO.getLng(), createRequestDTO.getLat());
        Map<String, Object> map = new HashMap<>();
        map.put("expires_at", createRequestDTO.getExpiresAt());
        map.put("join_code", JoinCodeGenerator.generate());
        map.put("title", createRequestDTO.getTitle());
        map.put("location", postGisLocation);
        map.put("radius_meters", createRequestDTO.getRadiusMeters());
        map.put("host_id", createRequestDTO.getHostId());
        map.put("requires_code", createRequestDTO.getRequiresCode());
        return map;
    }

    public Mono<SessionJoinValidationResponseDTO> validateJoinCode(String sessionID, String providedJoinCode) {
        return supabaseWebClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/sessions")
                                .queryParam("id", "eq." + sessionID)
                                .queryParam("select", "join_code")
                                .build()
                        )
                        .retrieve()
                        .onStatus(
                                HttpStatusCode::isError,
                                SupabaseErrorHandler.error("Failed to validate join code")
                        )
                        .bodyToFlux(SessionJoinValidationRequestDTO.class)
                        .map(SessionJoinValidationRequestDTO::join_code)
                        .next()
                        .map(actualJoinCode -> {
                            boolean isValid = providedJoinCode.equals(actualJoinCode);
                            return new SessionJoinValidationResponseDTO(isValid);
                        })
                .defaultIfEmpty(new SessionJoinValidationResponseDTO(false));
    }


    public Mono<SessionRowDTO> getActiveSession(String userId) {
        return supabaseWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/sessions")
                        .queryParam("select","*")
                        .queryParam("host_id", "eq." + userId)
                        .queryParam("ended_by_host", "eq.false")
                        .queryParam("expires_at", "gt." + OffsetDateTime.now(ZoneOffset.UTC).toString())
                        .queryParam("order", "created_at.desc")
                        .queryParam("limit", "1")
                        .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        SupabaseErrorHandler.error("Failed to fetch active session")
                )
                .bodyToMono(SessionRowDTO[].class)
                .flatMap(rows -> Mono.justOrEmpty(rows.length > 0 ? rows[0] : null));
    }
}