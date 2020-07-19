package cn.jesse.magicbox.data;

/**
 * aop 统计方法耗时
 *
 * @author jesse
 */
public class AopTimeCosting {
    private String objectHashCode;
    private String objectClassName;
    private String methodName;
    private long timeCosting;
    // 过滤冗余数据用的时间戳
    private long redundantTimestamp;

    public AopTimeCosting() {
        // unused
    }

    public AopTimeCosting(String objectHashCode, String objectClassName, String methodName, long timeCosting) {
        this.objectHashCode = objectHashCode;
        this.objectClassName = objectClassName;
        this.methodName = methodName;
        this.timeCosting = timeCosting;
    }

    public long getRedundantTimestamp() {
        return redundantTimestamp;
    }

    public void setRedundantTimestamp(long redundantTimestamp) {
        this.redundantTimestamp = redundantTimestamp;
    }

    public String getObjectHashCode() {
        return objectHashCode;
    }

    public void setObjectHashCode(String objectHashCode) {
        this.objectHashCode = objectHashCode;
    }

    public String getObjectClassName() {
        return objectClassName;
    }

    public void setObjectClassName(String objectClassName) {
        this.objectClassName = objectClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getTimeCosting() {
        return timeCosting;
    }

    public void setTimeCosting(long timeCosting) {
        this.timeCosting = timeCosting;
    }

    @Override
    public String toString() {
        return "{" +
                "objectHashCode='" + objectHashCode + '\'' +
                ", objectClassName='" + objectClassName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", timeCosting=" + timeCosting +
                '}';
    }
}
