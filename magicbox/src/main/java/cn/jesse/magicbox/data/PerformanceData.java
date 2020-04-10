package cn.jesse.magicbox.data;

import java.io.Serializable;

/**
 * 性能数据
 *
 * @author jesse
 */
public class PerformanceData implements Serializable {
    private float currentCPUUsage;
    private float currentMemUsage;
    private int currentFPS;

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
