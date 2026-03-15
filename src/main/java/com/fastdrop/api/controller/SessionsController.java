package com.fastdrop.api.controller;

import com.fastdrop.api.dto.session.request.ActiveSessionRequestDTO;
import com.fastdrop.api.dto.session.request.SessionCreateRequestDTO;
import com.fastdrop.api.dto.session.request.SessionJoinValidationRequestDTO;
import com.fastdrop.api.dto.session.request.SessionUpdateRequestDTO;
import com.fastdrop.api.dto.session.response.SessionJoinValidationResponseDTO;
import com.fastdrop.api.dto.session.response.NearbySessionResponseDTO;
import com.fastdrop.api.dto.session.response.SessionRowDTO;
import com.fastdrop.api.service.SessionsService;
import com.fastdrop.api.wrapper.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/sessions",produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionsController {

    private final SessionsService sessionsService;

    public SessionsController(SessionsService sessionsService) {
        this.sessionsService = sessionsService;
    }

    @PostMapping
    public Mono<ApiResponse<NearbySessionResponseDTO>> createNewSession(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SessionCreateRequestDTO sessionCreateRequestDTO){

        if (!authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        String jwt = authHeader.substring(7);
        System.out.println("JWT: "+jwt);
        return sessionsService.createSession(jwt,sessionCreateRequestDTO)
                .map(nearbySessionResponseDTO -> ApiResponse.success("Session created successfully", nearbySessionResponseDTO));
    }

    @GetMapping("/nearby")
    public Mono<ApiResponse<List<NearbySessionResponseDTO>>> getNearbySessions(
            @RequestParam double lng,
            @RequestParam double lat)
    {
        return sessionsService.getNearbySessions(lng,lat)
                .collectList()
                .map(list -> ApiResponse.success("Nearby sessions fetched successfully",list));
    }

    @PostMapping("/{sessionId}/join")
    public Mono<ApiResponse<SessionJoinValidationResponseDTO>> validateJoinCode(
            @PathVariable String sessionId,
            @RequestBody SessionJoinValidationRequestDTO requestDTO){
        String joinCode = requestDTO.join_code();
         return sessionsService.validateJoinCode(sessionId,joinCode)
                 .map(validation -> ApiResponse.success("Join code validation successfully",validation));
    }

    @PostMapping("/active")
    public Mono<ApiResponse<SessionRowDTO>> getActiveSession(@RequestBody ActiveSessionRequestDTO activeSessionRequestDTO){

        String userId = activeSessionRequestDTO.userId();
        return sessionsService.getActiveSession(userId)
                .map(active -> ApiResponse.success("Successfully fetched active session",active))
                .defaultIfEmpty(ApiResponse.success("No active session found", null));
    }

    @PatchMapping("/edit")
    public Mono<ApiResponse<SessionRowDTO>> updateSession(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SessionUpdateRequestDTO sessionUpdateRequestDTO){

        if (!authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        String jwt = authHeader.substring(7);
        System.out.println("JWT: "+jwt);
        return sessionsService.partialUpdateSession(jwt,sessionUpdateRequestDTO)
                .map(nearbySessionResponseDTO -> ApiResponse.success("Session Updated successfully", nearbySessionResponseDTO));
    }
}