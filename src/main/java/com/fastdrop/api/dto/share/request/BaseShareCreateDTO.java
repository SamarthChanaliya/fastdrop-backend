package com.fastdrop.api.dto.share.request;

public record BaseShareCreateDTO(
        String sessionId,
        String title,
        String shareType,
        String userId
) {
}
