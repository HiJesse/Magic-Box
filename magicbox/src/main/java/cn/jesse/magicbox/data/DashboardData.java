package cn.jesse.magicbox.data;

import java.io.Serializable;

/**
 * 所有汇总数据
 *
 * @author jesse
 */
public class DashboardData implements Serializable {
    private PerformanceData performanceData;
    private RequestLoggerData networkLoggerData;

    public DashboardData() {
        performanceData = new PerformanceData();
        networkLoggerData = new RequestLoggerData();
    }

    public PerformanceData getPerformanceData() {
        return performanceData;
    }

    public RequestLoggerData getNetworkLoggerData() {
        return networkLoggerData;
    }

    public void setNetworkLoggerData(RequestLoggerData networkLoggerData) {
        this.networkLoggerData = networkLoggerData;
    }
}
