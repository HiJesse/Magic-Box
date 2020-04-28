package cn.jesse.magicbox.manager;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.data.DashboardData;
import cn.jesse.magicbox.data.PerformanceData;
import cn.jesse.magicbox.data.RequestLoggerData;
import cn.jesse.magicbox.util.MBLog;

/**
 * Magic Box所有数据更新管理器
 *
 * @author jesse
 */
public class DashboardDataManager {
    private static final String TAG = "DashboardDataManager";
    private static final long DURATION_UPDATE_PERFORMANCE = 700;

    private static DashboardDataManager instance;

    private Handler handler;
    private DashboardData dashboardData;
    private PerformanceData performanceDataClone;
    private List<MagicBox.OnDashboardDataListener> dashboardDataListeners = new ArrayList<>();

    private long performanceUpdateTime;

    private DashboardDataManager() {
        dashboardData = new DashboardData();
        handler = new Handler(Looper.getMainLooper());
    }

    public static DashboardDataManager getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (DashboardDataManager.class) {
            if (instance == null) {
                instance = new DashboardDataManager();
            }
        }

        return instance;
    }

    /**
     * 向仪表盘注册监听
     *
     * @param listener listener
     */
    public synchronized void register(MagicBox.OnDashboardDataListener listener) {
        for (MagicBox.OnDashboardDataListener holdListener : dashboardDataListeners) {
            if (holdListener == listener) {
                return;
            }
        }

        dashboardDataListeners.add(listener);
    }

    /**
     * 注销仪表盘监听
     *
     * @param listener listener
     */
    public synchronized void unRegister(MagicBox.OnDashboardDataListener listener) {
        dashboardDataListeners.remove(listener);
    }

    /**
     * 更新cpu信息
     *
     * @param enable   是否正在监听
     * @param cpuUsage 使用情况
     */
    public void updateCPUUsage(boolean enable, float cpuUsage) {
        MBLog.i(TAG, "cpu " + cpuUsage);
        dashboardData.getPerformanceData().setCpuMonitorEnable(enable);
        dashboardData.getPerformanceData().setCurrentCPUUsage(cpuUsage);
        updatePerformance();
    }

    /**
     * 更新mem信息
     *
     * @param enable   是否正在监听
     * @param memUsage 使用情况
     */
    public void updateMemUsage(boolean enable, float memUsage) {
        MBLog.i(TAG, "mem " + memUsage);
        dashboardData.getPerformanceData().setMemMonitorEnable(enable);
        dashboardData.getPerformanceData().setCurrentMemUsage(memUsage);
        updatePerformance();
    }

    /**
     * 更新fps信息
     *
     * @param enable 是否正在监听
     * @param fps    帧率
     */
    public void updateFPS(boolean enable, int fps) {
        MBLog.i(TAG, "fps " + fps);
        dashboardData.getPerformanceData().setFpsMonitorEnable(enable);
        dashboardData.getPerformanceData().setCurrentFPS(fps);
        updatePerformance();
    }

    /**
     * 更新网络拦截日志信息
     *
     * @param data 日志
     */
    public void updateNetworkRequestLog(final RequestLoggerData data) {
        MBLog.i(TAG, "net log " + data == null ? "" : data.toString());
        dashboardData.setNetworkLoggerData(data);

        postToMainThread(new Runnable() {
            @Override
            public void run() {
                for (MagicBox.OnDashboardDataListener holdListener : dashboardDataListeners) {
                    holdListener.onHttpRequestLog(data);
                }
            }
        });
    }

    /**
     * 以DURATION_UPDATE_PERFORMANCE为最大频率更新性能数据, 如果有性能开关信息更新 则直接刷新
     */
    private void updatePerformance() {
        if (System.currentTimeMillis() - performanceUpdateTime < DURATION_UPDATE_PERFORMANCE && !isPerformanceOptionChanged()) {
            return;
        }
        performanceUpdateTime = System.currentTimeMillis();
        performanceDataClone = new PerformanceData(dashboardData.getPerformanceData());

        postToMainThread(new Runnable() {
            @Override
            public void run() {
                for (MagicBox.OnDashboardDataListener holdListener : dashboardDataListeners) {
                    holdListener.onPerformance(dashboardData.getPerformanceData());
                }
            }
        });
    }

    /**
     * 判断当次更新 是否有性能开关信息变更
     *
     * @return bool
     */
    private boolean isPerformanceOptionChanged() {
        if (performanceDataClone == null) {
            return true;
        }

        PerformanceData originPerformanceData = dashboardData.getPerformanceData();

        return performanceDataClone.isCpuMonitorEnable() != originPerformanceData.isCpuMonitorEnable() ||
                performanceDataClone.isMemMonitorEnable() != originPerformanceData.isMemMonitorEnable() ||
                performanceDataClone.isFpsMonitorEnable() != originPerformanceData.isFpsMonitorEnable();
    }

    private void postToMainThread(@NonNull Runnable runnable) {
        handler.post(runnable);
    }
}
