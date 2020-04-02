package cn.jesse.magicbox.job;


import android.os.Handler;
import android.os.Looper;
import android.view.Choreographer;

import cn.jesse.magicbox.util.MBLog;

/**
 * app 帧率监控job
 *
 * @author jesse
 */
public class PerformanceFPSJob extends BaseJob implements Choreographer.FrameCallback {
    private static final String TAG = "PerformanceFPSJob";
    private static final int DURATION_FPS = 1000;

    private int totalFramesPerSecond;
    private static final int MAX_FRAME_RATE = 60;
    private int lastFrameRate = MAX_FRAME_RATE;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void doFrame(long l) {
        totalFramesPerSecond++;
        Choreographer.getInstance().postFrameCallback(this);
    }

    @Override
    public void run() {
        lastFrameRate = totalFramesPerSecond;

        MBLog.d(TAG, "fps: " + lastFrameRate);
        totalFramesPerSecond = 0;
        mainHandler.postDelayed(this, DURATION_FPS);
    }

    @Override
    public void startMonitor() {
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

    public int getLastFrameRate() {
        return lastFrameRate;
    }
}
