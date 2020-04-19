package cn.jesse.magicbox.util;

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
}
