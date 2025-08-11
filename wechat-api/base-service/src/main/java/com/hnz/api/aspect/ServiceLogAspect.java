package com.hnz.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：ServiceLogAspect
 * @Date：2025/8/11 20:24
 * @Filename：ServiceLogAspect
 */

@Component
@Slf4j
@Aspect
public class ServiceLogAspect {
    @Around("execution(* com.hnz.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        统计service中方法的执行时间，若方法执行时间过长，则打印error级日志
        StopWatch stopwatch = new StopWatch();
        String pointName = joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName();
        stopwatch.start("方法执行："+ pointName);
        Object proceed = joinPoint.proceed();
        stopwatch.stop();
        long time = stopwatch.getTotalTimeMillis();
        if (time > 3000) {
            log.error("{}方法执行时间过长：{}ms", pointName, time);
        }else if (time > 2000) {
            log.warn("{}方法执行时间稍长：{}ms", pointName, time);
        }else {
            log.info("{}方法执行时间正常：{}ms", pointName, time);
        }
        return proceed;
    }
}
