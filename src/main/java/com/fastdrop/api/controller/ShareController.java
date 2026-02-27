package com.fastdrop.api.controller;

import com.fastdrop.api.dto.share.request.CodeShareCreateRequestDTO;
import com.fastdrop.api.dto.share.request.TextShareCreateRequestDTO;
import com.fastdrop.api.dto.share.response.ShareCreateRPCResponseDTO;
import com.fastdrop.api.dto.share.response.ShareItemsRowDTO;
import com.fastdrop.api.service.ShareService;
import com.fastdrop.api.wrapper.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/share",produces = MediaType.APPLICATION_JSON_VALUE)
public class ShareController {
    private final ShareService shareservice;

    public ShareController(ShareService shareservice) {
        this.shareservice = shareservice;
    }

    @PostMapping("/text")
    public Mono<ApiResponse<ShareItemsRowDTO>> createTextShare(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody TextShareCreateRequestDTO createTextShareRequestDTO
    ){
        if (!authHeader.startsWith("Bearer ")){
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String jwt = authHeader.substring(7);
        System.out.println("JWT: "+jwt);
        Mono<ShareCreateRPCResponseDTO<ShareItemsRowDTO>> createdShare = shareservice.createTextShare(jwt,createTextShareRequestDTO);
        return createdShare.map(
                rpcResponse -> ApiResponse.success(rpcResponse.message(), rpcResponse.data())
        );
    }

    @PostMapping("/code")
    public Mono<ApiResponse<ShareItemsRowDTO>> createCodeShare(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CodeShareCreateRequestDTO createCodeShareRequestDTO
    ){
        if (!authHeader.startsWith("Bearer ")){
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String jwt = authHeader.substring(7);
        System.out.println("JWT: "+jwt);
        Mono<ShareCreateRPCResponseDTO<ShareItemsRowDTO>> createdShare = shareservice.createCodeShare(jwt,createCodeShareRequestDTO);
        return createdShare.map(
                rpcResponse -> ApiResponse.success(rpcResponse.message(), rpcResponse.data())
        );
    }
}