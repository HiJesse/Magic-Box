package cn.jesse.magicbox.aop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * androidx.fragment.app.fragment aop
 *
 * @author jesse
 */
@Aspect
public class FragmentXAop {
    private static final String TAG = FragmentXAop.class.getSimpleName();

    /**
     * onCreateView 切点
     *
     * @param joinPoint 切点
     * @throws Throwable e
     */
    @Around("execution(android.view.View androidx.fragment.app.Fragment.onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle))")
    public void fragmentXOnViewCreateTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        long beforeTime = System.currentTimeMillis();
        joinPoint.proceed();
        Log.d(TAG, joinPoint.getThis().hashCode() + ".onCreateView duration: " + (System.currentTimeMillis() - beforeTime));
    }

    /**
     * onViewCreated 切点
     *
     * @param joinPoint 切点
     * @throws Throwable e
     */
    @Around("execution(void androidx.fragment.app.Fragment.onViewCreated(android.view.View, android.os.Bundle))")
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
    @Around("execution(void androidx.fragment.app.Fragment.onDestroyView())")
    public void fragmentXOnViewDestroyTriggered(ProceedingJoinPoint joinPoint) throws Throwable {
        long beforeTime = System.currentTimeMillis();
        joinPoint.proceed();
        Log.d(TAG, joinPoint.getThis().hashCode() + ".onDestroyView duration: " + (System.currentTimeMillis() - beforeTime));
    }
}
