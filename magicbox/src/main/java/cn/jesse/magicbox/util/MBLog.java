package cn.jesse.magicbox.util;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * magic box log
 *
 * @author jesse
 */
public class MBLog {
    private static final String TAG_MAIN = "MagicBox";

    private static boolean enable = false;

    /**
     * 是否开启日志
     *
     * @param enabled bool
     */
    public static void setEnable(boolean enabled) {
        enable = enabled;
    }

    public static void i(@NonNull String TAG, String msg) {
        if (!enable) {
            return;
        }

        Log.i(TAG_MAIN, TAG + ": " + msg);
    }

    public static void d(@NonNull String TAG, String msg) {
        if (!enable) {
            return;
        }

        Log.d(TAG_MAIN, TAG + ": " + msg);
    }

    public static void e(@NonNull String TAG, String msg) {
        if (!enable) {
            return;
        }

        Log.e(TAG_MAIN, TAG + ": " + msg);
    }
}
