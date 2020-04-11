package cn.jesse.magicbox;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.jesse.magicbox.data.PerformanceData;
import cn.jesse.magicbox.data.RequestLoggerData;
import cn.jesse.magicbox.manager.DashboardDataManager;
import cn.jesse.magicbox.manager.DashboardViewManager;
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
        DashboardViewManager.getInstance().init(app);
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

    /**
     * 注册仪表盘数据回调
     *
     * @param listener listener
     */
    public static void registerDashboardData(OnDashboardDataListener listener) {
        DashboardDataManager.getInstance().register(listener);
    }

    /**
     * 注销仪表盘数据回调
     *
     * @param listener listener
     */
    public static void unregisterDashboardData(OnDashboardDataListener listener) {
        DashboardDataManager.getInstance().unRegister(listener);
    }

    /**
     * 返回仪表盘 弹窗
     *
     * @return dashboard dialog
     */
    public static DashboardViewManager getDashboard() {
        return DashboardViewManager.getInstance();
    }

    /**
     * Magic Box所有数据监听. UI 线程回调.
     */
    public interface OnDashboardDataListener {
        /**
         * 性能相关数据回调
         *
         * @param performanceData 性能
         */
        void onPerformance(PerformanceData performanceData);

        /**
         * 网络请求拦截日志回调
         *
         * @param loggerData 拦截日志
         */
        void onHttpRequestLog(RequestLoggerData loggerData);
    }
}
