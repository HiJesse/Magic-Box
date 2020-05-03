package cn.jesse.magicbox.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;

import cn.jesse.magicbox.MagicBox;

/**
 * 系统平台工具
 *
 * @author jesse
 */
public class MBPlatformUtil {

    private MBPlatformUtil() {
        // unused
    }

    /**
     * 判断当前运行是否为debug
     *
     * @param application context
     * @return bool
     */
    public static boolean isDebug(Application application) {
        if (application == null) {
            return false;
        }
        try {
            ApplicationInfo info = application.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * toast 信息
     *
     * @param msg 信息
     */
    public static void toast(String msg) {
        if (MagicBox.getApplication() == null || TextUtils.isEmpty(msg)) {
            return;
        }

        Toast.makeText(MagicBox.getApplication(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取app可用最大内存
     *
     * @return mb
     */
    public static float getAppTotalMem() {
        return Runtime.getRuntime().maxMemory() / 1024 / 1024;
    }

    // ----- 基本设备信息

    /**
     * 获取品牌
     *
     * @return brand
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取型号
     *
     * @return model
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取系统版本号
     *
     * @return version
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取CPU 指令集
     *
     * @return abi
     */
    public static String getABI() {
        String[] abiArray;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abiArray = Build.SUPPORTED_ABIS;
        } else {
            abiArray = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }
        StringBuilder abiStr = new StringBuilder();
        for (String abi : abiArray) {
            abiStr.append(abi);
            abiStr.append(',');
        }
        return abiStr.toString();
    }

    /**
     * 获取设备屏幕分辨率
     *
     * @param context context
     * @return 720*1080
     */
    public static int[] getDisplayMetrics(@NonNull Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    // ----- 网络信息

    /**
     * 当前是否联网
     *
     * @param context context
     * @return bool
     */
    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        if (networkInfo != null && networkInfo.length > 0) {
            for (NetworkInfo networkInfo1 : networkInfo) {
                if (networkInfo1.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 当前是否已连接wifi
     *
     * @param context context
     * @return bool
     */
    public static boolean isWifiConnected(@NonNull Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return false;
        }
        NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取当前wifi名
     *
     * @param context context
     * @return ssid
     */
    public static String getSSID(@NonNull Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return "";
        }
        WifiInfo wifiInfo = wm.getConnectionInfo();
        if (wifiInfo != null) {
            String s = wifiInfo.getSSID();
            if (s.length() > 2 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
                return s.substring(1, s.length() - 1);
            }
        }
        return "";
    }

    /**
     * 获取wifi 信号和速度
     * <p>
     * int[0] 信号 [0, 100]
     * int[1] 网速 Mbps
     *
     * @param context context
     * @return []
     */
    public static int[] getWIFIRssiAndLinkSpeed(@NonNull Context context) {
        int[] data = new int[2];
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm == null) {
            return data;
        }
        WifiInfo info = wm.getConnectionInfo();
        if (info.getBSSID() != null) {
            data[0] = WifiManager.calculateSignalLevel(info.getRssi(), 100);
            data[1] = info.getLinkSpeed();
        }
        return data;
    }

    /**
     * 当前蜂窝是否可用
     *
     * @param context context
     * @return bool
     */
    public static boolean isMobileConnected(@NonNull Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return false;
        }
        NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取运营商信息 type
     * <p>
     * -1 无效
     * 0 未知
     * 1 移动
     * 2 联通
     * 3 电信
     *
     * @param context context
     * @return type
     */
    public static int getCellularOperatorType(@NonNull Context context) {
        int opeType = -1;
        // Mobile data disabled
        if (!isMobileConnected(context)) {
            return opeType;
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return opeType;
        }
        String operator = tm.getSimOperator();
        if ("46001".equals(operator) || "46006".equals(operator) || "46009".equals(operator)) {
            // 中国联通
            opeType = 2;
        } else if ("46000".equals(operator) || "46002".equals(operator) || "46004".equals(operator) || "46007".equals(operator)) {
            // 中国移动
            opeType = 1;
        } else if ("46003".equals(operator) || "46005".equals(operator) || "46011".equals(operator)) {
            // 中国电信
            opeType = 3;
        } else {
            opeType = 0;
        }
        return opeType;
    }

    /**
     * 获取运营商信息 文字
     *
     * @param context context
     * @return str
     */
    public static String getCellularOperatorTypeString(@NonNull Context context) {
        String typeString = "无效";
        switch (getCellularOperatorType(context)) {
            case -1:
                break;
            case 1:
                typeString = "移动";
                break;
            case 2:
                typeString = "联通";
                break;
            case 3:
                typeString = "电信";
                break;
            case 0:
            default:
                typeString = "未知";
        }
        return typeString;
    }
}
