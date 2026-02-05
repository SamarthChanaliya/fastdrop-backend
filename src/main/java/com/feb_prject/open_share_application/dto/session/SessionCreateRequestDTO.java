package com.feb_prject.open_share_application.dto.session;
import com.feb_prject.open_share_application.enums.Discoverability;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Instant;

@Data
public class SessionCreateRequestDTO {

    @NotBlank
    private String title;

    @Future
    private Instant expires_at;

    @NotNull(message = "latitude can't be empty")
    private Double lat;

    @NotNull(message = "longitude can't be empty")
    private Double lng;

    @Positive
    private Integer radius_meters;

    @NotNull
    private Discoverability discoverability;

    @NotNull
    private String host_id;


    private Boolean requires_join_code = false;


}
