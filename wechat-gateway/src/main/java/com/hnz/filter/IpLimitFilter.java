package com.hnz.filter;

import com.google.gson.Gson;
import com.hnz.base.BaseInfoProperties;
import com.hnz.result.R;
import com.hnz.result.ResponseStatusEnum;
import com.hnz.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
public class IpLimitFilter extends BaseInfoProperties implements GlobalFilter, Ordered {
    /**
     * @description: 若20秒内同一ip连续请求3次，则限制访问30秒
     */

    @Value("${blackIp.timeInterval}")
    private Integer timeInterval;
    @Value("${blackIp.continueCounts}")
    private Integer continueCounts;
    @Value("${blackIp.limitTime}")
    private Integer limitTime;
    private static final String IP_LIMIT_KEY = "gateway-ip:limit:";
    private static final String BLOCK_KEY = "gateway-ip:block:";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return doLimit(exchange, chain);
    }

    public Mono<Void> doLimit(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String ip = IPUtil.getIP(request);
        String ipRedisKey = IP_LIMIT_KEY + ip;
        // 获取当前 IP 的请求次数（Redis 存储）
        long reqCounts = redis.increment(ipRedisKey, 1);
        // 如果是第一次请求，设置 TTL（timeInterval 秒）
        if (reqCounts == 1) {
            redis.expire(ipRedisKey, timeInterval);
        }
        // 获取当前 TTL 值
        long ttl = redis.ttl(ipRedisKey);
        // 如果 TTL > 0，说明 IP 仍在限制时间内
        if (ttl > 0) {
            // 如果请求次数超过限制，封锁 IP
            if (reqCounts > continueCounts) {
                log.info("IP {} exceeded request limit, blocking for {} seconds", ip, limitTime);
                redis.set(ipRedisKey, BLOCK_KEY, limitTime);  // 设置拉黑时间（limitTime秒）
                redis.expire(ipRedisKey, limitTime); // 设置拉黑的过期时间
                return renderErrorMsg(exchange, ResponseStatusEnum.SYSTEM_ERROR_BLACK_IP); // 返回错误
            }
            // 请求次数未超过限制，继续放行
            return chain.filter(exchange);
        }

        // TTL <= 0，说明限制时间已经过期，可以正常请求
        redis.set(ipRedisKey, "0"); // 重置请求计数
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
