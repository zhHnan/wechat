package com.hnz.filter;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.hnz.base.BaseInfoProperties;
import com.hnz.result.ResponseStatusEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：SecurityFilterToken
 * @Date：2025/8/11 20:56
 * @Filename：SecurityFilterToken
 */

@Component
@Slf4j
@RefreshScope
public class SecurityFilterToken extends BaseInfoProperties implements GlobalFilter, Ordered {
    @Resource
    private ExcludeUrlPath excludeUrlPath;
    private AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();
        List<String> excludeUrls = excludeUrlPath.getUrls();
        if (!excludeUrls.isEmpty()) {
            for (String excludeUrl : excludeUrls) {
//                若匹配到排除的 URL，则不进行拦截
                if (matcher.match(excludeUrl, url)) {
                    return chain.filter(exchange);
                }
            }
        }
//        排除静态资源服务器static
        String fileStart = excludeUrlPath.getFileStart();
        if (StringUtils.isNotEmpty(fileStart)){
            if(matcher.match(fileStart, url)){
                return chain.filter(exchange);
            }
        }
//        到达此处说明请求被拦截
//        判断headers中是否包含token
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String userId = headers.getFirst(HEADER_USER_ID);
        String userToken = headers.getFirst(HEADER_USER_TOKEN);
        log.info("用户id:{},用户token:{}", userId, userToken);
        if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(userToken)) {
            String redisToken = redis.get(REDIS_USER_TOKEN + ":" + userId);
            if (redisToken.equals(userToken)) {
                return chain.filter(exchange);
            }
        }
//        默认不放行
        return RenderErrorUtils.display(exchange, ResponseStatusEnum.UN_LOGIN);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
