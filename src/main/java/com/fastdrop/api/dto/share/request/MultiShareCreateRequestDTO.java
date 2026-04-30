package com.fastdrop.api.dto.share.request;

import com.fastdrop.api.pojo.Item;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record MultiShareCreateRequestDTO(
        @NotBlank String sessionId,
        @NotBlank String title,
        @NotBlank String shareType,
        @NotBlank String createdBy,
        List<Item> items
        ) {
}
