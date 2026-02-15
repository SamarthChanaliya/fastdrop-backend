package com.fastdrop.api.dto.session.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;

@Data
public class SessionUpdateRequestDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100)
    private String title;

    @Future(message = "Expiry must be in the future")
    private Instant expires_at;

    @Positive(message = "Radius must be positive")
    private Integer radius_meters;

    private Boolean is_active;

    @NotNull(message = "Host ID is required")
    private String host_id;

    private String join_code;
}
