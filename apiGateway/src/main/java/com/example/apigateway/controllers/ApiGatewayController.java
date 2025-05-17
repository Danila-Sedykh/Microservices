package com.example.apigateway.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ApiGatewayController {

    /*@Autowired
    private RestTemplate restTemplate;*/

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @RequestMapping("/auth-service/auth/**")
    public ResponseEntity<?> routeToAuthService(
            HttpServletRequest request,
            @RequestBody(required = false) String body,
            @RequestHeader(required = false) Map<String, String> headers,
            @RequestParam(required = false) Map<String, String> queryParams) {
        String targetUrl = getServiceUrl("auth-service") + getRequestPath(request, "/auth-service");
        String method = request.getMethod();
        System.out.println(targetUrl);
        return forwardRequest(method, targetUrl, body, headers, queryParams);
    }

    @RequestMapping("/consumer-service/consumer/**")
    public ResponseEntity<?> routeToConsumerService(
            HttpServletRequest request,
            @RequestBody(required = false) String body,
            @RequestHeader Map<String, String> headers,
            @RequestParam(required = false) Map<String, String> queryParams) {
        System.out.println("1");
        String targetUrl = getServiceUrl("consumer-service") + getRequestPath(request, "/consumer-service");
        String method = request.getMethod();
        return forwardRequest(method, targetUrl, body, headers, queryParams);
    }

    @RequestMapping("/producer-service/producer/**")
    public ResponseEntity<?> routeToProducerService(
            HttpServletRequest request,
            @RequestBody(required = false) String body,
            @RequestHeader(required = false) Map<String, String> headers,
            @RequestParam(required = false) Map<String, String> queryParams) {
        String targetUrl = getServiceUrl("producer-service") + getRequestPath(request, "/producer-service");
        String method = request.getMethod();
        return forwardRequest(method, targetUrl, body, headers, queryParams);
    }

    private String getServiceUrl(String serviceName) {
        return loadBalancerClient.choose(serviceName).getUri().toString();
    }

    private String getRequestPath(HttpServletRequest request, String prefix) {
        return request.getRequestURI().substring(prefix.length());
    }

    private ResponseEntity<?> forwardRequest(String method, String url, String body, Map<String, String> headers,Map<String, String> queryParams) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();

            headers.forEach((key, value) -> {
                if ("authorization".equalsIgnoreCase(key)) {
                    httpHeaders.set("Authorization", value);
                } else {
                    httpHeaders.add(key, value);
                }
            });

            if (!httpHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            }
            if (queryParams != null && !queryParams.isEmpty()) {
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
                queryParams.forEach(uriBuilder::queryParam);
                url = uriBuilder.toUriString();
            }


            HttpEntity<String> requestEntity = new HttpEntity<>(body, httpHeaders);
            HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());

            return restTemplate.exchange(url, httpMethod, requestEntity, String.class);
        }catch (HttpClientErrorException e){
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            }
            throw e;
        }
    }
}

