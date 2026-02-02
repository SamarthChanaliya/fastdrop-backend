package com.feb_prject.open_share_application.constants;

public final class SupabaseSelects {

    private SupabaseSelects() {}

    public static final String NEARBY_SHARES =
            "id,room_id,share_type,created_at,expires_at,created_by,location,title," +
                    "share_items:share_items(" +
                    "id,share_id,item_type,content_text,file_path,language,file_type,created_at,file_name" +
                    ")";
}
