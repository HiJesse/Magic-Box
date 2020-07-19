package cn.jesse.magicbox.view;

import android.app.Application;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.R;
import cn.jesse.magicbox.data.AopTimeCosting;
import cn.jesse.magicbox.data.PerformanceData;
import cn.jesse.magicbox.data.RequestLoggerData;
import cn.jesse.magicbox.util.MBPlatformUtil;

public class DashboardView implements MagicBox.OnDashboardDataListener {
    private View dashboardRootView;
    // 性能信息
    private View cpuRootView;
    private TextView cpuCurrentUsageView;
    private PerformanceChartView cpuChartView;
    private float maxCPUUsage = 0;

    private View memRootView;
    private TextView memCurrentUsageView;
    private PerformanceChartView memChartView;

    private View fpsRootView;
    private TextView fpsCurrentUsageView;
    private PerformanceChartView fpsChartView;

    // 网络拦截日志
    private ScrollView netLoggerScrollview;
    private TextView netLoggerTextView;

    public DashboardView(Application application) {
        LayoutInflater layoutInflater = LayoutInflater.from(application);
        dashboardRootView = layoutInflater.inflate(R.layout.dialog_dashboard, null);
        // 性能信息view
        cpuRootView = dashboardRootView.findViewById(R.id.ll_cpu_root);
        cpuCurrentUsageView = dashboardRootView.findViewById(R.id.tv_current_cpu);
        cpuChartView = dashboardRootView.findViewById(R.id.cv_cpu);
        cpuChartView.setLineColor(R.color.dashboard_performance_cpu_value);

        memRootView = dashboardRootView.findViewById(R.id.ll_mem_root);
        memCurrentUsageView = dashboardRootView.findViewById(R.id.tv_current_mem);
        memChartView = dashboardRootView.findViewById(R.id.cv_mem);
        memChartView.setLineColor(R.color.dashboard_performance_mem_value);

        fpsRootView = dashboardRootView.findViewById(R.id.ll_fps_root);
        fpsCurrentUsageView = dashboardRootView.findViewById(R.id.tv_current_fps);
        fpsChartView = dashboardRootView.findViewById(R.id.cv_fps);
        fpsChartView.setLineColor(R.color.dashboard_performance_fps_value);

        // 网络拦截日志
        netLoggerScrollview = dashboardRootView.findViewById(R.id.sv_net_logger);
        netLoggerTextView = dashboardRootView.findViewById(R.id.tv_net_logger);
        netLoggerScrollview.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    /**
     * 获取根view
     *
     * @return view
     */
    public View getDashboardRootView() {
        return dashboardRootView;
    }

    /**
     * 清除仪表盘数据
     */
    public void clearDashboard() {
        if (dashboardRootView == null) {
            return;
        }

        cpuRootView.setVisibility(View.GONE);
        memRootView.setVisibility(View.GONE);
        fpsRootView.setVisibility(View.GONE);
        netLoggerTextView.setText("");
    }

    @Override
    public void onPerformance(PerformanceData performanceData) {
        if (performanceData.isCpuMonitorEnable()) {
            cpuRootView.setVisibility(View.VISIBLE);
            cpuCurrentUsageView.setText(String.format("%.2f%%", performanceData.getCurrentCPUUsage()));
            cpuChartView.setVisibility(View.VISIBLE);
            cpuChartView.updateCPUUsage(performanceData.getCurrentCPUUsage());
            parseReleaseCPUIssue(performanceData);
        } else {
            cpuRootView.setVisibility(View.GONE);
        }

        if (performanceData.isMemMonitorEnable()) {
            memRootView.setVisibility(View.VISIBLE);
            memCurrentUsageView.setText(String.format("%.2f MB", performanceData.getCurrentMemUsage()));
            memChartView.updateMemUsage(performanceData.getCurrentMemUsage());
        } else {
            memRootView.setVisibility(View.GONE);
        }

        if (performanceData.isFpsMonitorEnable()) {
            fpsRootView.setVisibility(View.VISIBLE);
            fpsCurrentUsageView.setText(String.format("%d FPS/s", performanceData.getCurrentFPS()));
            fpsChartView.updateFPS(performanceData.getCurrentFPS());
        } else {
            fpsRootView.setVisibility(View.GONE);
        }
    }

    /**
     * 兼容部分机型 release包 cpu 不能获取的问题
     *
     * @param performanceData 性能数据
     */
    private void parseReleaseCPUIssue(PerformanceData performanceData) {
        if (performanceData.getCurrentCPUUsage() > maxCPUUsage) {
            maxCPUUsage = performanceData.getCurrentCPUUsage();
        }

        // 如果历史cpu使用率大于0 则直接忽略
        if (maxCPUUsage > 0) {
            return;
        }

        // cpu关闭 或者值为非0 不处理
        if (!performanceData.isCpuMonitorEnable() || performanceData.getCurrentCPUUsage() != 0) {
            return;
        }

        // debug 或 sdk小于0 不处理
        if (MBPlatformUtil.isDebug(MagicBox.getApplication()) || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        cpuCurrentUsageView.setText(String.format("%s手机上请尝试使用debug包获取CPU信息", MBPlatformUtil.getBrand()));
        cpuChartView.setVisibility(View.GONE);
    }

    @Override
    public void onHttpRequestLog(RequestLoggerData loggerData) {
        if (!loggerData.isEnable()) {
            netLoggerScrollview.setVisibility(View.GONE);
            netLoggerTextView.setText("");
            return;
        }

        netLoggerScrollview.setVisibility(View.VISIBLE);
        netLoggerTextView.append("Code: ");
        netLoggerTextView.append(String.valueOf(loggerData.getCode()));
        netLoggerTextView.append(" Duration: ");
        netLoggerTextView.append(loggerData.getDuration() + "ms");
        netLoggerTextView.append("  Path: ");
        netLoggerTextView.append(loggerData.getPath());
        netLoggerTextView.append("\n");
        netLoggerScrollview.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onPageRenderCosting(AopTimeCosting costing) {
        netLoggerScrollview.setVisibility(View.VISIBLE);
        netLoggerTextView.append("Page: ");
        netLoggerTextView.append(costing.getObjectClassName());
        netLoggerTextView.append(" Hash: ");
        netLoggerTextView.append(costing.getObjectHashCode());
        netLoggerTextView.append(" Method: ");
        netLoggerTextView.append(costing.getMethodName());
        netLoggerTextView.append(" Duration: ");
        netLoggerTextView.append(costing.getTimeCosting() + "ms");
        netLoggerTextView.append("\n");
        netLoggerScrollview.fullScroll(View.FOCUS_DOWN);
    }
}
