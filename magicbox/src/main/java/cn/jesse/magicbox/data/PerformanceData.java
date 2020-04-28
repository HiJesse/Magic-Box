package cn.jesse.magicbox.data;

import java.io.Serializable;

/**
 * 性能数据
 *
 * @author jesse
 */
public class PerformanceData implements Serializable {
    private boolean cpuMonitorEnable = false;
    private float currentCPUUsage;
    private boolean memMonitorEnable = false;
    private float currentMemUsage;
    private boolean fpsMonitorEnable = false;
    private int currentFPS;

    public PerformanceData() {
        // ignore
    }

    public PerformanceData(PerformanceData performanceData) {
        if (performanceData == null) {
            return;
        }

        cpuMonitorEnable = performanceData.cpuMonitorEnable;
        currentCPUUsage = performanceData.currentCPUUsage;
        memMonitorEnable = performanceData.memMonitorEnable;
        currentMemUsage = performanceData.currentMemUsage;
        fpsMonitorEnable = performanceData.fpsMonitorEnable;
        currentFPS = performanceData.currentFPS;
    }

    /**
     * 当前是否监听cpu
     *
     * @return bool
     */
    public boolean isCpuMonitorEnable() {
        return cpuMonitorEnable;
    }

    public void setCpuMonitorEnable(boolean cpuMonitorEnable) {
        this.cpuMonitorEnable = cpuMonitorEnable;
    }

    /**
     * 获取当前cpu使用率
     *
     * @return %
     */
    public float getCurrentCPUUsage() {
        return currentCPUUsage;
    }

    public void setCurrentCPUUsage(float currentCPURate) {
        this.currentCPUUsage = currentCPURate;
    }

    /**
     * 当前是否监听mem
     *
     * @return bool
     */
    public boolean isMemMonitorEnable() {
        return memMonitorEnable;
    }

    public void setMemMonitorEnable(boolean memMonitorEnable) {
        this.memMonitorEnable = memMonitorEnable;
    }

    /**
     * 获取当前内存占用
     *
     * @return MB
     */
    public float getCurrentMemUsage() {
        return currentMemUsage;
    }

    public void setCurrentMemUsage(float currentMemUsage) {
        this.currentMemUsage = currentMemUsage;
    }

    /**
     * 当前是否监听fps
     *
     * @return bool
     */
    public boolean isFpsMonitorEnable() {
        return fpsMonitorEnable;
    }

    public void setFpsMonitorEnable(boolean fpsMonitorEnable) {
        this.fpsMonitorEnable = fpsMonitorEnable;
    }

    /**
     * 获取当前帧率
     *
     * @return fps/s
     */
    public int getCurrentFPS() {
        return currentFPS;
    }

    public void setCurrentFPS(int currentFPS) {
        this.currentFPS = currentFPS;
    }

    @Override
    public String toString() {
        return "{" +
                "currentCPUUsage=" + currentCPUUsage +
                "%, currentMemUsage=" + currentMemUsage +
                "MB, currentFPS=" + currentFPS +
                "fps/s}";
    }
}
