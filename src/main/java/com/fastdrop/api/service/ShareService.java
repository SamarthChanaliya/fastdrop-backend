package com.fastdrop.api.service;

import ch.qos.logback.core.joran.spi.HttpUtil;
import com.fastdrop.api.dto.session.response.NearbySessionResponseDTO;
import com.fastdrop.api.dto.share.request.BaseShareCreateDTO;
import com.fastdrop.api.dto.share.request.CodeShareCreateRequestDTO;
import com.fastdrop.api.dto.share.request.TextShareCreateRequestDTO;
import com.fastdrop.api.dto.share.response.ShareCreateRPCResponseDTO;
import com.fastdrop.api.dto.share.response.ShareItemsRowDTO;
import com.fastdrop.api.exception.supabase.SupabaseErrorHandler;
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
}
