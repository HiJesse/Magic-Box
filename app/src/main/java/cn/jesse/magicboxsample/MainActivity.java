package cn.jesse.magicboxsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.jesse.magicbox.MagicBox;
import cn.jesse.magicbox.data.MagicBoxDeviceAppInfoData;
import cn.jesse.magicbox.data.PerformanceData;
import cn.jesse.magicbox.data.RequestLoggerData;
import cn.jesse.magicbox.network.okhttp.interceptor.RequestLoggerInterceptor;
import cn.jesse.magicbox.network.okhttp.interceptor.SimulateNetworkInterceptor;
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
public class MainActivity extends AppCompatActivity implements MagicBox.OnDashboardDataListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_MAGIC_BOX = 2;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okHttpClient = ignoreSSL(new OkHttpClient.Builder()).build();

        findViewById(R.id.tv_magicbox).setOnClickListener(this);
        findViewById(R.id.tv_request).setOnClickListener(this);
        findViewById(R.id.tv_crash).setOnClickListener(this);

        // --- 初始化
        MagicBox.init(getApplication(), true);
        MagicBox.getJavaUncaughtCrashManager().enable(getApplication());
        okHttpClient = okHttpClient
                .newBuilder()
                .addNetworkInterceptor(new SimulateNetworkInterceptor())
                .addInterceptor(new RequestLoggerInterceptor())
                .build();

        // --- 注册数据回调, 并打开仪表盘
        MagicBox.registerDashboardData(this);

        // --- 设置扩展app信息
        List<MagicBoxDeviceAppInfoData> appData = new ArrayList<>();
        appData.add(new MagicBoxDeviceAppInfoData("渠道", "demo"));
        MagicBox.setAppInfoExternalData(appData);

    }

    @Override
    protected void onDestroy() {
        MagicBox.unregisterDashboardData(this);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_magicbox) {
            MagicBoxActivity.startActivityWithResult(this,
                    "修改标题",
                    REQUEST_MAGIC_BOX,
                    new String[]{"扩展1", "扩展2"});
            return;
        }

        if (v.getId() == R.id.tv_crash) {
            int i = 5;
            i /= 0;
            return;
        }

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
    }

    /**
     * 测试代码 忽略证书
     *
     * @param builder http builder
     * @return builder
     */
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

    private void toast(@NonNull String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REQUEST_MAGIC_BOX) {
            return;
        }

        // 以BASE_RESULT_CODE 基数 + 扩展下标
        if (resultCode == MagicBoxActivity.BASE_RESULT_CODE) {
            toast("扩展1");
        } else if (resultCode == MagicBoxActivity.BASE_RESULT_CODE + 1) {
            toast("扩展2");
        }
    }
}
