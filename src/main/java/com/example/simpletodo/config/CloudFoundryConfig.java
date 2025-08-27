package com.example.simpletodo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

/**
 * Configuration for Cloud Foundry platform-specific settings
 */
@Configuration
@ConditionalOnCloudPlatform(CloudPlatform.CLOUD_FOUNDRY)
@Profile("prod")
public class CloudFoundryConfig {

    private static final Logger logger = LoggerFactory.getLogger(CloudFoundryConfig.class);

    private final Environment environment;

    public CloudFoundryConfig(Environment environment) {
        this.environment = environment;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logCloudFoundryInfo() {
        logger.info("=== Cloud Foundry Application Information ===");
        
        // Log CF-specific environment variables
        String cfInstanceIndex = environment.getProperty("CF_INSTANCE_INDEX", "0");
        String cfInstanceAddr = environment.getProperty("CF_INSTANCE_ADDR", "unknown");
        String cfInstancePort = environment.getProperty("CF_INSTANCE_PORT", "8080");
        String cfAppName = environment.getProperty("VCAP_APPLICATION", "simple-todo");
        
        logger.info("CF Instance Index: {}", cfInstanceIndex);
        logger.info("CF Instance Address: {}", cfInstanceAddr);
        logger.info("CF Instance Port: {}", cfInstancePort);
        logger.info("VCAP Application: {}", cfAppName);
        
        // Log bound services
        String vcapServices = environment.getProperty("VCAP_SERVICES");
        if (vcapServices != null) {
            logger.info("Bound Services Available: YES");
            logger.debug("VCAP_SERVICES: {}", vcapServices);
        } else {
            logger.warn("No bound services detected");
        }
        
        // Log database connection info
        String dbUrl = environment.getProperty("spring.datasource.url");
        String dbUsername = environment.getProperty("spring.datasource.username");
        logger.info("Database URL: {}", dbUrl);
        logger.info("Database Username: {}", dbUsername);
        
        logger.info("============================================");
    }
}
