package com.fastdrop.api.service;

import com.fastdrop.api.dto.session.request.SessionUpdateRequestDTO;
import com.fastdrop.api.dto.session.response.SessionRowDTO;
import com.fastdrop.api.exception.SupabaseException;
import com.fastdrop.api.utils.JoinCodeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastdrop.api.dto.session.request.SessionCreateRequestDTO;
import com.fastdrop.api.dto.session.request.SessionJoinValidationRequestDTO;
import com.fastdrop.api.dto.session.response.SessionJoinValidationResponseDTO;
import com.fastdrop.api.dto.session.response.NearbySessionResponseDTO;
import com.fastdrop.api.exception.supabase.SupabaseErrorHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
public class SessionsService {

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
                        .filter(this::isDuplicateKeyError)
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
        map.put("sharing_enabled", createRequestDTO.getSharingEnabled());
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

    public Mono<SessionRowDTO> partialUpdateSession(String jwt, @Valid SessionUpdateRequestDTO sessionUpdateRequestDTO) {
        return supabaseWebClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/sessions")
                        .queryParam("id","eq." + sessionUpdateRequestDTO.getSessionId())
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .header("Prefer", "return=representation")
                .bodyValue(toSupabaseMap(sessionUpdateRequestDTO))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        SupabaseErrorHandler.error("Failed to update session").apply(response)
                )
                .bodyToMono(SessionRowDTO[].class)
                .flatMap(rows -> {
                    if (rows.length == 0) {
                        return Mono.error(new RuntimeException("Session not found"));
                    }
                    return Mono.just(rows[0]);
                });
    }
    private Map<String, Object> toSupabaseMap(SessionUpdateRequestDTO sessionUpdateRequestDTO) {
        Map<String, Object> map = new HashMap<>();
        if (sessionUpdateRequestDTO.getTitle() != null)
            map.put("title", sessionUpdateRequestDTO.getTitle());

        if (sessionUpdateRequestDTO.getExpiresAt() != null)
            map.put("expires_at", sessionUpdateRequestDTO.getExpiresAt());

        if (sessionUpdateRequestDTO.getRadiusMeters() != null) {
            map.put("radius_meters", sessionUpdateRequestDTO.getRadiusMeters());
        }

        if (sessionUpdateRequestDTO.getRequiresCode() != null) {
            map.put("requires_code", sessionUpdateRequestDTO.getRequiresCode());
        }

        if (sessionUpdateRequestDTO.getSharingEnabled() != null) {
            map.put("sharing_enabled", sessionUpdateRequestDTO.getSharingEnabled());
        }

        return map;
    }
}