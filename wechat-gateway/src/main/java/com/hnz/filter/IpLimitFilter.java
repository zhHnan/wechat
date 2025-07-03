package com.hnz.filter;

import com.google.gson.Gson;
import com.hnz.base.BaseInfoProperties;
import com.hnz.result.R;
import com.hnz.result.ResponseStatusEnum;
import com.hnz.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：IpLimitFilter
 * @Date：2025/7/3 14:52
 * @Filename：IpLimitFilter
 */

@Component
@Slf4j
public class IpLimitFilter extends BaseInfoProperties implements GlobalFilter, Ordered {
    /**
     * @description: 若20秒内同一ip连续请求3次，则限制访问30秒
     * */
    private static final Integer ContinueCounts = 3;
    private static final Integer timeInterval = 20;
    private static final Integer limitTime = 30;
    private static final String ipLimitKey = "gateway-ip:limit:";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return doLimit(exchange, chain);
    }
    public Mono<Void> doLimit(ServerWebExchange exchange, GatewayFilterChain chain){
        ServerHttpRequest request = exchange.getRequest();
        String ip = IPUtil.getIP(request);
        String ipRedisKey = ipLimitKey + ip;
//        若在redis中存在ip，则判断是否在限制时间内
        long ttl = redis.ttl(ipRedisKey);
        if (ttl > 0){
//            终止请求，返回错误
            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }
//        若在限制时间外
//        在redis中访问ip累加次数
        long reqCounts = redis.increment(ipRedisKey, 1);
        if (reqCounts == 1){
            redis.expire(ipRedisKey, timeInterval);
        }
//        若还能获得请求的正常次数，则说明用户的连续请求落在限定的【timeInterval】之内
//        一旦请求次数超过限定的连续访问次数【ContinueCounts】，则需要限制当前的ip
        if(reqCounts > ContinueCounts){
            redis.set(ipRedisKey, ipRedisKey, limitTime);
//            终止请求，返回错误
            return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP);
        }
        return chain.filter(exchange);
    }
    public Mono<Void> renderErrorMsg(ServerWebExchange exchange, ResponseStatusEnum responseStatusEnum) {
        ServerHttpResponse response = exchange.getResponse();
        R result = R.exception(responseStatusEnum);
//        设置header类型
        if (!response.getHeaders().containsKey("Content-Type")) {
            response.getHeaders().add("Content-Type", MimeTypeUtils.APPLICATION_JSON_VALUE);
        }
//        修改response状态码为500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
//        转换json写入response
        String resJson = new Gson().toJson(result);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(resJson.getBytes(StandardCharsets.UTF_8))));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
