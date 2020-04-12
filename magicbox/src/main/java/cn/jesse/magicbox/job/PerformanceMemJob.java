package cn.jesse.magicbox.job;


import android.app.ActivityManager;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;

import java.text.DecimalFormat;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.manager.DashboardDataManager;
import cn.jesse.magicbox.util.MBLog;

/**
 * app memory监控job
 *
 * @author jesse
 */
public class PerformanceMemJob extends BaseJob {
    private static final String TAG = "PerformanceMemJob";
    private static final int DURATION_MEM = 500;

    private ActivityManager activityManager;
    private Handler handler;
    private DecimalFormat rateFormat = new DecimalFormat("#.00");

    public PerformanceMemJob() {
        activityManager = MagicBox.getActivityManager();
    }

    @Override
    public void run() {
        // 最近app 内存使用 MB
        float lastMemInfo = getAppMemory();
        lastMemInfo = Float.parseFloat(rateFormat.format(lastMemInfo));

        // 更新数据
        DashboardDataManager.getInstance().updateMemUsage(true, lastMemInfo);
        if (handler != null) {
            handler.postDelayed(this, DURATION_MEM);
        }
    }

    @Override
    public void startMonitor(Object... args) {
        if (args == null || args.length < 1 || !(args[0] instanceof Handler)) {
            MBLog.d(TAG, "startMonitor params is invalid");
            return;
        }
        super.startMonitor();
        DashboardDataManager.getInstance().updateMemUsage(true, 0);

        handler = (Handler) args[0];
        handler.postDelayed(this, DURATION_MEM);
    }

    @Override
    public void stopMonitor() {
        super.stopMonitor();
        if (handler != null) {
            handler.removeCallbacks(this);
            handler = null;
        }
        DashboardDataManager.getInstance().updateMemUsage(false, 0);
    }

    private float getAppMemory() {
        float mem = 0.0F;
        if (activityManager == null) {
            MBLog.e(TAG, "getAppMemory activity manager is invalid");
            return mem;
        }

        try {
            Debug.MemoryInfo memInfo = null;
            // 区分从Q开始获取内存信息的方式
            if (Build.VERSION.SDK_INT > 28) {
                memInfo = new Debug.MemoryInfo();
                Debug.getMemoryInfo(memInfo);
            } else {
                Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{android.os.Process.myPid()});
                if (processMemoryInfo != null && processMemoryInfo.length > 0) {
                    memInfo = processMemoryInfo[0];
                }
            }

            if (memInfo == null) {
                return mem;
            }

            int totalPss = memInfo.getTotalPss();

            // 转换MB
            if (totalPss >= 0) {
                mem = totalPss / 1024.0F;
            }
        } catch (Exception e) {
            MBLog.e(TAG, "getAppMemory " + e.getMessage());
        }
        return mem;
    }
}
