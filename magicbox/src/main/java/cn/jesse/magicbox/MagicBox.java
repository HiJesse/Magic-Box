package cn.jesse.magicbox;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.jesse.magicbox.manager.NetworkInfoManager;
import cn.jesse.magicbox.manager.PerformanceInfoManager;

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

    /**
     * 获取性能管理器
     *
     * @return PerformanceInfoManager
     */
    public static PerformanceInfoManager getPerformanceManager() {
        return PerformanceInfoManager.getInstance();
    }

    /**
     * 获取网络管理器
     *
     * @return NetworkInfoManager
     */
    public static NetworkInfoManager getNetworkInfoManager() {
        return NetworkInfoManager.getInstance();
    }
}
