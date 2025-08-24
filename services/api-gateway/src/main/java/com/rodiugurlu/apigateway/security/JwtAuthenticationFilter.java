package com.rodiugurlu.apigateway.security;

import com.rodiugurlu.apigateway.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtService.getUsernameByToken(token);
            if (jwtService.isTokenExpired(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            List<String> roles = jwtService.getRoles(token);
            if (isForbidden(request.getPath().toString(), roles)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // Request header’a username ekle (diğer servisler için)
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-Authenticated-User", username)
                    .build();
            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isForbidden(String path, List<String> roles) {
        if (path.startsWith("/api/admin") && !roles.contains("ADMIN")) {
            return true;
        }
        return false;
    }
}
