package com.fastdrop.api.service;

import com.fastdrop.api.dto.share.request.*;
import com.fastdrop.api.dto.share.response.ShareCreateRPCResponseDTO;
import com.fastdrop.api.dto.share.response.ShareItemsRowDTO;
import com.fastdrop.api.exception.supabase.SupabaseErrorHandler;
import jakarta.validation.Valid;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ShareService {

    private final WebClient supabaseClient;

    public ShareService(WebClient supabaseClient) {
        this.supabaseClient = supabaseClient;
    }

    public Mono<ShareCreateRPCResponseDTO<ShareItemsRowDTO>> createTextShare(String jwt, TextShareCreateRequestDTO request
    ) {

        return supabaseClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/rpc/create_text_share")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .bodyValue(Map.of(
                        "p_session_id", request.sessionId(),
                        "p_user_id", request.createdBy(),
                        "p_share_title", request.title(),
                        "p_item_title", request.title(),
                        "p_content_text", request.contentText()
                ))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        SupabaseErrorHandler.error("Supabase create text share RPC failed: ")
                )
                .bodyToMono(new ParameterizedTypeReference<ShareCreateRPCResponseDTO<ShareItemsRowDTO>>() {});
    }

    public Mono<ShareCreateRPCResponseDTO<ShareItemsRowDTO>> createCodeShare(String jwt, CodeShareCreateRequestDTO request
    ) {

        return supabaseClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/rpc/create_code_share")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .bodyValue(Map.of(
                        "p_session_id", request.sessionId(),
                        "p_user_id", request.createdBy(),
                        "p_share_title", request.title(),
                        "p_item_title", request.title(),
                        "p_content_text", request.contentText(),
                        "p_language", request.language()
                ))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        SupabaseErrorHandler.error("Supabase create Code share RPC failed: ")
                )
                .bodyToMono(new ParameterizedTypeReference<ShareCreateRPCResponseDTO<ShareItemsRowDTO>>() {});
    }

    public Mono<ShareCreateRPCResponseDTO<ShareItemsRowDTO>> createLinkShare(String jwt, LinkShareCreateRequestDTO request
    ) {

        return supabaseClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/rpc/create_link_share")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .bodyValue(Map.of(
                        "p_session_id", request.sessionId(),
                        "p_user_id", request.createdBy(),
                        "p_share_title", request.title(),
                        "p_item_title", request.title(),
                        "p_content_text", request.contentText()
                ))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        SupabaseErrorHandler.error("Supabase create Code share RPC failed: ")
                )
                .bodyToMono(new ParameterizedTypeReference<ShareCreateRPCResponseDTO<ShareItemsRowDTO>>() {});
    }

    public Mono<ShareCreateRPCResponseDTO<ShareItemsRowDTO>> createFileShare(String jwt, @Valid FileShareCreateRequestDTO request) {
        return supabaseClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/rpc/create_file_share")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .bodyValue(Map.of(
                        "p_session_id", request.sessionId(),
                        "p_user_id", request.createdBy(),
                        "p_share_title", request.title(),
                        "p_item_title", request.title(),
                        "p_file_type", request.fileType(),
                        "p_file_name", request.fileName(),
                        "p_file_path", request.filePath()
                ))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        SupabaseErrorHandler.error("Supabase create File share RPC failed: ")
                )
                .bodyToMono(new ParameterizedTypeReference<ShareCreateRPCResponseDTO<ShareItemsRowDTO>>() {});
    }

    public Mono<ShareCreateRPCResponseDTO<String>> createMultiShare(String jwt, @Valid MultiShareCreateRequestDTO request) {
        return supabaseClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/rpc/create_folder_share")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .bodyValue(Map.of(
                        "p_session_id", request.sessionId(),
                        "p_user_id", request.createdBy(),
                        "p_share_title", request.title(),
                        "p_items", request.items()
                ))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        SupabaseErrorHandler.error("Supabase create Multi share RPC failed: ")
                )
                .bodyToMono(new ParameterizedTypeReference<ShareCreateRPCResponseDTO<String>>() {});
    }
}
