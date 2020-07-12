package cn.jesse.magicbox.aop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * activity aop
 *
 * @author jesse
 */
@Aspect
public class ActivityAop {
    private static final String TAG = ActivityAop.class.getSimpleName();

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
        Log.d(TAG, joinPoint.getThis().hashCode() + ".onCreate duration: " + (System.currentTimeMillis() - beforeTime));
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
        Log.d(TAG, joinPoint.getThis().hashCode() + ".onDestroy duration: " + (System.currentTimeMillis() - beforeTime));
    }
}
