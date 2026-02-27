package com.fastdrop.api.dto.share.response;

public record SharesRowDTO(
        String id,
        String createdBy,
        String sessionId,
        String createdAt,
        String shareType,
        String title
) {

}
