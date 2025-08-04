package com.hnz.controller.interceptor;

import com.hnz.base.BaseInfoProperties;
import com.hnz.exceptions.GraceException;
import com.hnz.exceptions.MyCustomException;
import com.hnz.result.ResponseStatusEnum;
import com.hnz.utils.IPUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static com.hnz.base.BaseInfoProperties.MOBILE_SMSCODE;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：SmsInterceptor
 * @Date：2025/7/4 18:24
 * @Filename：SmsInterceptor
 */

@Slf4j
public class SmsInterceptor extends BaseInfoProperties implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIp = IPUtil.getRequestIp(request);
        if (redis.keyIsExist(MOBILE_SMSCODE + ":" + userIp)) {
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
