package com.uwi.idworks.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Service
@Getter
@Setter
@ConfigurationProperties(prefix="term.config")
public class TermConfig {
     private String override;
	private String semester;
}
