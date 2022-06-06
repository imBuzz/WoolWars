package me.buzz.woolwars.game.data.mysql.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {

    /**
     * Closes the ResultSet, the Statement and the Connection.
     *
     * @param result CompositeResult with ResultSet, Statement and Connection to close
     */
    public static void closeQuietly(CompositeResult result) {
        try {
            if (result.getResult() != null) {
                if (!result.getResult().isClosed()) {
                    result.getResult().close();
                }
            }

            if (result.getStatement() != null) {
                if (!result.getStatement().isClosed()) {
                    result.getStatement().close();
                }
            }

            if (result.getConnection() != null) {
                if (!result.getConnection().isClosed()) {
                    result.getConnection().close();
                }
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * Closes the ResultSet, the Statement and the Connection.
     *
     * @param connection Connection to close
     * @param statement  Statement to close
     * @param result     ResultSet to close
     */
    public static void closeQuietly(Connection connection, Statement statement, ResultSet result) {
        try {
            if (result != null) {
                if (!result.isClosed()) {
                    result.close();
                }
            }

            if (statement != null) {
                if (!statement.isClosed()) {
                    statement.close();
                }
            }

            if (connection != null) {
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * Closes the ResultSet, the Statement and the Connection.
     *
     * @param result ResultSet to close
     */
    public static void closeQuietly(ResultSet result) {
        try {
            if (result != null) {
                if (!result.isClosed()) {
                    result.close();
                }
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * Closes the ResultSet, the Statement and the Connection.
     *
     * @param statement Statement to close
     */
    public static void closeQuietly(Statement statement) {
        try {
            if (statement != null) {
                if (!statement.isClosed()) {
                    statement.close();
                }
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * Closes the ResultSet, the Statement and the Connection.
     *
     * @param connection Connection to close
     */
    public static void closeQuietly(Connection connection) {
        try {
            if (connection != null) {
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
        } catch (SQLException ignored) {
        }
    }
}
