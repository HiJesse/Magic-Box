package cn.jesse.magicbox.manager;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.jesse.magicbox.data.AopTimeCosting;

/**
 * aop 管理器
 *
 * @author jesse
 */
public class AopManager implements Runnable {
    private static final String TAG = "AopManager";
    // 页面渲染3s超时足够了, 如果渲染超过3s那就过分了
    private static final int DURATION_CHECK_PAGE_RENDER = 3000;
    private static AopManager instance;
    // 用来过滤冗余数据
    private Map<String, AopTimeCosting> pageRenderCostingData;
    private Handler handler;
    private HandlerThread handlerThread;

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
    public synchronized void setAopEnable(boolean aopEnable) {
        this.aopEnable = aopEnable;
        if (pageRenderCostingData == null) {
            pageRenderCostingData = new ConcurrentHashMap<>();
            handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
            handler.postDelayed(this, DURATION_CHECK_PAGE_RENDER);
        }
    }

    /**
     * 过滤重复的渲染统计, 并将最终的结果更新给仪表盘数据管理
     *
     * @param costing 耗时信息
     */
    public void updatePageRenderCosting(final AopTimeCosting costing) {
        if (pageRenderCostingData == null || costing == null) {
            DashboardDataManager.getInstance().updatePageRenderCosting(costing);
            return;
        }

        synchronized (AopManager.class) {
            final String mapKey = costing.getObjectHashCode() + costing.getMethodName();
            costing.setRedundantTimestamp(System.currentTimeMillis());
            pageRenderCostingData.put(mapKey, costing);
        }
    }

    @Override
    public void run() {
        if (handler == null) {
            return;
        }

        if (pageRenderCostingData.isEmpty()) {
            handler.postDelayed(this, DURATION_CHECK_PAGE_RENDER);
            return;
        }

        synchronized (AopManager.class) {
            for (Map.Entry<String, AopTimeCosting> entry : pageRenderCostingData.entrySet()) {
                AopTimeCosting costing = entry.getValue();
                if (costing == null || System.currentTimeMillis() - costing.getRedundantTimestamp() < DURATION_CHECK_PAGE_RENDER) {
                    continue;
                }

                DashboardDataManager.getInstance().updatePageRenderCosting(costing);
                pageRenderCostingData.remove(entry.getKey());
            }
        }

        handler.postDelayed(this, DURATION_CHECK_PAGE_RENDER);
    }
}
