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
@ConfigurationProperties(prefix="override")
public class MySQLConfig {
   
	private String driver;
    private String database;
	private String user;
	private String password;
   
	@Override
	public String toString() {
		return "ExternalService [driver=" + driver + ", connect=" + database + ", username=" + user + ", password="
				+ password + "]";
	}
}
