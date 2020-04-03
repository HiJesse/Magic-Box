package cn.jesse.magicbox.manager;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

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
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    // jobs
    private IJob fpsJob;

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
        handler = new Handler(handlerThread.getLooper()) {

        };

        fpsJob = PerformanceJobFactory.generateJob(PerformanceJobFactory.TYPE_FPS);
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
}
