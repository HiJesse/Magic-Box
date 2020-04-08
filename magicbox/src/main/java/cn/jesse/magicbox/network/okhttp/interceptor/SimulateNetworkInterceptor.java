package cn.jesse.magicbox.network.okhttp.interceptor;

import android.os.SystemClock;

import java.io.IOException;

import cn.jesse.magicbox.manager.NetworkInfoManager;
import cn.jesse.magicbox.network.okhttp.SpeedLimitRequestBody;
import cn.jesse.magicbox.network.okhttp.SpeedLimitResponseBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 模拟网络拦截器, 超时, 弱网, 断网
 *
 * @author jesse
 */
public class SimulateNetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkInfoManager.getInstance().isSimulationEnable()) {
            Request request = chain.request();
            return chain.proceed(request);
        }

        Response response;
        final int type = NetworkInfoManager.getInstance().getSimulationType();
        switch (type) {
            case NetworkInfoManager.SIMULATION_TYPE_BLOCK:
                response = simulateBlockNetwork(chain);
                break;
            case NetworkInfoManager.SIMULATION_TYPE_TIMEOUT:
                response = simulateTimeOut(chain);
                break;
            case NetworkInfoManager.SIMULATION_TYPE_SPEED_LIMIT:
                response = simulateSpeedLimit(chain);
                break;
            default:
                Request request = chain.request();
                response = chain.proceed(request);
        }

        return response;
    }

    /**
     * 模拟断网
     *
     * @param chain 请求链
     * @return response
     * @throws IOException io
     */
    private Response simulateBlockNetwork(Interceptor.Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        ResponseBody responseBody = ResponseBody.create(response.body().contentType(), "");
        return response.newBuilder()
                .code(404)
                .message(String.format("Unable to resolve host %s: No address associated with hostname", chain.request().url().host()))
                .body(responseBody)
                .build();
    }

    /**
     * 模拟超时
     *
     * @param chain 请求链
     * @return response
     * @throws IOException io
     */
    private Response simulateTimeOut(Interceptor.Chain chain) throws IOException {
        int timeout = NetworkInfoManager.getInstance().getSimulationTimeout();
        if (timeout <= 0) {
            Request request = chain.request();
            return chain.proceed(request);
        }
        SystemClock.sleep(timeout);
        final Response response = chain.proceed(chain.request());
        ResponseBody responseBody = ResponseBody.create(response.body().contentType(), "");
        return response.newBuilder()
                .code(400)
                .message(String.format("failed to connect to %s  after %dms", chain.request().url().host(), timeout))
                .body(responseBody)
                .build();
    }

    /**
     * 限制网速
     *
     * @param chain 请求链
     * @return response
     * @throws IOException io
     */
    private Response simulateSpeedLimit(Interceptor.Chain chain) throws IOException {
        int requestSpeed = NetworkInfoManager.getInstance().getSimulationRequestSpeed();

        Request request = chain.request();
        final RequestBody body = request.body();
        if (body != null) {
            final RequestBody requestBody = requestSpeed > 0 ? new SpeedLimitRequestBody(requestSpeed, body) : body;
            request = request.newBuilder().method(request.method(), requestBody).build();
        }
        final Response response = chain.proceed(request);

        final ResponseBody responseBody = response.body();
        final ResponseBody newResponseBody = requestSpeed > 0 ? new SpeedLimitResponseBody(requestSpeed, responseBody) : responseBody;
        return response.newBuilder().body(newResponseBody).build();
    }
}
