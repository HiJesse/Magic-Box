package cn.jesse.magicbox.manager;

import android.app.Application;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import cn.jesse.magicbox.R;
import cn.jesse.magicbox.util.MBLog;

import static android.content.Context.WINDOW_SERVICE;

/**
 * 仪表盘 系统弹窗
 *
 * @author jesse
 */
public class DashboardViewManager {
    private static final String TAG = "DashboardViewManager";
    private static DashboardViewManager instance;

    private Application application;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View dashboardRootView;

    private boolean showing = false;

    public static DashboardViewManager getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (DashboardViewManager.class) {
            if (instance == null) {
                instance = new DashboardViewManager();
            }
        }

        return instance;
    }

    /**
     * 使用application初始化, 避免内存泄露
     *
     * @param application application
     */
    public void init(@NonNull Application application) {
        this.application = application;
    }

    /**
     * 显示仪表盘弹窗
     */
    public synchronized void showDashboard() {
        if (application == null) {
            MBLog.e(TAG, "showDashboard should init with valid application");
            return;
        }

        if (isShowing()) {
            MBLog.d(TAG, "dialog is showing");
            return;
        }

        initDashboard();

        showing = true;
        windowManager.addView(dashboardRootView, layoutParams);
    }

    /**
     * 隐藏仪表盘弹窗
     */
    public synchronized void dismissDashboard() {
        if (application == null || windowManager == null) {
            MBLog.e(TAG, "dismissDashboard should init with valid application");
            return;
        }

        if (!isShowing()) {
            MBLog.e(TAG, "dialog is not showing");
            return;
        }

        windowManager.removeViewImmediate(dashboardRootView);
        showing = false;
    }

    /**
     * 当前仪表盘是否正在显示
     *
     * @return bool
     */
    public boolean isShowing() {
        return showing;
    }

    private void initDashboard() {
        if (windowManager != null) {
            return;
        }

        windowManager = (WindowManager) application.getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);

        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = outMetrics.widthPixels;
        layoutParams.height = outMetrics.heightPixels;
        layoutParams.x = 0;
        layoutParams.y = 0;

        if (dashboardRootView != null) {
            return;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(application);
        dashboardRootView = layoutInflater.inflate(R.layout.dialog_dashboard, null);
    }
}
