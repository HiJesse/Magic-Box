package cn.jesse.magicbox.aop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * android.app.fragment aop
 *
 * @author jesse
 */
@Aspect
public class FragmentAop {
    private static final String TAG = FragmentAop.class.getSimpleName();

    /**
     * onCreateView 切点
     *
     * @param joinPoint 切点
     * @throws Throwable e
     */
    @Around("execution(android.view.View android.app.Fragment.onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle))")
    public Object fragmentOnViewCreateTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        long beforeTime = System.currentTimeMillis();
        Object returnData = joinPoint.proceed();
        Log.d(TAG, joinPoint.getThis().hashCode() + ".onCreateView duration: " + (System.currentTimeMillis() - beforeTime));
        return returnData;
    }

    /**
     * onViewCreated 切点
     *
     * @param joinPoint 切点
     * @throws Throwable e
     */
    @Around("execution(void android.app.Fragment.onViewCreated(android.view.View, android.os.Bundle))")
    public void fragmentOnViewCreatedTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        long beforeTime = System.currentTimeMillis();
        joinPoint.proceed();
        Log.d(TAG, joinPoint.getThis().hashCode() + ".onViewCreated duration: " + (System.currentTimeMillis() - beforeTime));
    }

    /**
     * onDestroyView 切点
     *
     * @param joinPoint 切点
     * @throws Throwable e
     */
    @Around("execution(void android.app.Fragment.onDestroyView())")
    public void fragmentOnViewDestroyTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        long beforeTime = System.currentTimeMillis();
        joinPoint.proceed();
        Log.d(TAG, joinPoint.getThis().hashCode() + ".onDestroyView duration: " + (System.currentTimeMillis() - beforeTime));
    }
}
