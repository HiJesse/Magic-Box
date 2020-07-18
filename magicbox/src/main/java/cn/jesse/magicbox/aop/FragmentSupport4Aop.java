package cn.jesse.magicbox.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import cn.jesse.magicbox.data.AopTimeCosting;
import cn.jesse.magicbox.manager.AopManager;
import cn.jesse.magicbox.manager.DashboardDataManager;

/**
 * android.support.v4.app.fragment aop
 *
 * @author jesse
 */
@Aspect
public class FragmentSupport4Aop {
    private static final String TAG = FragmentSupport4Aop.class.getSimpleName();

    /**
     * onCreateView 切点
     *
     * @param joinPoint 切点
     * @throws Throwable e
     */
    @Around("execution(android.view.View android.support.v4.app.Fragment.onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle))")
    public Object fragmentOnViewCreateTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!AopManager.getInstance().isAopEnable()) {
            return joinPoint.proceed();
        }
        long beforeTime = System.currentTimeMillis();
        Object returnData = joinPoint.proceed();
        DashboardDataManager.getInstance().updatePageRenderCosting(new AopTimeCosting(
                String.valueOf(joinPoint.getThis().hashCode()),
                joinPoint.getTarget().getClass().getSimpleName(),
                "onCreateView",
                System.currentTimeMillis() - beforeTime
        ));
        return returnData;
    }

    /**
     * onViewCreated 切点
     *
     * @param joinPoint 切点
     * @throws Throwable e
     */
    @Around("execution(void android.support.v4.app.Fragment.onViewCreated(android.view.View, android.os.Bundle))")
    public void fragmentOnViewCreatedTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!AopManager.getInstance().isAopEnable()) {
            joinPoint.proceed();
            return;
        }
        long beforeTime = System.currentTimeMillis();
        joinPoint.proceed();
        DashboardDataManager.getInstance().updatePageRenderCosting(new AopTimeCosting(
                String.valueOf(joinPoint.getThis().hashCode()),
                joinPoint.getTarget().getClass().getSimpleName(),
                "onViewCreated",
                System.currentTimeMillis() - beforeTime
        ));
    }

    /**
     * onDestroyView 切点
     *
     * @param joinPoint 切点
     * @throws Throwable e
     */
    @Around("execution(void android.support.v4.app.Fragment.onDestroyView())")
    public void fragmentOnViewDestroyTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!AopManager.getInstance().isAopEnable()) {
            joinPoint.proceed();
            return;
        }
        long beforeTime = System.currentTimeMillis();
        joinPoint.proceed();
        DashboardDataManager.getInstance().updatePageRenderCosting(new AopTimeCosting(
                String.valueOf(joinPoint.getThis().hashCode()),
                joinPoint.getTarget().getClass().getSimpleName(),
                "onDestroyView",
                System.currentTimeMillis() - beforeTime
        ));
    }
}
