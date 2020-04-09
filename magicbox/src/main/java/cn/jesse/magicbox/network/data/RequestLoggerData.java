package cn.jesse.magicbox.network.data;

import java.io.Serializable;

/**
 * 请求日志数据
 *
 * @author jesse
 */
public class RequestLoggerData implements Serializable {
    private String method;
    private int code;
    private String host;
    private String path;
    private String params;
    // 请求耗时 ms
    private long duration;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "{" +
                "method='" + method + ' ' +
                ", code=" + code +
                ", duration=" + duration +
                ", host='" + host + ' ' +
                ", path='" + path + ' ' +
                ", params='" + params + ' ' +
                '}';
    }
}