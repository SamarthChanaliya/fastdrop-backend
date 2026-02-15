package com.fastdrop.api.dto.session.request;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SessionCreateRequestDTO {

    @NotBlank
    private String title;

    private String expiresAt;

    @NotNull(message = "latitude can't be empty")
    private Double lat;

    @NotNull(message = "longitude can't be empty")
    private Double lng;

    @Positive
    private Integer radiusMeters;

    @NotNull
    private String hostId;

    private Boolean requiresCode = false;

}
