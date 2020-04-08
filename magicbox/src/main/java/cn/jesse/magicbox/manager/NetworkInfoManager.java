package cn.jesse.magicbox.manager;

import cn.jesse.magicbox.network.okhttp.interceptor.SimulateNetworkInterceptor;
import cn.jesse.magicbox.util.MBLog;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * 网络相关信息 管理器
 *
 * @author jesse
 */
public class NetworkInfoManager {
    private static final String TAG = "NetworkInfoManager";
    public static final int SIMULATION_TYPE_BLOCK = 100;
    public static final int SIMULATION_TYPE_TIMEOUT = 200;
    public static final int SIMULATION_TYPE_SPEED_LIMIT = 300;

    private static final int SIMULATION_DEFAULT_REQUEST_SPEED = 1;
    private static final int SIMULATION_DEFAULT_TIMEOUT_MILLIS = 2000;

    private static NetworkInfoManager instance;
    // 模拟网络
    private boolean simulationEnable = false;
    private int simulationType = -1;
    private int simulationTimeout = SIMULATION_DEFAULT_TIMEOUT_MILLIS;
    // request限速 k/s
    private int simulationRequestSpeed = SIMULATION_DEFAULT_REQUEST_SPEED;

    public static NetworkInfoManager getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (NetworkInfoManager.class) {
            if (instance == null) {
                instance = new NetworkInfoManager();
            }
        }

        return instance;
    }

    /**
     * 网络模拟是否有效
     *
     * @return bool
     */
    public boolean isSimulationEnable() {
        return simulationEnable;
    }

    /**
     * 设置开启网络模拟
     *
     * @param simulationEnable 是否开启
     * @param okHttpClient     client
     */
    public OkHttpClient setSimulationEnable(boolean simulationEnable, OkHttpClient okHttpClient) {
        this.simulationEnable = simulationEnable;

        if (okHttpClient == null) {
            MBLog.e(TAG, "setSimulationEnable http client is invalid");
            return null;
        }

        for (Interceptor interceptor : okHttpClient.networkInterceptors()) {
            if (interceptor instanceof SimulateNetworkInterceptor) {
                if (simulationEnable) {
                    break;
                }
            }
        }

        if (simulationEnable) {
            okHttpClient = okHttpClient.newBuilder().addNetworkInterceptor(new SimulateNetworkInterceptor()).build();
        }

        return okHttpClient;
    }

    /**
     * 获取网络模拟类型
     * SIMULATION_TYPE_BLOCK
     * SIMULATION_TYPE_TIMEOUT
     * SIMULATION_TYPE_SPEED_LIMIT
     *
     * @return type
     */
    public int getSimulationType() {
        return simulationType;
    }

    /**
     * 设置网络模拟类型
     * SIMULATION_TYPE_BLOCK
     * SIMULATION_TYPE_TIMEOUT
     * SIMULATION_TYPE_SPEED_LIMIT
     *
     * @param simulationType type
     */
    public void setSimulationType(int simulationType) {
        this.simulationType = simulationType;
    }

    public int getSimulationTimeout() {
        return simulationTimeout;
    }

    /**
     * 设置模拟超时时间
     *
     * @param simulationTimeout ms
     */
    public void setSimulationTimeout(int simulationTimeout) {
        this.simulationTimeout = simulationTimeout;
    }

    public int getSimulationRequestSpeed() {
        return simulationRequestSpeed;
    }

    /**
     * 设置请求限速
     *
     * @param simulationRequestSpeed k/s
     */
    public void setSimulationRequestSpeed(int simulationRequestSpeed) {
        this.simulationRequestSpeed = simulationRequestSpeed;
    }
}
