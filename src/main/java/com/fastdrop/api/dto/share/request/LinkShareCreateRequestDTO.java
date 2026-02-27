package com.fastdrop.api.dto.share.request;

import jakarta.validation.constraints.NotBlank;

public record LinkShareCreateRequestDTO(
        @NotBlank String sessionId,
        @NotBlank String title,
        @NotBlank String shareType,
        @NotBlank String createdBy,
        @NotBlank String contentText
) {
}
