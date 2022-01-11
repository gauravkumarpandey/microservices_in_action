package com.optimagrowth.license.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class ServiceConfig {
	
	@Value("${example.property}")
	private String property;

}
