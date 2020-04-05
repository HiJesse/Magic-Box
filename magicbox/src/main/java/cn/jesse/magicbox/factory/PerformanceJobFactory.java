package cn.jesse.magicbox.factory;

import androidx.annotation.Nullable;

import cn.jesse.magicbox.job.IJob;
import cn.jesse.magicbox.job.PerformanceCPUJob;
import cn.jesse.magicbox.job.PerformanceFPSJob;
import cn.jesse.magicbox.job.PerformanceMemJob;

/**
 * 性能检测job工厂
 *
 * @author jesse
 */
public class PerformanceJobFactory {
    public static final int TYPE_FPS = 1;
    public static final int TYPE_CPU = 2;
    public static final int TYPE_MEM = 3;

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
            case TYPE_CPU:
                job = new PerformanceCPUJob();
                break;
            case TYPE_MEM:
                job = new PerformanceMemJob();
                break;
            default:
                // null
        }
        return job;
    }


}
