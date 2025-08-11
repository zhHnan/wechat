package com.hnz.filter;

import com.google.gson.Gson;
import com.hnz.base.BaseInfoProperties;
import com.hnz.result.R;
import com.hnz.result.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：RenderErrorUtils
 * @Date：2025/8/11 21:34
 * @Filename：RenderErrorUtils
 */

@Component
@Slf4j
@RefreshScope
public class RenderErrorUtils extends BaseInfoProperties {
    public static Mono<Void> display(ServerWebExchange exchange, ResponseStatusEnum responseStatusEnum) {
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
}
