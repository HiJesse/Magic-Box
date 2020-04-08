package cn.jesse.magicboxsample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.manager.NetworkInfoManager;
import cn.jesse.magicbox.util.MBLog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MagicBox.init(getApplication());
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        final Request request = new Request.Builder()
                .url("http://www.github.com")
                .get()
                .build();

        okHttpClient = MagicBox.getNetworkInfoManager().setSimulationEnable(true, okHttpClient);
//        MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_TIMEOUT);
//        MagicBox.getNetworkInfoManager().setSimulationTimeout(5000);

//        MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_BLOCK);

//        MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_SPEED_LIMIT);
//        MagicBox.getNetworkInfoManager().setSimulationRequestSpeed(100);

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
        MagicBox.getPerformanceManager().startMonitorFPS();
        MagicBox.getPerformanceManager().startMonitorCPU();
        MagicBox.getPerformanceManager().startMonitorMem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MagicBox.getPerformanceManager().stopMonitorFPS();
        MagicBox.getPerformanceManager().stopMonitorCPU();
        MagicBox.getPerformanceManager().stopMonitorMem();
    }
}
