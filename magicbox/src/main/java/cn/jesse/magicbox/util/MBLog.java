package cn.jesse.magicbox.util;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * magic box log
 *
 * @author jesse
 */
public class MBLog {

    public static void d(@NonNull String TAG, String msg) {
        Log.d(TAG, msg);
    }
}
