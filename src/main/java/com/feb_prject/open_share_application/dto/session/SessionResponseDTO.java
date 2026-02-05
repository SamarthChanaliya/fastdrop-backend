package com.feb_prject.open_share_application.dto.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponseDTO { //Used for POST, PUT, PATCH
    private Long id;
    private String title;
    private OffsetDateTime expires_at;
    private OffsetDateTime created_at;
    private String host_id;
    private Integer radius_meters;
    private String discoverability;
    private Boolean is_active;
    private String join_code;
}
