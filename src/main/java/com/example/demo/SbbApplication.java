package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import java.time.ZoneId;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class SbbApplication implements ApplicationListener<ApplicationReadyEvent> {

    public static void main(String[] args) {
        SpringApplication.run(SbbApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Environment env = event.getApplicationContext().getEnvironment();
        String[] profiles = env.getActiveProfiles();
        String port = env.getProperty("server.port", "8080");
        String tz = env.getProperty("spring.jackson.time-zone", ZoneId.systemDefault().getId());

        log.info("=== SBB Started ===");
        log.info("Active profiles   : {}", profiles.length == 0 ? "[default]" : Arrays.toString(profiles));
        log.info("Server port       : {}", port);
        log.info("App TimeZone      : {}", tz);
        log.info("Log file (prod)   : {}", env.getProperty("logging.file.name", "(console only)"));
    }
}
