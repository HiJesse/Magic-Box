package cn.jesse.magicbox.job;


import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

import cn.jesse.magicbox.util.MBLog;

/**
 * app cpu监控job
 *
 * @author jesse
 */
public class PerformanceCPUJob extends BaseJob {
    private static final String TAG = "PerformanceCPUJob";
    private static final int DURATION_CPU = 500;

    // 8.0一下系统获取cpu信息
    private Long lastCpuTime;
    private Long lastAppCpuTime;
    private RandomAccessFile appCPUStateFile;
    private RandomAccessFile systemCPUStateFile;

    // 最近app cpu使用率
    private float lastCPURate;
    private Handler handler;
    private DecimalFormat rateFormat = new DecimalFormat("#.00");

    @Override
    public void run() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lastCPURate = getCPURateAfterO();
        } else {
            lastCPURate = getCPURate();
        }

        lastCPURate = Float.parseFloat(rateFormat.format(lastCPURate));
        MBLog.d(TAG, "cpu: " + lastCPURate);
        if (handler != null) {
            handler.postDelayed(this, DURATION_CPU);
        }
    }

    @Override
    public void startMonitor(Object... args) {
        if (args == null || args.length < 1 || !(args[0] instanceof Handler)) {
            MBLog.d(TAG, "startMonitor params is invalid");
            return;
        }
        super.startMonitor();

        handler = (Handler) args[0];
        handler.postDelayed(this, DURATION_CPU);
    }

    @Override
    public void stopMonitor() {
        super.stopMonitor();
        handler = null;
    }

    public float getLastCPURate() {
        return lastCPURate;
    }

    /**
     * 8.0以上获取cpu的方式
     *
     * @return cpu rate
     */
    private float getCPURateAfterO() {
        Process process = null;
        try {
            // 取当前实时进程信息 一次
            process = Runtime.getRuntime().exec("top -n 1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int cpuIndex = -1;

            // 遍历每个进程信息
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (TextUtils.isEmpty(line)) {
                    continue;
                }

                // 匹配出cpu信息的位置
                int tempIndex = getCPUIndex(line);
                if (tempIndex != -1) {
                    cpuIndex = tempIndex;
                    continue;
                }

                // 处理当前app进程信息
                if (line.startsWith(String.valueOf(android.os.Process.myPid()))) {
                    if (cpuIndex == -1) {
                        continue;
                    }
                    String[] param = line.split("\\s+");
                    if (param.length <= cpuIndex) {
                        continue;
                    }
                    String cpu = param[cpuIndex];
                    if (cpu.endsWith("%")) {
                        cpu = cpu.substring(0, cpu.lastIndexOf("%"));
                    }
                    return Float.parseFloat(cpu) / Runtime.getRuntime().availableProcessors();
                }
            }
        } catch (IOException e) {
            MBLog.e(TAG, "getCPURateAfterO " + e.getMessage());
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return 0;
    }

    /**
     * 计算出cpu信息在每行信息中的第几位
     *
     * @param line 一行数据
     * @return index
     */
    private int getCPUIndex(@NonNull String line) {
        if (line.contains("CPU")) {
            String[] titles = line.split("\\s+");
            for (int i = 0; i < titles.length; i++) {
                if (titles[i].contains("CPU")) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 8.0一下获取proc信息获取当前app的cpu使用率
     *
     * @return cpu rate
     */
    private float getCPURate() {
        long cpuTime;
        long appTime;
        float rate = 0.0f;
        try {
            if (systemCPUStateFile == null || appCPUStateFile == null) {
                systemCPUStateFile = new RandomAccessFile("/proc/stat", "r");
                appCPUStateFile = new RandomAccessFile("/proc/" + android.os.Process.myPid() + "/stat", "r");
            } else {
                systemCPUStateFile.seek(0L);
                appCPUStateFile.seek(0L);
            }
            String systemCPUStr = systemCPUStateFile.readLine();
            String appCPUStr = appCPUStateFile.readLine();

            if (TextUtils.isEmpty(systemCPUStr) || TextUtils.isEmpty(appCPUStr)) {
                return rate;
            }

            // 切割两个proc的状态, 并拼接各个cpu时间
            String[] systemStatus = systemCPUStr.split(" ");
            String[] appStats = appCPUStr.split(" ");
            cpuTime = Long.parseLong(systemStatus[2]) + Long.parseLong(systemStatus[3])
                    + Long.parseLong(systemStatus[4]) + Long.parseLong(systemStatus[5])
                    + Long.parseLong(systemStatus[6]) + Long.parseLong(systemStatus[7])
                    + Long.parseLong(systemStatus[8]);
            appTime = Long.parseLong(appStats[13]) + Long.parseLong(appStats[14]);

            // 初始化cpu使用时间, 方便下次计算
            if (lastCpuTime == null && lastAppCpuTime == null) {
                lastCpuTime = cpuTime;
                lastAppCpuTime = appTime;
                return rate;
            }

            // 计算当前app cpu使用率. app cpu使用时间 / 系统总时间. %分制
            rate = ((float) (appTime - lastAppCpuTime) / (float) (cpuTime - lastCpuTime)) * 100f;
            lastCpuTime = cpuTime;
            lastAppCpuTime = appTime;
        } catch (Exception e) {
            MBLog.e(TAG, "getCPURate " + e.getMessage());
        }
        return rate;
    }
}
