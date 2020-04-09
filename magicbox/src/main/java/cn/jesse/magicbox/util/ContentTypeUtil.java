package cn.jesse.magicbox.util;

import android.text.TextUtils;

/**
 * http content type类型解析
 *
 * @author jesse
 */
public class ContentTypeUtil {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String TYPE_IMAGE = "image/";
    public static final String TYPE_TEXT = "text/";
    public static final String TYPE_APPLICATION = "application/";
    public static final String TYPE_MULTIPART = "multipart/";
    public static final String TYPE_UNKNOWN = "unknown/";

    private ContentTypeUtil() {
        // unused
    }

    /**
     * 解析content type类型
     *
     * @param contentType type
     * @return type
     */
    public static String getContentType(String contentType) {
        if (TextUtils.isEmpty(contentType)) {
            return TYPE_UNKNOWN;
        }

        String type = TYPE_UNKNOWN;

        if (contentType.contains(TYPE_IMAGE)) {
            type = TYPE_IMAGE;
        } else if (contentType.contains(TYPE_TEXT)) {
            type = TYPE_TEXT;
        } else if (contentType.contains(TYPE_APPLICATION)) {
            type = TYPE_APPLICATION;
        } else if (contentType.contains(TYPE_MULTIPART)) {
            type = TYPE_MULTIPART;
        }

        return type;
    }

    /**
     * 是否为图片
     *
     * @param contentType type
     * @return bool
     */
    public static boolean isImage(String contentType) {
        return TYPE_IMAGE.equals(getContentType(contentType));
    }
}
