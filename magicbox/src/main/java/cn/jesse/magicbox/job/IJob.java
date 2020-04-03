package cn.jesse.magicbox.job;


/**
 * 任务基础接口
 *
 * @author jesse
 */
public interface IJob extends Runnable {

    /**
     * 启动job监控
     */
    void startMonitor(Object... args);

    /**
     * 关闭job监控
     */
    void stopMonitor();

    /**
     * 当前job是否正在检测
     *
     * @return bool
     */
    boolean isMonitorRunning();
}
