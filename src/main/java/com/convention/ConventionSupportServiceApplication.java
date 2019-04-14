package com.convention;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.convention.config.FileStorageConfig;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageConfig.class
})
public class ConventionSupportServiceApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(ConventionSupportServiceApplication.class);
		application.addListeners(new ApplicationPidFileWriter());
		application.run(args);
	}

}
