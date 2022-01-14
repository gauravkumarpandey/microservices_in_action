package com.optimagrowth.gateway.filters;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Order(1)
@Component
@Slf4j
public class TrackingFilter implements GlobalFilter{

	@Autowired
	private FilterUtils filterUtils; 
	
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpHeaders headers =  exchange.getRequest()
		      .getHeaders();
		
		if(this.isCorrelationIdPresent(headers)) {
			log.info("tmx-correlation-id found in tracking filter: {}. ", 
					filterUtils.getCorrelationId(headers));
		}
		else {
			String correlationID = generateCorrelationId();
			exchange = filterUtils.setCorrelationId(exchange, correlationID);
			log.info("tmx-correlation-id generated in tracking filter: {}.", correlationID);
		}
	   return chain.filter(exchange);
	}
	
	private boolean isCorrelationIdPresent(HttpHeaders headers) {
		return Optional.ofNullable(filterUtils.getCorrelationId(headers))
		        .map(s-> true)
		        .orElse(false);
	}
	
	private String generateCorrelationId() {
		return java.util.UUID.randomUUID().toString();
	}


}
