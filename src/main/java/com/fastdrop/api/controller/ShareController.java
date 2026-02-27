package com.fastdrop.api.controller;

import com.fastdrop.api.dto.share.request.*;
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

    @PostMapping("/link")
    public Mono<ApiResponse<ShareItemsRowDTO>> createLinkShare(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody LinkShareCreateRequestDTO createLinkShareRequestDTO
    ){
        if (!authHeader.startsWith("Bearer ")){
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String jwt = authHeader.substring(7);
        System.out.println("JWT: "+jwt);
        Mono<ShareCreateRPCResponseDTO<ShareItemsRowDTO>> createdShare = shareservice.createLinkShare(jwt,createLinkShareRequestDTO);
        return createdShare.map(
                rpcResponse -> ApiResponse.success(rpcResponse.message(), rpcResponse.data())
        );
    }

    @PostMapping("/file")
    public Mono<ApiResponse<ShareItemsRowDTO>> createFileShare(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody FileShareCreateRequestDTO createFileShareRequestDTO
    ){
        if (!authHeader.startsWith("Bearer ")){
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String jwt = authHeader.substring(7);
        System.out.println("JWT: "+jwt);
        Mono<ShareCreateRPCResponseDTO<ShareItemsRowDTO>> createdShare = shareservice.createFileShare(jwt,createFileShareRequestDTO);
        return createdShare.map(
                rpcResponse -> ApiResponse.success(rpcResponse.message(), rpcResponse.data())
        );
    }

    @PostMapping("/multi")
    public Mono<ApiResponse<String >> createMultiShare(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody MultiShareCreateRequestDTO createMultiShareRequestDTO
    ){
        if (!authHeader.startsWith("Bearer ")){
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String jwt = authHeader.substring(7);
        System.out.println("JWT: "+jwt);
        Mono<ShareCreateRPCResponseDTO<String>> createdShare = shareservice.createMultiShare(jwt,createMultiShareRequestDTO);
        return createdShare.map(
                rpcResponse -> ApiResponse.success(rpcResponse.message(), rpcResponse.data())
        );
    }
}