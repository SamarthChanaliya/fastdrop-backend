package com.feb_prject.open_share_application.controller;

import com.feb_prject.open_share_application.service.NearbySharesService;
import com.feb_prject.open_share_application.wrapper.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/shares",produces = MediaType.APPLICATION_JSON_VALUE)
public class SharesController {

    private final NearbySharesService service;

    public SharesController(NearbySharesService service) {
        this.service = service;
    }

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> nearbyShares(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radius) {

        String jwt = authHeader.replace("Bearer ", "");
        List<Map<String,Object>> shares =  service.getNearbyShares(jwt, lat, lng, radius);

        return ResponseEntity.ok(
                ApiResponse.success("Nearby shares fetched successfully",shares)
        );

    }

    @GetMapping("/ping")
    public Map<String, String> getPing() {
        return Map.of("Status", "OK");
    }

    @GetMapping("/all")
    public List<Map<String ,Object>> allShares(){
        return service.getAllShares();
    }

    @GetMapping("/throw")
    public ResponseEntity<ApiResponse<Void>> throw_error() throws Exception {
        throw new Exception("Im throwing");
    }
}
