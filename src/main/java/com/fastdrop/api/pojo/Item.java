package com.fastdrop.api.pojo;

import jakarta.annotation.Nullable;

public record Item(
        @Nullable String content,
        @Nullable String fileName,
        @Nullable String fileType,
        @Nullable String filePath,
        @Nullable String language,
        @Nullable String itemType,
        @Nullable String title
) {

}
