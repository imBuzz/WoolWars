package me.buzz.woolwars.game.data.mysql.utils;

import me.buzz.woolwars.game.data.mysql.connectors.Connector;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class SQLImplementation {
    private static final SimpleDateFormat TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    public Connector connector;
    protected boolean printQuery = false;

    protected SQLImplementation(Connector connector) {
        this.connector = connector;
    }

    private boolean restartConnection(Exception e) {
        return e.getMessage().contains("Connection is not available, request timed out after ") ||
                e.getMessage().contains("Too many connections") ||
                e.getMessage().contains("has been closed.");
    }

    protected String objectToString(Object obj) {
        if (obj instanceof Boolean) {
            if ((Boolean) obj) return "1";
            else return "0";
        } else if (obj instanceof java.util.Date) {
            return TIMESTAMP.format((java.util.Date) obj).replace("24:", "00:");
        } else if (obj instanceof String) {
            String str = (String) obj;
            if (str.contains("'")) str = str.replace("'", "''");
            obj = str;
        }

        return String.valueOf(obj);
    }

    /**
     * Sets this to true to print queries.
     *
     * @param printQuery Set this to true to print queries
     */
    public void setPrintQuery(boolean printQuery) {
        this.printQuery = printQuery;
    }

    /**
     * Terminates the connection with the database.
     */
    public void shutdown() {
        connector.shutdown();
    }

    /**
     * Restart the connection with the database
     */
    public void reset(String reason) throws SQLException {
        Logger.getLogger(getClass().getSimpleName()).warning("Database connection reset! Reason: " + reason);
        connector.shutdown();
        connector.getConnection();
    }

    /**
     * Return the ping with the database.
     *
     * @return The ping in milliseconds with the database
     */
    public long ping() {
        long start = System.currentTimeMillis();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connector.getConnection();
            statement = connection.prepareStatement("SELECT 1");
            statement.execute();
            return System.currentTimeMillis() - start;
        } catch (SQLException ignored) {
            return -1;
        } finally {
            DBUtils.closeQuietly(statement);
            DBUtils.closeQuietly(connection);
        }
    }

    /**
     * Executes a given MySQL query.
     *
     * @param query the query to be executed
     * @param table the table to be used
     * @return the CompositeResult of the query
     * @throws SQLException SQLException
     */
    public CompositeResult executeQuery(String query, String table) throws SQLException {
        if (query.trim().isEmpty()) throw new IllegalArgumentException("Query cannot be empty");
        query = query.replace("{table}", table);

        try {
            Connection connection = connector.getConnection();
            PreparedStatement statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet result = statement.executeQuery();

            if (printQuery) System.out.println(query);

            return new CompositeResult(connection, statement, result, query);
        } catch (SQLException e) {
            if (restartConnection(e)) reset(e.getMessage());
            throw e;
        }
    }

    /**
     * Executes an update given a MySQL query.
     *
     * @param query the query to be executed
     * @param table the table to be used
     * @throws SQLException SQLException
     */
    public void executeUpdate(String query, String table) throws SQLException {
        if (query.trim().isEmpty()) throw new IllegalArgumentException("Query cannot be empty");
        query = query.replace("{table}", table);

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connector.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();

            if (printQuery) System.out.println(query);
        } catch (SQLException e) {
            if (restartConnection(e)) reset(e.getMessage());
            throw e;
        } finally {
            DBUtils.closeQuietly(statement);
            DBUtils.closeQuietly(connection);
        }
    }

    /**
     * Creates a new table if it is not present in the database.
     *
     * @param args    the list of columns with their type (ex. `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY)
     * @param table   the table to create
     * @param charset the default character set
     * @throws SQLException SQLException
     */
    public void createTable(String[] args, String table, String charset) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS `")
                .append(table)
                .append("` (");
        for (int i = 0; i < args.length; i++) {
            query.append(args[i]);
            if (i != args.length - 1) query.append(", ");
        }
        if (charset == null || charset.isEmpty()) {
            query.append(");");
        } else {
            query.append(") DEFAULT CHARACTER SET ").append(charset).append(";");
        }

        executeUpdate(query.toString(), table);
    }

    /**
     * Creates a new table if it is not present in the database.
     *
     * @param args  the list of columns with their type (ex. `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY)
     * @param table the table to create
     * @throws SQLException SQLException
     */
    public void createTable(String[] args, String table) throws SQLException {
        createTable(args, table, null);
    }

    /**
     * Adds a new row to the table assigning the given values to the given columns.
     *
     * @param columns the list of columns to edit
     * @param values  the list of values to be added to the columns
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public void addRow(String[] columns, Object[] values, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO `")
                .append(table)
                .append("` (");
        for (int i = 0; i < columns.length; i++) {
            query.append("`")
                    .append(columns[i])
                    .append("`");
            if (i != columns.length - 1) query.append(", ");
        }
        query.append(") VALUES (");
        for (int i = 0; i < values.length; i++) {
            query.append("'")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != values.length - 1) query.append(", ");
        }
        query.append(");");

        executeUpdate(query.toString(), table);
    }

    /**
     * Adds a new row to the table assigning the given value to the given column.
     *
     * @param column the column to edit
     * @param value  the value to be added to the column
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public void addRow(String column, Object value, String table) throws SQLException {
        addRow(new String[]{column}, new Object[]{value}, table);
    }

    /**
     * Removes a row from the table where the given columns have the given values.
     *
     * @param columns the list of columns for the research
     * @param values  the values to be searched in the columns
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public void removeRow(String[] columns, Object[] values, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");

        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`").append(columns[i]).append("`").append(" IS NULL");
            else
                query.append("`").append(columns[i]).append("`").append(" = '").append(objectToString(values[i])).append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        executeUpdate(query.toString(), table);
    }

    /**
     * Removes a row from the table where the given columns have the given values.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public void removeRow(String column, Object value, String table) throws SQLException {
        removeRow(new String[]{column}, new Object[]{value}, table);
    }

    /**
     * Checks if a row exists with the given values in the given columns.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public boolean rowExists(String[] columns, Object[] values, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        boolean b = result.getResult().next();
        result.close();
        return b;
    }

    /**
     * Checks if a row exists with the given value in the given column.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public boolean rowExists(String column, Object value, String table) throws SQLException {
        return rowExists(new String[]{column}, new Object[]{value}, table);
    }

    /**
     * Gets a Byte from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Byte getByte(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        Byte b = null;
        if (result.getResult().absolute(row)) {
            b = result.getResult().getByte(search);
        }
        result.close();
        return b;
    }

    /**
     * Gets a Byte from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Byte getByte(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getByte(columns, values, search, 1, table);
    }

    /**
     * Gets a Byte from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Byte getByte(String column, Object value, String search, int row, String table) throws SQLException {
        return getByte(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a Byte from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Byte getByte(String column, Object value, String search, String table) throws SQLException {
        return getByte(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a Short from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Short getShort(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        Short s = null;
        if (result.getResult().absolute(row)) {
            s = result.getResult().getShort(search);
        }
        result.close();
        return s;
    }

    /**
     * Gets a Short from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Short getShort(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getShort(columns, values, search, 1, table);
    }

    /**
     * Gets a Short from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Short getShort(String column, Object value, String search, int row, String table) throws SQLException {
        return getShort(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a Short from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Short getShort(String column, Object value, String search, String table) throws SQLException {
        return getShort(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a Integer from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Integer getInteger(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        Integer i = null;
        if (result.getResult().absolute(row)) {
            i = result.getResult().getInt(search);
        }
        result.close();
        return i;
    }

    /**
     * Gets a Integer from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Integer getInteger(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getInteger(columns, values, search, 1, table);
    }

    /**
     * Gets a Integer from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Integer getInteger(String column, Object value, String search, int row, String table) throws SQLException {
        return getInteger(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a Integer from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Integer getInteger(String column, Object value, String search, String table) throws SQLException {
        return getInteger(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a Long from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Long getLong(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        Long l = null;
        if (result.getResult().absolute(row)) {
            l = result.getResult().getLong(search);
        }
        result.close();
        return l;
    }

    /**
     * Gets a Long from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Long getLong(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getLong(columns, values, search, 1, table);
    }

    /**
     * Gets a Long from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Long getLong(String column, Object value, String search, int row, String table) throws SQLException {
        return getLong(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a Long from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Long getLong(String column, Object value, String search, String table) throws SQLException {
        return getLong(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a Float from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Float getFloat(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        Float f = null;
        if (result.getResult().absolute(row)) {
            f = result.getResult().getFloat(search);
        }
        result.close();
        return f;
    }

    /**
     * Gets a Float from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Float getFloat(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getFloat(columns, values, search, 1, table);
    }

    /**
     * Gets a Float from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Float getFloat(String column, Object value, String search, int row, String table) throws SQLException {
        return getFloat(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a Float from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Float getFloat(String column, Object value, String search, String table) throws SQLException {
        return getFloat(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a Double from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Double getDouble(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        Double d = null;
        if (result.getResult().absolute(row)) {
            d = result.getResult().getDouble(search);
        }
        result.close();
        return d;
    }

    /**
     * Gets a Double from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Double getDouble(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getDouble(columns, values, search, 1, table);
    }

    /**
     * Gets a Double from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Double getDouble(String column, Object value, String search, int row, String table) throws SQLException {
        return getDouble(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a Double from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Double getDouble(String column, Object value, String search, String table) throws SQLException {
        return getDouble(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a String from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public String getString(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        String s = null;
        if (result.getResult().absolute(row)) {
            s = result.getResult().getString(search);
        }
        result.close();
        return s;
    }

    /**
     * Gets a String from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public String getString(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getString(columns, values, search, 1, table);
    }

    /**
     * Gets a String from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public String getString(String column, Object value, String search, int row, String table) throws SQLException {
        return getString(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a String from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public String getString(String column, Object value, String search, String table) throws SQLException {
        return getString(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a Boolean from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Boolean getBoolean(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        Boolean b = null;
        if (result.getResult().absolute(row)) {
            b = result.getResult().getBoolean(search);
        }
        result.close();
        return b;
    }

    /**
     * Gets a Boolean from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Boolean getBoolean(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getBoolean(columns, values, search, 1, table);
    }

    /**
     * Gets a Boolean from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Boolean getBoolean(String column, Object value, String search, int row, String table) throws SQLException {
        return getBoolean(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a Boolean from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Boolean getBoolean(String column, Object value, String search, String table) throws SQLException {
        return getBoolean(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a Timestamp from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Timestamp getTimestamp(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        Timestamp t = null;
        if (result.getResult().absolute(row)) {
            t = result.getResult().getTimestamp(search);
        }
        result.close();
        return t;
    }

    /**
     * Gets a Timestamp from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Timestamp getTimestamp(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getTimestamp(columns, values, search, 1, table);
    }

    /**
     * Gets a Timestamp from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Timestamp getTimestamp(String column, Object value, String search, int row, String table) throws SQLException {
        return getTimestamp(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a Timestamp from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Timestamp getTimestamp(String column, Object value, String search, String table) throws SQLException {
        return getTimestamp(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a Date from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Date getDate(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        Date d = null;
        if (result.getResult().absolute(row)) {
            d = result.getResult().getDate(search);
        }
        result.close();
        return d;
    }

    /**
     * Gets a Date from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Date getDate(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getDate(columns, values, search, 1, table);
    }

    /**
     * Gets a Date from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Date getDate(String column, Object value, String search, int row, String table) throws SQLException {
        return getDate(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a Date from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Date getDate(String column, Object value, String search, String table) throws SQLException {
        return getDate(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a Time from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Time getTime(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        Time t = null;
        if (result.getResult().absolute(row)) {
            t = result.getResult().getTime(search);
        }
        result.close();
        return t;
    }

    /**
     * Gets a Time from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Time getTime(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getTime(columns, values, search, 1, table);
    }

    /**
     * Gets a Time from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Time getTime(String column, Object value, String search, int row, String table) throws SQLException {
        return getTime(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a Time from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Time getTime(String column, Object value, String search, String table) throws SQLException {
        return getTime(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets an UUID from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public UUID getUUID(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        UUID u = null;
        if (result.getResult().absolute(row)) {
            try {
                u = UUID.fromString(result.getResult().getString(search));
            } catch (Exception e) {
            }
        }
        result.close();
        return u;
    }

    /**
     * Gets an UUID from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public UUID getUUID(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getUUID(columns, values, search, 1, table);
    }

    /**
     * Gets an UUID from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public UUID getUUID(String column, Object value, String search, int row, String table) throws SQLException {
        return getUUID(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets an UUID from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public UUID getUUID(String column, Object value, String search, String table) throws SQLException {
        return getUUID(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a Object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param row     if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Object getObject(String[] columns, Object[] values, String search, int row, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");
        if (row < 1) throw new IllegalArgumentException("Rows values starts from 1");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        Object o = null;
        if (result.getResult().absolute(row)) {
            o = result.getResult().getObject(search);
        }
        result.close();
        return o;
    }

    /**
     * Gets a Object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Object getObject(String[] columns, Object[] values, String search, String table) throws SQLException {
        return getObject(columns, values, search, 1, table);
    }

    /**
     * Gets a Object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param row    if the research has more than one results, this is the number of the result you want (starts from 1)
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Object getObject(String column, Object value, String search, int row, String table) throws SQLException {
        return getObject(new String[]{column}, new Object[]{value}, search, row, table);
    }

    /**
     * Gets a Object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Object getObject(String column, Object value, String search, String table) throws SQLException {
        return getObject(new String[]{column}, new Object[]{value}, search, 1, table);
    }

    /**
     * Gets a ArrayList of Object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param type   the type of the ArrayList
     * @param table  the table to be used
     * @throws SQLException SQLException | ClassCastException
     */
    public <T> List<T> getList(String column, Object value, String search, Class<T> type, String table) throws SQLException {
        return getList(new String[]{column}, new Object[]{value}, search, type, table);
    }

    /**
     * Gets a ArrayList of Object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param type    the type of the ArrayList
     * @param table   the table to be used
     * @throws SQLException SQLException | ClassCastException
     */
    public <T> List<T> getList(String[] columns, Object[] values, String search, Class<T> type, String table) throws SQLException {
        if (columns.length != values.length)
            throw new IllegalArgumentException("Columns and values length must have the same value");

        StringBuilder query = new StringBuilder();
        query.append("SELECT `")
                .append(search)
                .append("` FROM `")
                .append(table)
                .append("` WHERE (");
        for (int i = 0; i < columns.length; i++) {
            if (values[i] == null) query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" IS NULL");
            else query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        CompositeResult result = executeQuery(query.toString(), table);
        List<T> list = new ArrayList<>();
        while (result.getResult().next()) {
            list.add(type.cast(result.getResult().getObject(search)));
        }
        result.close();
        return list;
    }

    /**
     * Gets the last Byte from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException | ClassCastException
     */
    public Byte getLastByte(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<Byte> list = getList(columns, values, search, Byte.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Byte from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Byte getLastByte(String column, Object value, String search, String table) throws SQLException {
        return getLastByte(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last Short from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException | ClassCastException
     */
    public Short getLastShort(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<Short> list = getList(columns, values, search, Short.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Short from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Short getLastShort(String column, Object value, String search, String table) throws SQLException {
        return getLastShort(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last Integer from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Integer getLastInteger(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<Integer> list = getList(columns, values, search, Integer.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Integer from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Integer getLastInteger(String column, Object value, String search, String table) throws SQLException {
        return getLastInteger(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last Long from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Long getLastLong(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<Long> list = getList(columns, values, search, Long.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Long from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Long getLastLong(String column, Object value, String search, String table) throws SQLException {
        return getLastLong(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last Float from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Float getLastFloat(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<Float> list = getList(columns, values, search, Float.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Float from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Float getLastFloat(String column, Object value, String search, String table) throws SQLException {
        return getLastFloat(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last Double from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Double getLastDouble(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<Double> list = getList(columns, values, search, Double.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Double from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Double getLastDouble(String column, Object value, String search, String table) throws SQLException {
        return getLastDouble(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last String from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public String getLastString(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<String> list = getList(columns, values, search, String.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last String from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public String getLastString(String column, Object value, String search, String table) throws SQLException {
        return getLastString(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last Boolean from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Boolean getLastBoolean(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<Boolean> list = getList(columns, values, search, Boolean.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Boolean from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Boolean getLastBoolean(String column, Object value, String search, String table) throws SQLException {
        return getLastBoolean(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last Timestamp from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Timestamp getLastTimestamp(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<Timestamp> list = getList(columns, values, search, Timestamp.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Timestamp from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Timestamp getLastTimestamp(String column, Object value, String search, String table) throws SQLException {
        return getLastTimestamp(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last Date from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException | ClassCastException
     */
    public Date getLastDate(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<Date> list = getList(columns, values, search, Date.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Date from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Date getLastDate(String column, Object value, String search, String table) throws SQLException {
        return getLastDate(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last Time from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException | ClassCastException
     */
    public Time getLastTime(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<Time> list = getList(columns, values, search, Time.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Time from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Time getLastTime(String column, Object value, String search, String table) throws SQLException {
        return getLastTime(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last UUID from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException | ClassCastException
     */
    public UUID getLastUUID(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<UUID> list = getList(columns, values, search, UUID.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last UUID from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public UUID getLastUUID(String column, Object value, String search, String table) throws SQLException {
        return getLastUUID(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last Object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public Object getLastObject(String[] columns, Object[] values, String search, String table) throws SQLException {
        List<Object> list = getList(columns, values, search, Object.class, table);
        return list.get(list.size() - 1);
    }

    /**
     * Gets the last Object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public Object getLastObject(String column, Object value, String search, String table) throws SQLException {
        return getLastObject(new String[]{column}, new Object[]{value}, search, table);
    }

    /**
     * Gets the last T object from the database.
     *
     * @param columns the list of columns for the research
     * @param values  the list of values to be searched in the columns
     * @param search  the name of the column whose value is wanted
     * @param type    the type of the object you want to get
     * @param table   the table to be used
     * @throws SQLException SQLException
     */
    public <T> T getLastObject(String[] columns, Object[] values, String search, Class<T> type, String table) throws SQLException {
        List<Object> list = getList(columns, values, search, Object.class, table);
        return type.cast(list.get(list.size() - 1));
    }

    /**
     * Gets the last T object from the database.
     *
     * @param column the column for the research
     * @param value  the value to be searched in the column
     * @param search the name of the column whose value is wanted
     * @param type   the type of the object you want to get
     * @param table  the table to be used
     * @throws SQLException SQLException
     */
    public <T> T getLastObject(String column, Object value, String search, Class<T> type, String table) throws SQLException {
        return getLastObject(new String[]{column}, new Object[]{value}, search, type, table);
    }

    /**
     * Update a list of columns with new values.
     *
     * @param columnsToEdit the list of columns to edit
     * @param newValues     the list of new values
     * @param columns       the list of columns for the research
     * @param values        the list of values to be searched in the columns
     * @param table         the table to be used
     * @throws SQLException SQLException
     */
    public void set(String[] columnsToEdit, Object[] newValues, String[] columns, Object[] values, String table) throws SQLException {
        if ((columns.length != values.length) || (columnsToEdit.length != newValues.length))
            throw new IllegalArgumentException("Columns and values length must have the same value");

        StringBuilder query = new StringBuilder();
        query.append("UPDATE `")
                .append(table)
                .append("` SET ");
        for (int i = 0; i < columnsToEdit.length; i++) {
            query.append("`")
                    .append(columnsToEdit[i])
                    .append("`")
                    .append(" ='");
            query.append(objectToString(newValues[i])).append("' ");
            if (i != columnsToEdit.length - 1) query.append(", ");
        }
        query.append("WHERE (");
        for (int i = 0; i < columns.length; i++) {
            query.append("`")
                    .append(columns[i])
                    .append("`")
                    .append(" = '")
                    .append(objectToString(values[i]))
                    .append("'");
            if (i != columns.length - 1) query.append(" AND ");
        }
        query.append(");");

        executeUpdate(query.toString(), table);
    }

    /**
     * Update a list of columns with new values.
     *
     * @param columnToEdit the column to edit
     * @param newValue     the new value
     * @param columns      the list of columns for the research
     * @param values       the list of values to be searched in the columns
     * @param table        the table to be used
     * @throws SQLException SQLException
     */
    public void set(String columnToEdit, Object newValue, String[] columns, Object[] values, String table) throws SQLException {
        set(new String[]{columnToEdit}, new Object[]{newValue}, columns, values, table);
    }

    /**
     * Update a list of columns with new values.
     *
     * @param columnsToEdit the list of columns to edit
     * @param newValues     the list of new values
     * @param column        the column for the research
     * @param value         the value to be searched in the column
     * @param table         the table to be used
     * @throws SQLException SQLException
     */
    public void set(String[] columnsToEdit, Object[] newValues, String column, Object value, String table) throws SQLException {
        set(columnsToEdit, newValues, new String[]{column}, new Object[]{value}, table);
    }

    /**
     * Update a list of columns with new values.
     *
     * @param columnToEdit the column to edit
     * @param newValues    the new value
     * @param column       the column for the research
     * @param value        the value to be searched in the column
     * @param table        the table to be used
     * @throws SQLException SQLException
     */
    public void set(String columnToEdit, Object newValues, String column, Object value, String table) throws SQLException {
        set(new String[]{columnToEdit}, new Object[]{newValues}, new String[]{column}, new Object[]{value}, table);
    }
}
