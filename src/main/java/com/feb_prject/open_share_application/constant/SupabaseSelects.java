package com.feb_prject.open_share_application.constant;

public final class SupabaseSelects {

    private SupabaseSelects() {}

    public static final String SESSION_DETAILS_SELECT =
            "id,created_at,discoverability,radius_meters,expires_at,host_id,location,title,is_active," +
                    "shares:shares (id,session_id,share_type,created_at," +
                    "share_items:share_items (id,share_id,item_type,content_text,file_path,language,file_type,created_at,file_name))";
}