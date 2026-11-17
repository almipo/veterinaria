package com.sistema_veterinaria.filters;

import com.sistema_veterinaria.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Registro y login libres; preflight CORS libre.
        if (path.equals("/auth") || path.startsWith("/auth/")
                || exchange.getRequest().getMethod() == HttpMethod.OPTIONS
                || isPublicWebResource(path)) {
            return chain.filter(exchange);
        }

        String authorization = exchange.getRequest().getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authorization.substring(7).trim();
        if (token.isEmpty() || !jwtService.validateToken(token)) {
            return unauthorized(exchange);
        }

        String username = jwtService.extractUsername(token);
        // Evita confiar en un X-Auth-User enviado por el cliente.
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> headers.set("X-Auth-User", username))
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    private boolean isPublicWebResource(String path) {
        return path.equals("/")
                || path.equals("/favicon.ico")
                || path.equals("/error")
                || path.equals("/actuator/health")
                || path.equals("/actuator/info")
                || (path.endsWith(".html") && path.indexOf('/', 1) == -1)
                || path.startsWith("/js/")
                || path.startsWith("/css/")
                || path.startsWith("/images/");
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        // Primero autenticación; luego LoggingFilter (orden -1).
        return -100;
    }
}
