package cn.jesse.magicbox.util;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import cn.jesse.magicbox.MagicBox;

/**
 * 系统平台工具
 *
 * @author jesse
 */
public class MBPlatformUtil {

    private MBPlatformUtil() {
        // unused
    }

    /**
     * 判断当前运行是否为debug
     *
     * @param application context
     * @return bool
     */
    public static boolean isDebug(Application application) {
        if (application == null) {
            return false;
        }
        try {
            ApplicationInfo info = application.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * toast 信息
     *
     * @param msg 信息
     */
    public static void toast(String msg) {
        if (MagicBox.getApplication() == null || TextUtils.isEmpty(msg)) {
            return;
        }

        Toast.makeText(MagicBox.getApplication(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取app可用最大内存
     *
     * @return mb
     */
    public static float getAppTotalMem() {
        return Runtime.getRuntime().maxMemory() / 1024 / 1024;
    }

    /**
     * 获取品牌
     *
     * @return brand
     */
    public static String getBrand() {
        return Build.BRAND;
    }
}
