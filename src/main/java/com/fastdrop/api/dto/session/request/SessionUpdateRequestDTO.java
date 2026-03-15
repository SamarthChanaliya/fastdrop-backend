package com.fastdrop.api.dto.session.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;

@Data
public class SessionUpdateRequestDTO {

    private String  sessionId;

    @NotBlank(message = "Title is required")
    private String title;

    private String  expiresAt;

    @Positive(message = "Radius must be positive")
    private Integer radiusMeters;

    private Boolean requiresCode;

    private Boolean sharingEnabled;
}
