package cn.jesse.magicbox.manager;


import android.app.Application;

import androidx.annotation.NonNull;

import cn.jesse.magicbox.util.CrashUtil;
import cn.jesse.magicbox.util.MBLog;

/**
 * java 异常处理
 * <p>
 * 未捕获的异常写入到 /data/data/package_name/xxx下
 * <p>
 * 文件格式 java_date_time.so
 * eg. java_20200501_121122.so
 *
 * @author jesse
 */
public class JavaUncaughtCrashManager implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "JavaUncaughtCrashManager";
    private static JavaUncaughtCrashManager sInstance = new JavaUncaughtCrashManager();
    private Thread.UncaughtExceptionHandler originalUncaughtHandler;
    private Application application;

    private JavaUncaughtCrashManager() {
        // unused
    }

    public static JavaUncaughtCrashManager getInstance() {
        return sInstance;
    }

    /**
     * 开启java crash拦截
     *
     * @param application application
     */
    public void enable(@NonNull Application application) {
        originalUncaughtHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.application = application;
    }

    /**
     * 关闭java crash拦截
     */
    public void disable() {
        if (originalUncaughtHandler != null) {
            Thread.setDefaultUncaughtExceptionHandler(originalUncaughtHandler);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            CrashUtil.saveJavaCrashInfo(application, e);
        } catch (Exception additionalEx) {
            // 保险起见 catch下
            MBLog.e(TAG, additionalEx.getMessage());
        }

        // 将异常再还给系统继续处理
        if (originalUncaughtHandler != null) {
            originalUncaughtHandler.uncaughtException(t, e);
        }
    }
}
