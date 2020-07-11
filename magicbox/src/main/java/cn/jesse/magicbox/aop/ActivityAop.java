package cn.jesse.magicbox.aop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

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
     */
    @Pointcut("execution(* android.app.Activity.onCreate(..))")
    public void activityOnCreate() {
        // unused
    }


    @Around("activityOnCreate()")
    public void activityOnCreateTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        String targetClassName = joinPoint.getTarget().getClass().getName();
        String signatureName = joinPoint.getSignature().getName();
        long beforeTime = System.currentTimeMillis();
        joinPoint.proceed();
        Log.d(TAG, targetClassName + "." + signatureName + " duration: " + (System.currentTimeMillis() - beforeTime));
    }

    /**
     * onDestroy 切入点
     */
    @Pointcut("execution(* android.app.Activity.onDestroy())")
    public void activityOnDestroy() {
        // unused
    }


    @Around("activityOnDestroy()")
    public void activityOnDestroyTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        String targetClassName = joinPoint.getTarget().getClass().getName();
        String signatureName = joinPoint.getSignature().getName();
        long beforeTime = System.currentTimeMillis();
        joinPoint.proceed();
        Log.d(TAG, targetClassName + "." + signatureName + " duration: " + (System.currentTimeMillis() - beforeTime));
    }
}
