package me.buzz.woolwars.game.data.mysql.connectors;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class HikariConnector implements Connector {

    private final HikariDataSource dataSource;

    public HikariConnector(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public HikariConnector(String host, int port, String database, String user, String password, String driver, PoolSettings poolSettings) {
        HikariConfig config = new HikariConfig();

        config.setDataSourceClassName(driver);
        config.addDataSourceProperty("serverName", host);
        config.addDataSourceProperty("port", port);
        config.addDataSourceProperty("databaseName", database);
        config.setUsername(user);
        config.setPassword(password);

        config.setPoolName(poolSettings.getPoolName());
        config.setMaximumPoolSize(poolSettings.getMaximumPoolSize());
        config.setMinimumIdle(poolSettings.getMinimumIdle());
        config.setInitializationFailTimeout(poolSettings.getInitializationFailTimeout());
        config.setConnectionTimeout(poolSettings.getConnectionTimeout());
        config.setIdleTimeout(poolSettings.getIdleTimeout());
        config.setMaxLifetime(poolSettings.getMaxLifetime());
        config.setLeakDetectionThreshold(poolSettings.getLeakDetectionThreshold());
        config.setConnectionTestQuery("SELECT 1");

        for (Map.Entry<String, String> property : poolSettings.getDataSourceProperties().entrySet()) {
            config.addDataSourceProperty(property.getKey(), property.getValue());
        }

        for (Map.Entry<String, String> property : poolSettings.getDataSourceProperties().entrySet()) {
            config.addHealthCheckProperty(property.getKey(), property.getValue());
        }

        dataSource = new HikariDataSource(config);
    }

    public HikariConnector(String host, int port, String database, String user, String password, PoolSettings poolSettings) {
        HikariConfig config = new HikariConfig();

        MariaDbDataSource ds = new MariaDbDataSource();
        String url = "jdbc:mariadb://" + host + ":" + port + "/" + database;
        try {
            ds.setUrl(url);
            ds.setUser(user);
            ds.setPassword(password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        config.setDataSource(ds);

        config.setPoolName(poolSettings.getPoolName());
        config.setMaximumPoolSize(poolSettings.getMaximumPoolSize());
        config.setMinimumIdle(poolSettings.getMinimumIdle());
        config.setInitializationFailTimeout(poolSettings.getInitializationFailTimeout());
        config.setConnectionTimeout(poolSettings.getConnectionTimeout());
        config.setIdleTimeout(poolSettings.getIdleTimeout());
        config.setMaxLifetime(poolSettings.getMaxLifetime());
        config.setLeakDetectionThreshold(poolSettings.getLeakDetectionThreshold());
        config.setConnectionTestQuery("SELECT 1");

        for (Map.Entry<String, String> property : poolSettings.getDataSourceProperties().entrySet()) {
            config.addDataSourceProperty(property.getKey(), property.getValue());
        }

        for (Map.Entry<String, String> property : poolSettings.getDataSourceProperties().entrySet()) {
            config.addHealthCheckProperty(property.getKey(), property.getValue());
        }

        dataSource = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to get a connection from the pool. (dataSource is null)");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to get a connection from the pool. (connection is null)");
        }

        return connection;
    }

    @Override
    public void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
