package cn.jesse.magicbox.manager;

/**
 * aop 管理器
 *
 * @author jesse
 */
public class AopManager {
    private static final String TAG = "AopManager";
    private static AopManager instance;

    // 当前是否支持aop
    private boolean aopEnable;

    public static AopManager getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (AopManager.class) {
            if (instance == null) {
                instance = new AopManager();
            }
        }

        return instance;
    }

    private AopManager() {
        aopEnable = false;
    }

    /**
     * 当前aop是否可用
     *
     * @return enable
     */
    public boolean isAopEnable() {
        return aopEnable;
    }

    /**
     * 开关aop
     *
     * @param aopEnable enable
     */
    public void setAopEnable(boolean aopEnable) {
        this.aopEnable = aopEnable;
    }
}
