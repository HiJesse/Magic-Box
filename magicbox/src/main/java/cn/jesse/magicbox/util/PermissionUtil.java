package cn.jesse.magicbox.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * 权限管理
 *
 * @author jesse
 */
public class PermissionUtil {

    private PermissionUtil() {
        // unused
    }

    /**
     * 检查当前是否有存储权限
     *
     * @param context context
     * @return bool
     */
    public static boolean checkStoragePermission(@NonNull Activity context) {
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 申请存储权限
     *
     * @param context     context
     * @param requestCode 回调code
     */
    public static void requestStoragePermisson(@NonNull Activity context, int requestCode) {
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
    }
}
