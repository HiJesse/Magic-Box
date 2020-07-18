package cn.jesse.magicbox.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import cn.jesse.magicbox.data.AopTimeCosting;
import cn.jesse.magicbox.manager.DashboardDataManager;

/**
 * activity aop
 *
 * @author jesse
 */
@Aspect
public class ActivityAop {

    /**
     * onCreate 切入点
     *
     * @param joinPoint 切入点
     * @throws Throwable e
     */
    @Around("execution(void android.app.Activity.onCreate(android.os.Bundle))")
    public void activityOnCreateTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        long beforeTime = System.currentTimeMillis();
        joinPoint.proceed();
        DashboardDataManager.getInstance().updatePageRenderCosting(new AopTimeCosting(
                String.valueOf(joinPoint.getThis().hashCode()),
                joinPoint.getTarget().getClass().getSimpleName(),
                "onCreate",
                System.currentTimeMillis() - beforeTime
        ));
    }

    /**
     * onDestroy 切入点
     *
     * @param joinPoint 切入点
     * @throws Throwable e
     */
    @Around("execution(void android.app.Activity.onDestroy())")
    public void activityOnDestroyTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        long beforeTime = System.currentTimeMillis();
        joinPoint.proceed();
        DashboardDataManager.getInstance().updatePageRenderCosting(new AopTimeCosting(
                String.valueOf(joinPoint.getThis().hashCode()),
                joinPoint.getTarget().getClass().getSimpleName(),
                "onDestroy",
                System.currentTimeMillis() - beforeTime
        ));
    }
}
