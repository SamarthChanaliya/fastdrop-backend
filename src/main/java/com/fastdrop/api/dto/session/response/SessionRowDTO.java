package com.fastdrop.api.dto.session.response;

import java.time.OffsetDateTime;

public record SessionRowDTO(
        String id,
        String title,
        OffsetDateTime expiresAt,
        OffsetDateTime createdAt,
        String hostId,
        Integer radiusMeters,
        Boolean endedByHost,
        Integer distanceMeters,
        Integer sharesCount,
        Boolean requiresCode
) { }
