package com.uwi.idworks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.uwi.idworks.service.contract.IDWorksService;




@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class IdworksApplication {
	
//	@Autowired
//	private IDWorksService worksService;
//	
	public static void main(String[] args) {
		SpringApplication.run(IdworksApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//		return args -> {
//			worksService.performUpdates();
//		};
//	}
}
