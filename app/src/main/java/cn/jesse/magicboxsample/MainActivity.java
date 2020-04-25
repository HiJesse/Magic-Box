package cn.jesse.magicboxsample;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.data.PerformanceData;
import cn.jesse.magicbox.data.RequestLoggerData;
import cn.jesse.magicbox.util.MBLog;
import cn.jesse.magicbox.view.activity.MagicBoxActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * sample页
 *
 * @author jesse
 */
public class MainActivity extends AppCompatActivity implements MagicBox.OnDashboardDataListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpClient okHttpClient = ignoreSSL(new OkHttpClient.Builder()).build();

        // --- 初始化
        MagicBox.init(getApplication(), true);
        // --- 开启性能监控
        MagicBox.getPerformanceManager().startMonitorFPS();
        MagicBox.getPerformanceManager().startMonitorCPU();
        MagicBox.getPerformanceManager().startMonitorMem();

        // --- 开启网络相关
        okHttpClient = MagicBox.getNetworkInfoManager().setSimulationEnable(true, okHttpClient);
//        MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_TIMEOUT);
//        MagicBox.getNetworkInfoManager().setSimulationTimeout(5000);

//        MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_BLOCK);

//        MagicBox.getNetworkInfoManager().setSimulationType(NetworkInfoManager.SIMULATION_TYPE_SPEED_LIMIT);
//        MagicBox.getNetworkInfoManager().setSimulationRequestSpeed(100);

        okHttpClient = MagicBox.getNetworkInfoManager().setRequestLoggerEnable(true, okHttpClient);
        MagicBox.getNetworkInfoManager().setRequestLoggerHostWhiteList(new String[]{"www.github.com"});

        // --- 注册数据回调, 并打开仪表盘
        MagicBox.registerDashboardData(this);
        MagicBox.getDashboard().showDashboard();

        final Request request = new Request.Builder()
                .url("http://www.baidu.com/s?wd=android+developer")
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

        MagicBoxActivity.start(this);
    }

    @Override
    protected void onDestroy() {
        MagicBox.getPerformanceManager().stopMonitorFPS();
        MagicBox.getPerformanceManager().stopMonitorCPU();
        MagicBox.getPerformanceManager().stopMonitorMem();
        MagicBox.unregisterDashboardData(this);
        MagicBox.getDashboard().dismissDashboard();
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

    private OkHttpClient.Builder ignoreSSL(@NonNull OkHttpClient.Builder builder) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // unused
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // unused
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
            }, new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory());
            builder.hostnameVerifier(new HostnameVerifier() {
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "error to set self HTTPS verify", ex);
        }
        return builder;
    }
}
