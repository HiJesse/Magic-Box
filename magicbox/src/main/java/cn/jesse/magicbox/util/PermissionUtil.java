package cn.jesse.magicbox.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;

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
        int permission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permission = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 申请存储权限
     *
     * @param context     context
     * @param requestCode 回调code
     */
    public static void requestStoragePermission(@NonNull Activity context, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
        }
    }
}
