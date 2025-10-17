package com.hnz.api.aspect;

import com.hnz.utils.IPUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@Aspect
public class ServiceLogAspect {
    // 时间格式化器（统一时间格式）
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Around("execution(* com.hnz.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopwatch = new StopWatch();
        String methodName = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();

        // 1. 获取请求上下文信息（IP、用户ID）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ip = "unknown";
        String userId = "anonymous";
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            ip = IPUtil.getRequestIp(request);
            userId = request.getHeader("userId") == null ? "anonymous" : request.getHeader("userId");
        }

        // 2. 记录开始时间（UTC格式，与logback一致）
        String startTime = LocalDateTime.now().format(formatter);

        // 3. 执行目标方法并记录结果
        stopwatch.start(methodName);
        Object result = null;
        String executeResult = "success";
        try {
            // 往MDC中添加变量，供logback的encoder提取
            MDC.put("userId", userId);
            MDC.put("ip", ip);
            MDC.put("methodName", methodName);
            MDC.put("startTime", startTime);

            result = joinPoint.proceed();
        } catch (Exception e) {
            executeResult = "fail: " + e.getMessage();
            throw e;
        } finally {
            stopwatch.stop();
            long time = stopwatch.getTotalTimeMillis();

            // 4. 输出结构化日志（包含所有统计信息）
            log.info("method executed: time={}ms, result={}", time, executeResult);

            // 清除MDC变量，避免线程复用污染
            MDC.clear();
        }
        return result;
    }
}