package com.fastdrop.api.dto.session.request;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class SessionCreateRequestDTO {

    @NotBlank
    private String title;

    private String expiresAt;

    @NotNull(message = "longitude can't be empty")
    private Double lng;

    @NotNull(message = "latitude can't be empty")
    private Double lat;


    @Positive
    private Integer radiusMeters;

    @NotNull
    private String hostId;

    private Boolean requiresCode = false;

    private Boolean sharingEnabled = true;

}
