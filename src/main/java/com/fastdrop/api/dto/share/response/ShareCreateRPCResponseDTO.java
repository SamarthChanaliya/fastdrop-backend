package com.fastdrop.api.dto.share.response;

public record ShareCreateRPCResponseDTO<T>(
        Boolean success,
        String message,
        T data
) {
}
