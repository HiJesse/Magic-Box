package cn.jesse.magicbox.network.okhttp;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Sink;

/**
 * 限速request body. 在写内容时根据设置限制 speed kb/s
 *
 * @author jesse
 */
public class SpeedLimitRequestBody extends RequestBody {
    // k/s
    private long speedByte;
    private RequestBody requestBody;
    private BufferedSink bufferedSink;

    public SpeedLimitRequestBody(long speed, @NonNull RequestBody source) {
        this.requestBody = source;
        // kb -> byte
        this.speedByte = speed * 1024;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            // 将skin 转化为固定容量sink 1kb
            bufferedSink = new ByteCountBufferedSink(sink(sink), 1024L);
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.close();
    }

    private Sink sink(final BufferedSink sink) {
        return new ForwardingSink(sink) {
            private long cacheTotalBytesWritten;
            private long cacheStartTime;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                if (cacheStartTime == 0) {
                    cacheStartTime = SystemClock.uptimeMillis();
                }

                super.write(source, byteCount);
                cacheTotalBytesWritten += byteCount;

                long endTime = SystemClock.uptimeMillis() - cacheStartTime;

                // 1s内没写完1k, 不用限速
                if (endTime > 1000L) {
                    return;
                }

                // 1s内写还没写满限速, 返回继续写
                if (cacheTotalBytesWritten < speedByte) {
                    return;
                }

                // 超过限速的速度, 则休眠
                long sleep = 1000L - endTime;
                SystemClock.sleep(sleep);

                //重置计算
                cacheStartTime = 0L;
                cacheTotalBytesWritten = 0L;
            }
        };
    }

}
