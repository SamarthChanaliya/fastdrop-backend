package com.fastdrop.api.dto.share.response;


public record ShareItemsRowDTO(
        String id,
        String shareId,
        String contentText,
        String fileName,
        String fileType,
        String filePath,
        String language,
        String createdAt,
        String itemType,
        String title
) { }
