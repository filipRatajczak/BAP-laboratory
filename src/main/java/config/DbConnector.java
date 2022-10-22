package config;

import exception.DbConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.util.Objects.isNull;

public class DbConnector {
    private static final Logger log = LoggerFactory.getLogger(DbConnector.class);
    private Connection connection = null;
    private static final String PROPERTIES_PATH = "src/main/resources/application.properties";
    private static final String DATABASE_PROPERTIES_NAME = "db.url";

    public Connection getConnection() {
        if (isNull(connection)) {
            String databaseUrl = "";
            try {
                databaseUrl = getDbUrlFromProperties();
                this.connection = DriverManager.getConnection(databaseUrl);
                log.info("Successfully connected to database: [{}]", databaseUrl);
            } catch (IOException | SQLException exception) {
                log.error("Exception message: [{}]", exception.getMessage(), exception);
                throw new DbConnectorException("Cannot create connection to database. Database url: [" + databaseUrl + "]");
            }
        }
        return this.connection;
    }

    private static String getDbUrlFromProperties() throws IOException {
        Properties applicationProperties = new Properties();
        applicationProperties.load(new FileInputStream(PROPERTIES_PATH));
        return applicationProperties.getProperty(DATABASE_PROPERTIES_NAME);
    }
}
