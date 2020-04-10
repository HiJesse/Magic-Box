package cn.jesse.magicbox.manager;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.data.DashboardData;
import cn.jesse.magicbox.data.RequestLoggerData;

/**
 * Magic Box所有数据更新管理器
 *
 * @author jesse
 */
public class DashboardDataManager {
    private static final String TAG = "DashboardDataManager";
    private static final long DURATION_UPDATE_PERFORMANCE = 500;

    private static DashboardDataManager instance;

    private Handler handler;
    private DashboardData dashboardData;
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

    public void updateCPUUsage(float cpuUsage) {
        dashboardData.getPerformanceData().setCurrentCPUUsage(cpuUsage);
        updatePerformance();
    }

    public void updateMemUsage(float memUsage) {
        dashboardData.getPerformanceData().setCurrentMemUsage(memUsage);
        updatePerformance();
    }

    public void updateFPS(int fps) {
        dashboardData.getPerformanceData().setCurrentFPS(fps);
        updatePerformance();
    }

    public void updateNetworkRequestLog(RequestLoggerData data) {
        dashboardData.setNetworkLoggerData(data);

        handler.post(new Runnable() {
            @Override
            public void run() {
                for (MagicBox.OnDashboardDataListener holdListener : dashboardDataListeners) {
                    holdListener.onPerformance(dashboardData.getPerformanceData());
                }
            }
        });

        postToMainThread(new Runnable() {
            @Override
            public void run() {
                for (MagicBox.OnDashboardDataListener holdListener : dashboardDataListeners) {
                    holdListener.onHttpRequestLog(dashboardData.getNetworkLoggerData());
                }
            }
        });
    }

    /**
     * 以DURATION_UPDATE_PERFORMANCE为最大频率更新性能数据
     */
    private void updatePerformance() {
        if (System.currentTimeMillis() - performanceUpdateTime < DURATION_UPDATE_PERFORMANCE) {
            return;
        }
        performanceUpdateTime = System.currentTimeMillis();

        postToMainThread(new Runnable() {
            @Override
            public void run() {
                for (MagicBox.OnDashboardDataListener holdListener : dashboardDataListeners) {
                    holdListener.onPerformance(dashboardData.getPerformanceData());
                }
            }
        });
    }

    private void postToMainThread(@NonNull Runnable runnable) {
        handler.post(runnable);
    }
}
