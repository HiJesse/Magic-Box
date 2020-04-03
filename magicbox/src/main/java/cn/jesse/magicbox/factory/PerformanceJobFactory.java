package cn.jesse.magicbox.factory;

import androidx.annotation.Nullable;

import cn.jesse.magicbox.job.IJob;
import cn.jesse.magicbox.job.PerformanceFPSJob;

/**
 * 性能检测job工厂
 *
 * @author jesse
 */
public class PerformanceJobFactory {
    public static final int TYPE_FPS = 1;

    /**
     * 根据类型创建不同的检测job
     *
     * @param flag 类型
     * @return Nullable job
     */
    public static @Nullable IJob generateJob(int flag) {
        IJob job = null;
        switch (flag) {
            case TYPE_FPS:
                job = new PerformanceFPSJob();
                break;
            default:
                // null
        }
        return job;
    }


}
