package cn.jesse.magicboxsample;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.data.PerformanceData;
import cn.jesse.magicbox.data.RequestLoggerData;
import cn.jesse.magicbox.util.MBLog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements MagicBox.OnDashboardDataListener {
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        // -- 初始化
        MagicBox.init(getApplication());
        MagicBox.getPerformanceManager().startMonitorFPS();
        MagicBox.getPerformanceManager().startMonitorCPU();
        MagicBox.getPerformanceManager().startMonitorMem();

        okHttpClient = MagicBox.getNetworkInfoManager().setSimulationEnable(true, okHttpClient);
//        MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_TIMEOUT);
//        MagicBox.getNetworkInfoManager().setSimulationTimeout(5000);

//        MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_BLOCK);

//        MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_SPEED_LIMIT);
//        MagicBox.getNetworkInfoManager().setSimulationRequestSpeed(100);

        okHttpClient = MagicBox.getNetworkInfoManager().setRequestLoggerEnable(true, okHttpClient);
        MagicBox.getNetworkInfoManager().setRequestLoggerHostWhiteList(new String[]{"www.github.com"});

        final Request request = new Request.Builder()
                .url("http://www.baidu.com?test=123")
                .get()
                .build();
        MBLog.d(TAG, "start");
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MBLog.d(TAG, "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MBLog.d(TAG, "onResponse " + response.code());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MagicBox.registerDashboardData(this);
        MagicBox.getDashboard().showDashboard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MagicBox.unregisterDashboardData(this);
        MagicBox.getDashboard().dismissDashboard();
    }

    @Override
    protected void onDestroy() {
        MagicBox.getPerformanceManager().stopMonitorFPS();
        MagicBox.getPerformanceManager().stopMonitorCPU();
        MagicBox.getPerformanceManager().stopMonitorMem();
        super.onDestroy();
    }

    @Override
    public void onPerformance(PerformanceData performanceData) {
        Log.d(TAG, performanceData.toString());
    }

    @Override
    public void onHttpRequestLog(RequestLoggerData loggerData) {
        Log.d(TAG, loggerData.toString());
    }
}
