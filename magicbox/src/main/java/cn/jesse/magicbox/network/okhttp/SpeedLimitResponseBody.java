package cn.jesse.magicbox.network.okhttp;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


/**
 * 限速response body. 在读内容时根据设置限制 speed kb/s
 *
 * @author jesse
 */
public class SpeedLimitResponseBody extends ResponseBody {
    private static String TAG = "SpeedLimitResponseBody";
    // k/s
    private long speedByte;
    private ResponseBody responseBody;
    private BufferedSource bufferedSource;

    public SpeedLimitResponseBody(long speed, @NonNull ResponseBody source) {
        this.responseBody = source;
        // kb -> byte
        this.speedByte = speed * 1024;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            private long cacheTotalBytesRead;
            private long cacheStartTime;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                if (cacheStartTime == 0) {
                    cacheStartTime = SystemClock.uptimeMillis();
                }

                //读1kb
                long bytesRead = super.read(sink.buffer(), 1024L);
                if (bytesRead == -1) {
                    return bytesRead;
                }

                // 累计byte 并计算当次读取所消耗的时间
                cacheTotalBytesRead = cacheTotalBytesRead + bytesRead;
                long costTime = SystemClock.uptimeMillis() - cacheStartTime;

                //如果找过1s 则不限速
                if (costTime > 1000L) {
                    return bytesRead;
                }

                // 1s内还没读到限速的容量, 返回继续读
                if (cacheTotalBytesRead < speedByte) {
                    return bytesRead;
                }

                // 写满限速, 并超过限速的速度, 则休眠
                long sleep = 1000L - costTime;
                SystemClock.sleep(sleep);

                //重置计算
                cacheStartTime = 0L;
                cacheTotalBytesRead = 0L;

                return bytesRead;
            }
        };
    }

}
