package cn.jesse.magicbox;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * magic box 主入口
 *
 * @author jesse
 */
public class MagicBox {
    private static Application application;

    private MagicBox() {
        // unused
    }

    public static void init(@NonNull Application app) {
        application = app;
    }

    public static @Nullable
    Application getApplication() {
        return application;
    }

    public static @Nullable
    ActivityManager getActivityManager() {
        return application == null ? null : (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
    }
}
