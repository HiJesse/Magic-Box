package cn.jesse.magicbox.manager;


import android.os.Handler;
import android.os.HandlerThread;

import cn.jesse.magicbox.factory.PerformanceJobFactory;
import cn.jesse.magicbox.job.IJob;

/**
 * 性能信息管理
 *
 * @author jesse
 */
public class PerformanceInfoManager {
    private static final String TAG = "PerformanceInfoManager";
    private static PerformanceInfoManager instance;

    private Handler handler;
    private HandlerThread handlerThread;

    // jobs
    private IJob fpsJob;
    private IJob cpuJob;
    private IJob memJob;

    public static PerformanceInfoManager getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (PerformanceInfoManager.class) {
            if (instance == null) {
                instance = new PerformanceInfoManager();
            }
        }

        return instance;
    }

    private PerformanceInfoManager() {
        handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        fpsJob = PerformanceJobFactory.generateJob(PerformanceJobFactory.TYPE_FPS);
        cpuJob = PerformanceJobFactory.generateJob(PerformanceJobFactory.TYPE_CPU);
        memJob = PerformanceJobFactory.generateJob(PerformanceJobFactory.TYPE_MEM);
    }

    /**
     * 获取性能数据处理线程
     *
     * @return handler
     */
    public Handler getPerformanceHandler() {
        return handler;
    }

    /**
     * 开始监控fps
     */
    public void startMonitorFPS() {
        if (fpsJob == null || fpsJob.isMonitorRunning()) {
            return;
        }

        fpsJob.startMonitor();
    }

    /**
     * 停止监控fps
     */
    public void stopMonitorFPS() {
        if (fpsJob == null) {
            return;
        }

        fpsJob.stopMonitor();
    }

    /**
     * 是否正在监控fps
     *
     * @return bool
     */
    public boolean isMonitoringFPS() {
        return fpsJob != null && fpsJob.isMonitorRunning();
    }

    /**
     * 开始监控cpu
     */
    public void startMonitorCPU() {
        if (cpuJob == null || cpuJob.isMonitorRunning()) {
            return;
        }

        cpuJob.startMonitor(handler);
    }

    /**
     * 停止监控cpu
     */
    public void stopMonitorCPU() {
        if (cpuJob == null) {
            return;
        }

        cpuJob.stopMonitor();
    }

    /**
     * 是否正在监控cpu
     *
     * @return bool
     */
    public boolean isMonitoringCPU() {
        return cpuJob != null && cpuJob.isMonitorRunning();
    }

    /**
     * 开始监控mem
     */
    public void startMonitorMem() {
        if (memJob == null || memJob.isMonitorRunning()) {
            return;
        }

        memJob.startMonitor(handler);
    }

    /**
     * 停止监控mem
     */
    public void stopMonitorMem() {
        if (memJob == null) {
            return;
        }

        memJob.stopMonitor();
    }

    /**
     * 是否正在监控mem
     *
     * @return bool
     */
    public boolean isMonitoringMem() {
        return memJob != null && memJob.isMonitorRunning();
    }
}
