package cn.jesse.magicbox.job;

/**
 * job基类
 *
 * @author jesse
 */
public abstract class BaseJob implements IJob {
    protected boolean isMonitorRunning = false;

    @Override
    public void startMonitor() {
        isMonitorRunning = true;
    }

    @Override
    public void stopMonitor() {
        isMonitorRunning = false;
    }

    @Override
    public boolean isMonitorRunning() {
        return isMonitorRunning;
    }
}
