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

    public static void d(@NonNull String TAG, String msg) {
        Log.d(TAG_MAIN, TAG + ": " + msg);
    }

    public static void e(@NonNull String TAG, String msg) {
        Log.e(TAG_MAIN, TAG + ": " + msg);
    }
}
