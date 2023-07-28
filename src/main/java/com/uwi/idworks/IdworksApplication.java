package com.uwi.idworks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.uwi.idworks.service.contract.IDWorksService;




@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class IdworksApplication {
	
	 private static ConfigurableApplicationContext context;
//	@Autowired
//	private IDWorksService worksService;
	
	public static void main(String[] args) {
		SpringApplication.run(IdworksApplication.class, args);
	}
    public static void restart() {
		
		ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(SpringApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//		return args -> {
//			worksService.performUpdates();
//		};
//	}
}
