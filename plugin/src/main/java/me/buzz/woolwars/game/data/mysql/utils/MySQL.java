package me.buzz.woolwars.game.data.mysql.utils;

import com.zaxxer.hikari.HikariDataSource;
import me.buzz.woolwars.game.data.mysql.connectors.HikariConnector;
import me.buzz.woolwars.game.data.mysql.connectors.PoolSettings;

public class MySQL extends SQLImplementation {

    private final String host;
    private final int port;
    private final String database;
    private final String user;

    public MySQL(String host, int port, String database, String user, String password, PoolSettings poolSettings) {
        super(new HikariConnector(host, port, database, user, password, poolSettings));

        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
    }

    public MySQL(String host, int port, String database, String user, String password) {
        this(host, port, database, user, password, new PoolSettings());
    }

    public MySQL(HikariDataSource dataSource) {
        super(new HikariConnector(dataSource));
        this.host = "";
        this.port = 3306;
        this.database = "";
        this.user = "";
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public static class Builder {
        private String host;
        private int port = -1;
        private String database;
        private String user;
        private String password;
        private boolean pool = true;
        private PoolSettings poolSettings;

        public String getHost() {
            return host;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public int getPort() {
            return port;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public String getDatabase() {
            return database;
        }

        public Builder setDatabase(String database) {
            this.database = database;
            return this;
        }

        public String getUser() {
            return user;
        }

        public Builder setUser(String user) {
            this.user = user;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public boolean isPool() {
            return pool;
        }

        public Builder setPool(boolean pool) {
            this.pool = pool;
            return this;
        }

        public boolean usePool() {
            return pool;
        }

        public Builder usePool(boolean pool) {
            this.pool = pool;
            return this;
        }

        public PoolSettings getPoolSettings() {
            return poolSettings;
        }

        public Builder setPoolSettings(PoolSettings poolSettings) {
            this.poolSettings = poolSettings;
            return this;
        }

        public MySQL build() {
            if (host == null || host.trim().isEmpty() ||
                    port == -1 ||
                    database == null || database.trim().isEmpty() ||
                    user == null || user.trim().isEmpty() ||
                    password == null || password.trim().isEmpty())
                throw new IllegalArgumentException("Some required parameters are missing.");

            return new MySQL(host, port, database, user, password, poolSettings);
        }
    }
}
