package cn.jesse.magicbox.util;

import android.app.Application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * app crash相关工具
 *
 * @author jesse
 */
public class CrashUtil {
    private static final String TAG = "CrashUtil";
    private static final String SUFFIX_CRASH = ".so";
    private static final String FORMAT_CRASH_DATE = "yyyyMMdd_HHmmss";


    private CrashUtil() {
        // unused
    }

    /**
     * 将java crash信息保存进墓碑文件里
     *
     * @param application app
     * @param e           异常
     */
    public static void saveJavaCrashInfo(Application application, Throwable e) {
        if (application == null || e == null) {
            return;
        }

        WorkspaceUtil.getInternalCrashDir(application);
        String time = new SimpleDateFormat(FORMAT_CRASH_DATE).format(new Date());
        File javaCrashFile = new File(WorkspaceUtil.getInternalCrashDir(application), "java_" + time + SUFFIX_CRASH);

        PrintWriter pw = null;
        try {
            //往文件中写入数据
            pw = new PrintWriter(new BufferedWriter(new FileWriter(javaCrashFile)));
            pw.println(time);
            pw.println(getDeviceAndAppInfo(application));
            e.printStackTrace(pw);
        } catch (Exception ex) {
            MBLog.e(TAG, "saveJavaCrashInfo " + ex.getMessage());
        } finally {
            FileUtil.close(pw);
        }
    }

    /**
     * 获取设备和app信息, crash文件的头部信息
     *
     * @param application app
     * @return info
     */
    public static String getDeviceAndAppInfo(Application application) {
        if (application == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        // 系统信息
        sb.append("Brand: ");
        sb.append(MBPlatformUtil.getBrand());
        sb.append("\n");

        sb.append("Model: ");
        sb.append(MBPlatformUtil.getModel());
        sb.append("\n");

        sb.append("OSVersion: ");
        sb.append(MBPlatformUtil.getOSVersion());
        sb.append("\n");

        sb.append("ABI: ");
        sb.append(MBPlatformUtil.getABI());
        sb.append("\n");

        sb.append("ROOT: ");
        sb.append(MBPlatformUtil.isRooted() ? "Y" : "N");
        sb.append("\n");

        sb.append("Screen: ");
        int[] displayMetrics = MBPlatformUtil.getDisplayMetrics(application);
        sb.append(displayMetrics[0]).append("*").append(displayMetrics[1]);
        sb.append("\n\n");

        //app 信息
        sb.append("AppVersion: ");
        sb.append(MBAppUtil.getVersionName(application));
        sb.append("\n");

        sb.append("AppCode: ");
        sb.append(MBAppUtil.getVersionCode(application));
        sb.append("\n");
        return sb.toString();
    }
}
