package me.buzz.woolwars.game.data.mysql.utils;

import java.io.InputStream;
import java.sql.*;
import java.util.UUID;

public class CompositeResult implements AutoCloseable {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet result;
    private String query;

    public CompositeResult(Connection connection, PreparedStatement statement, ResultSet result, String query) {
        this.connection = connection;
        this.statement = statement;
        this.result = result;
        this.query = query;
    }

    /**
     * Closes the ResultSet, the Statement and the Connection.
     */
    public void close() {
        DBUtils.closeQuietly(connection, statement, result);
    }

    /**
     * Return the query sent to database.
     *
     * @return
     */
    public String getQuery() {
        return query;
    }

    /**
     * Return the ResultSet of the query.
     *
     * @return the ResultSet of the query
     */
    public ResultSet getResult() {
        return result;
    }

    /**
     * Return the PreparedStatement of the query.
     *
     * @return the PreparedStatement of the query
     */
    public PreparedStatement getStatement() {
        return statement;
    }

    /**
     * Return the Connection of the query.
     *
     * @return the Connection of the query
     */
    public Connection getConnection() {
        return connection;
    }

    public String getString(int columnIndex) throws SQLException {
        return result.getString(columnIndex);
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        return result.getBoolean(columnIndex);
    }

    public byte getByte(int columnIndex) throws SQLException {
        return result.getByte(columnIndex);
    }

    public short getShort(int columnIndex) throws SQLException {
        return result.getShort(columnIndex);
    }

    public int getInt(int columnIndex) throws SQLException {
        return result.getInt(columnIndex);
    }

    public long getLong(int columnIndex) throws SQLException {
        return result.getLong(columnIndex);
    }

    public float getFloat(int columnIndex) throws SQLException {
        return result.getFloat(columnIndex);
    }

    public double getDouble(int columnIndex) throws SQLException {
        return result.getDouble(columnIndex);
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        return result.getBytes(columnIndex);
    }

    public Date getDate(int columnIndex) throws SQLException {
        return result.getDate(columnIndex);
    }

    public Time getTime(int columnIndex) throws SQLException {
        return result.getTime(columnIndex);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return result.getTimestamp(columnIndex);
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return result.getAsciiStream(columnIndex);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return result.getBinaryStream(columnIndex);
    }

    public Object getObject(int columnIndex) throws SQLException {
        return result.getObject(columnIndex);
    }

    public String getString(String columnLabel) throws SQLException {
        return result.getString(columnLabel);
    }

    public boolean getBoolean(String columnLabel) throws SQLException {
        return result.getBoolean(columnLabel);
    }

    public byte getByte(String columnLabel) throws SQLException {
        return result.getByte(columnLabel);
    }

    public short getShort(String columnLabel) throws SQLException {
        return result.getShort(columnLabel);
    }

    public int getInt(String columnLabel) throws SQLException {
        return result.getInt(columnLabel);
    }

    public long getLong(String columnLabel) throws SQLException {
        return result.getLong(columnLabel);
    }

    public float getFloat(String columnLabel) throws SQLException {
        return result.getFloat(columnLabel);
    }

    public double getDouble(String columnLabel) throws SQLException {
        return result.getDouble(columnLabel);
    }

    public byte[] getBytes(String columnLabel) throws SQLException {
        return result.getBytes(columnLabel);
    }

    public Date getDate(String columnLabel) throws SQLException {
        return result.getDate(columnLabel);
    }

    public Time getTime(String columnLabel) throws SQLException {
        return result.getTime(columnLabel);
    }

    public UUID getUUID(String columnLabel) throws SQLException {
        try {
            return UUID.fromString(result.getString(columnLabel));
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return result.getTimestamp(columnLabel);
    }

    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return result.getAsciiStream(columnLabel);
    }

    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return result.getBinaryStream(columnLabel);
    }

    public Object getObject(String columnLabel) throws SQLException {
        return result.getObject(columnLabel);
    }

    public boolean absolute(int row) throws SQLException {
        return result.absolute(row);
    }

    public boolean next() throws SQLException {
        return result.next();
    }
}
