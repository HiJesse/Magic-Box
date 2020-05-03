package cn.jesse.magicbox.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

/**
 * app信息工具
 *
 * @author jesse
 */
public class MBAppUtil {

    private MBAppUtil() {
        // unused
    }

    /**
     * 获取app 名称
     *
     * @param context context
     * @return name
     */
    public static String getAppName(@NonNull Context context) {
        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return context.getResources().getString(packageInfo.applicationInfo.labelRes);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取版本号
     *
     * @param context context
     * @return eg. 4.0.0
     */
    public static String getVersionName(@NonNull Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 获取内部版本号
     *
     * @param context context
     * @return eg. 120
     */
    public static int getVersionCode(@NonNull Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 获取应用包名
     *
     * @param context context
     * @return cn.xx.xxx
     */
    public static String getPackageName(@NonNull Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 获取项目app icon
     *
     * @param context context
     * @return bitmap
     */
    public static Bitmap getAppIcon(@NonNull Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();

        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            Drawable d = packageManager.getApplicationIcon(applicationInfo);
            BitmapDrawable bd = (BitmapDrawable) d;
            return bd.getBitmap();
        } catch (Exception e) {
            return null;
        }

    }

}
