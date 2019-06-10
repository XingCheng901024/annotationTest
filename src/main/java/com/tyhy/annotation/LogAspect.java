package com.tyhy.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2019/6/10.
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    private static Date beforeTime = null;
    private static Date afterTime = null;
    private static Date afterReturningTime = null;
    private static Date afterThrowingTime = null;


    // 配置织入点
    @Pointcut("@annotation(com.tyhy.annotation.SystemControllerLog)")
    public void logPointCut() {
    }

    /**
     * 前置通知 用于拦截操作，在方法执行前执行
     * @param joinPoint 切点
     */
    @Before(value = "logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        beforeTime = new Date();
        handleLog(joinPoint, null);
    }

    /**
     * 前置通知 用于拦截操作，在方法执行后执行
     * @param joinPoint 切点
     */
    @After(value = "logPointCut()")
    public void doAfter(JoinPoint joinPoint) {
        afterTime = new Date();
        handleLog(joinPoint, null);
    }

    /**
     * 前置通知 用于拦截操作，在方法返回后执行
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()")
    public void doAfterReturning(JoinPoint joinPoint) {
        afterReturningTime = new Date();
        handleLog(joinPoint, null);
    }

    /**
     * 拦截异常操作，有异常时执行
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        afterThrowingTime = new Date();
        handleLog(joinPoint, e);
    }

    private void handleLog(JoinPoint joinPoint, Exception e) {
        try {
            // 获得注解
            SystemControllerLog controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            // 获得方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            String author = controllerLog.author();
            String action = controllerLog.action();
            String title = controllerLog.title();
            log.info(">>>>>>>>>>>>>开发者名称：{}",author);
            log.info(">>>>>>>>>>>>>模块名称：{}",title);
            log.info(">>>>>>>>>>>>>操作名称：{}",action);
            log.info(">>>>>>>>>>>>>类名：{}",className);
            Long seconds = null;
            log.info(">>>>>>>>>>>>>运行时间：{}",sdf.format(beforeTime));
            if(afterTime!=null){
                log.info(">>>>>>>>>>>>>结束时间：{}",sdf.format(afterTime));
                seconds = (afterTime.getTime()-beforeTime.getTime())/1000;
                log.info(">>>>>>>>>>>>>耗时：{}",seconds);
            }else if(afterThrowingTime!=null){
                log.info(">>>>>>>>>>>>>结束时间：{}",sdf.format(afterThrowingTime));
                seconds = (afterThrowingTime.getTime()-beforeTime.getTime())/1000;
                log.info(">>>>>>>>>>>>>耗时：{}",seconds);
            }else{
                log.debug(">>>>>>>>>>>>>未能捕获该方法的结束时间!");
            }

        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private static SystemControllerLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(SystemControllerLog.class);
        }
        return null;
    }

}
