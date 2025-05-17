/*
package com.example.apigateway.configs;

import com.example.apigateway.filter.JwtAuthenticationFilter;
import com.example.apigateway.filter.LoggingFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("producer_route", r -> r.path("/producer/**")
                        .uri("http://localhost:8081")) // URL Producer
                .route("consumer_route", r -> r.path("/consumer/**")
                        .uri("http://localhost:8082")) // URL Consumer
                .build();
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtFilter) {
        return builder.routes()
                .route("producer_route", r -> r.path("/producer/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8081"))
                .route("consumer_route", r -> r.path("/consumer/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8082"))
                .build();
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, JwtAuthenticationFilter jwtFilter, LoggingFilter loggingFilter) {
        return builder.routes()
                .route("producer_route", r -> r.path("/producer/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilter.Config()))
                                .filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri("http://localhost:8081"))
                .route("consumer_route", r -> r.path("/consumer/**")
                        .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilter.Config()))
                                .filter(loggingFilter.apply(new LoggingFilter.Config())))
                        .uri("http://localhost:8082"))
                .build();
    }
}
*/
