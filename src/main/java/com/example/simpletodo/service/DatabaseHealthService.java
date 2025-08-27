package com.example.simpletodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Service for checking database health and providing connection information
 */
@Service
public class DatabaseHealthService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseHealthService.class);

    private final DataSource dataSource;
    private final Environment environment;

    @Autowired
    public DatabaseHealthService(DataSource dataSource, Environment environment) {
        this.dataSource = dataSource;
        this.environment = environment;
    }

    @Override
    public void run(String... args) throws Exception {
        checkDatabaseConnection();
    }

    /**
     * Check database connection and log information
     */
    public void checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            logger.info("=== Database Connection Information ===");
            logger.info("Database Product: {} {}", 
                metaData.getDatabaseProductName(), 
                metaData.getDatabaseProductVersion());
            logger.info("Driver: {} {}", 
                metaData.getDriverName(), 
                metaData.getDriverVersion());
            logger.info("URL: {}", metaData.getURL());
            logger.info("Username: {}", metaData.getUserName());
            String[] activeProfiles = environment.getActiveProfiles();
            String profile = activeProfiles.length > 0 ? activeProfiles[0] : "default";
            String url = environment.getProperty("spring.datasource.url");
            logger.info("Configuration: Active Profile: {}, Database URL: {}", profile, url);
            logger.info("Connection Catalog: {}", connection.getCatalog());
            logger.info("Connection Valid: {}", connection.isValid(5));
            logger.info("========================================");
            
        } catch (SQLException e) {
            logger.error("Failed to establish database connection", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    /**
     * Get database connection status
     */
    public boolean isHealthy() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5);
        } catch (SQLException e) {
            logger.warn("Database health check failed", e);
            return false;
        }
    }

    /**
     * Get database product name
     */
    public String getDatabaseProduct() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            return metaData.getDatabaseProductName();
        } catch (SQLException e) {
            logger.warn("Failed to get database product name", e);
            return "Unknown";
        }
    }
}
