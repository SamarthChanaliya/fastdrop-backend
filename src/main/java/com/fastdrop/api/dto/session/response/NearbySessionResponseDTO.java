package com.fastdrop.api.dto.session.response;

import java.time.OffsetDateTime;

/**
 * DTO for Session responses.
 * Fields are automatically converted to snake_case in JSON
 * (e.g., expiresAt -> expires_at) via application.properties.
 */
public record NearbySessionResponseDTO(
        String id,
        String title,
        OffsetDateTime expiresAt,
        OffsetDateTime createdAt,
        String hostId,
        Integer radiusMeters,
        Boolean endedByHost,
        Integer distanceMeters,
        Integer sharesCount,
        Boolean requiresCode,
        Boolean sharingEnabled
) {}