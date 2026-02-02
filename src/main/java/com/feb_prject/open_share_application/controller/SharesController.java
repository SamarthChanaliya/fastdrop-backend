package com.feb_prject.open_share_application.controller;

import com.feb_prject.open_share_application.service.NearbySharesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shares")
public class SharesController {

    private final NearbySharesService service;

    public SharesController(NearbySharesService service) {
        this.service = service;
    }

    @GetMapping("/nearby")
    public List<Map<String, Object>> nearby(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radius) {

        System.out.println("Request came");
        String jwt = authHeader.replace("Bearer ", "");
        System.out.println(jwt);
        return service.getNearbyShares(jwt, lat, lng, radius);
    }

    @GetMapping("/ping")
    public Map<String, String> getPing() {
        return Map.of("Status", "OK");
    }
}
