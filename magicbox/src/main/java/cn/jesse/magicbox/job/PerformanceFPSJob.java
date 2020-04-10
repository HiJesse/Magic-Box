package cn.jesse.magicbox.job;


import android.os.Handler;
import android.os.Looper;
import android.view.Choreographer;

import cn.jesse.magicbox.manager.DashboardDataManager;

/**
 * app 帧率监控job
 *
 * @author jesse
 */
public class PerformanceFPSJob extends BaseJob implements Choreographer.FrameCallback {
    private static final String TAG = "PerformanceFPSJob";
    private static final int DURATION_FPS = 1000;

    private int totalFramesPerSecond;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void doFrame(long l) {
        totalFramesPerSecond++;
        Choreographer.getInstance().postFrameCallback(this);
    }

    @Override
    public void run() {
        int lastFrameRate = totalFramesPerSecond;

        // 更新数据
        DashboardDataManager.getInstance().updateFPS(lastFrameRate);
        totalFramesPerSecond = 0;
        mainHandler.postDelayed(this, DURATION_FPS);
    }

    @Override
    public void startMonitor(Object... args) {
        super.startMonitor();
        Choreographer.getInstance().postFrameCallback(this);
        mainHandler.postDelayed(this, DURATION_FPS);
    }

    @Override
    public void stopMonitor() {
        super.stopMonitor();
        Choreographer.getInstance().removeFrameCallback(this);
        mainHandler.removeCallbacks(this);
    }
}
