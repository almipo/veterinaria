package com.sistema_veterinaria.filters;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        long start = Instant.now().toEpochMilli();

        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getPath().toString();

        LOGGER.info("[GATEWAY] --> {} {}", method, path);

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {

                    long duration =
                            Instant.now().toEpochMilli() - start;

                    int status = exchange.getResponse().getStatusCode() != null
                            ? exchange.getResponse().getStatusCode().value()
                            : 0;

                    LOGGER.info(
                            "[GATEWAY] <-- {} {} {} {}ms",
                            method,
                            path,
                            status,
                            duration
                    );
                }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}