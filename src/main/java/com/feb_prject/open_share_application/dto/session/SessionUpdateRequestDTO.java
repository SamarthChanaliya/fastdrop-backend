package com.feb_prject.open_share_application.dto.session;

import com.feb_prject.open_share_application.enums.Discoverability;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;

@Data
public class SessionUpdateRequestDTO {

    private Long id; // only for PUT/PATCH, ignored in POST

    @NotBlank(message = "Title is required")
    @Size(max = 100)
    private String title;

    @Future(message = "Expiry must be in the future")
    private Instant expires_at;

    @Positive(message = "Radius must be positive")
    private Integer radius_meters;

    @NotNull
    private Discoverability discoverability;// Use enum?

    private Boolean is_active;

    @NotNull(message = "Host ID is required")
    private String host_id;

    private String join_code;
}
