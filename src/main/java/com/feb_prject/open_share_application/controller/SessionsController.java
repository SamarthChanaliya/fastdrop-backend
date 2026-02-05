package com.feb_prject.open_share_application.controller;

import com.feb_prject.open_share_application.dto.session.SessionCreateRequestDTO;
import com.feb_prject.open_share_application.dto.session.SessionResponseDTO;
import com.feb_prject.open_share_application.service.SessionsService;
import com.feb_prject.open_share_application.wrapper.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/sessions",produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionsController {

    private final SessionsService sessionsService;

    public SessionsController(SessionsService sessionsService) {
        this.sessionsService = sessionsService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SessionResponseDTO>> createNewShare(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SessionCreateRequestDTO sessionCreateRequestDTO){

        if (!authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        String jwt = authHeader.substring(7);
        SessionResponseDTO session = sessionsService.createSession(jwt,sessionCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Session created successfully",session));
    }

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<List<SessionResponseDTO>>> getNearbySessions(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radius){
        List<SessionResponseDTO> nearbySessions = sessionsService.getNearbySessions(lat,lng,radius);
        System.out.println(nearbySessions.toString());


        return ResponseEntity.ok(
                ApiResponse.success("Nearby sessions fetched successfully", nearbySessions)
        );
    }


}