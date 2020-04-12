package cn.jesse.magicbox.network.okhttp.interceptor;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.URLDecoder;

import cn.jesse.magicbox.data.RequestLoggerData;
import cn.jesse.magicbox.manager.DashboardDataManager;
import cn.jesse.magicbox.manager.NetworkInfoManager;
import cn.jesse.magicbox.util.ContentTypeUtil;
import cn.jesse.magicbox.util.MBLog;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 请求记录拦截器, 直接白名单
 *
 * @author jesse
 */
public class RequestLoggerInterceptor implements Interceptor {
    private static final String TAG = "RequestLoggerInterceptor";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(request);

        if (!NetworkInfoManager.getInstance().isRequestLoggerEnable()) {
            return response;
        }

        if (ignoreByWhiteList(request.url().host())) {
            return response;
        }

        if (ContentTypeUtil.isImage(request.header(ContentTypeUtil.CONTENT_TYPE))) {
            return response;
        }

        RequestLoggerData requestLoggerData = new RequestLoggerData();
        requestLoggerData.setMethod(request.method());
        requestLoggerData.setHost(request.url().host());
        requestLoggerData.setPath(request.url().encodedPath());

        if (METHOD_GET.equals(requestLoggerData.getMethod())) {
            parseGetParams(requestLoggerData, request.url().url().toString());
        } else if (METHOD_POST.equals(requestLoggerData.getMethod())) {
            parsePostParams(requestLoggerData, request.body());
        }

        requestLoggerData.setCode(response.code());
        requestLoggerData.setDuration(System.currentTimeMillis() - startTime);
        requestLoggerData.setEnable(true);
        // 更新数据
        DashboardDataManager.getInstance().updateNetworkRequestLog(requestLoggerData);

        return response;
    }

    /**
     * 白名单过滤
     *
     * @param host host
     * @return true 过滤
     */
    private boolean ignoreByWhiteList(String host) {
        String[] whiteList = NetworkInfoManager.getInstance().getRequestLoggerHostWhiteList();
        if (whiteList == null || whiteList.length == 0 || TextUtils.isEmpty(host)) {
            return false;
        }

        for (String whiteHost : whiteList) {
            if (host.contains(whiteHost)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 拆除get请求的参数
     *
     * @param data data
     * @param url  url
     */
    private void parseGetParams(@NonNull RequestLoggerData data, String url) {
        if (!METHOD_GET.equals(data.getMethod()) || TextUtils.isEmpty(url)) {
            return;
        }

        String params = url.replace("https://", "").replace("http://", "").replace("?", "");

        if (!TextUtils.isEmpty(data.getHost())) {
            params = params.replace(data.getHost(), "");
        }

        if (!TextUtils.isEmpty(data.getPath())) {
            params = params.replace(data.getPath(), "");
        }

        data.setParams(params);
    }

    /**
     * 解析post请求参数
     *
     * @param data        data
     * @param requestBody request body
     */
    private void parsePostParams(@NonNull RequestLoggerData data, RequestBody requestBody) {
        if (!METHOD_POST.equals(data.getMethod()) || requestBody == null) {
            return;
        }

        Buffer buffer = new Buffer();
        String params = null;
        try {
            requestBody.writeTo(buffer);
            params = buffer.readUtf8();
            params = URLDecoder.decode(params, "utf-8");
        } catch (IOException e) {
            MBLog.e(TAG, "parsePostParams " + e.getMessage());
        }

        data.setParams(params);

    }
}
