package cn.jesse.magicbox.view;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.R;
import cn.jesse.magicbox.data.PerformanceData;
import cn.jesse.magicbox.data.RequestLoggerData;

public class DashboardView implements MagicBox.OnDashboardDataListener {
    private View dashboardRootView;
    // 性能信息
    private View cpuRootView;
    private TextView cpuCurrentUsageView;

    private View memRootView;
    private TextView memCurrentUsageView;

    private View fpsRootView;
    private TextView fpsCurrentUsageView;

    // 网络拦截日志
    private ScrollView netLoggerScrollview;
    private TextView netLoggerTextView;

    public DashboardView(Application application) {
        LayoutInflater layoutInflater = LayoutInflater.from(application);
        dashboardRootView = layoutInflater.inflate(R.layout.dialog_dashboard, null);
        // 性能信息view
        cpuRootView = dashboardRootView.findViewById(R.id.ll_cpu_root);
        cpuCurrentUsageView = dashboardRootView.findViewById(R.id.tv_current_cpu);

        memRootView = dashboardRootView.findViewById(R.id.ll_mem_root);
        memCurrentUsageView = dashboardRootView.findViewById(R.id.tv_current_mem);

        fpsRootView = dashboardRootView.findViewById(R.id.ll_fps_root);
        fpsCurrentUsageView = dashboardRootView.findViewById(R.id.tv_current_fps);

        // 网络拦截日志
        netLoggerScrollview = dashboardRootView.findViewById(R.id.sv_net_logger);
        netLoggerTextView = dashboardRootView.findViewById(R.id.tv_net_logger);
        netLoggerScrollview.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    public View getDashboardRootView() {
        return dashboardRootView;
    }

    @Override
    public void onPerformance(PerformanceData performanceData) {
        if (performanceData.isCpuMonitorEnable()) {
            cpuRootView.setVisibility(View.VISIBLE);
            cpuCurrentUsageView.setText(String.format("%.2f%%", performanceData.getCurrentCPUUsage()));
        } else {
            cpuRootView.setVisibility(View.GONE);
        }

        if (performanceData.isMemMonitorEnable()) {
            memRootView.setVisibility(View.VISIBLE);
            memCurrentUsageView.setText(String.format("%.2f MB", performanceData.getCurrentMemUsage()));
        } else {
            memRootView.setVisibility(View.GONE);
        }

        if (performanceData.isFpsMonitorEnable()) {
            fpsRootView.setVisibility(View.VISIBLE);
            fpsCurrentUsageView.setText(String.format("%d FPS/s", performanceData.getCurrentFPS()));
        } else {
            fpsRootView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHttpRequestLog(RequestLoggerData loggerData) {
        if (!loggerData.isEnable()) {
            netLoggerScrollview.setVisibility(View.GONE);
            netLoggerTextView.setText("");
            return;
        }

        netLoggerScrollview.setVisibility(View.VISIBLE);
        netLoggerTextView.append("CODE: ");
        netLoggerTextView.append(String.valueOf(loggerData.getCode()));
        netLoggerTextView.append(" Duration: ");
        netLoggerTextView.append(loggerData.getDuration() + "ms");
        netLoggerTextView.append("  PATH: ");
        netLoggerTextView.append(loggerData.getPath());
        netLoggerTextView.append("\n");
        netLoggerScrollview.fullScroll(View.FOCUS_DOWN);
    }
}