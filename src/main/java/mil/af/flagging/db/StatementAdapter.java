/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 *
 * @author Brad
 */
public class StatementAdapter implements Statement {

    private final Statement st;

    public StatementAdapter(Statement st) {
        this.st = st;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return st.executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return st.executeUpdate(sql);
    }

    @Override
    public void close() throws SQLException {
        st.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return st.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        st.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return st.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        st.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        st.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return st.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        st.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        st.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return st.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        st.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        st.setCursorName(name);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return st.execute(sql);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return st.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return st.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return st.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        st.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return st.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        st.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return st.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return st.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return st.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        st.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        st.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return st.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return st.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return st.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return st.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return st.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return st.executeUpdate(sql, columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return st.executeUpdate(sql, columnNames);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return st.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return st.execute(sql, columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return st.execute(sql, columnNames);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return st.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return st.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        st.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return st.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        st.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return st.isCloseOnCompletion();
    }

    @Override
    public long getLargeUpdateCount() throws SQLException {
        return st.getLargeUpdateCount();
    }

    @Override
    public void setLargeMaxRows(long max) throws SQLException {
        st.setLargeMaxRows(max);
    }

    @Override
    public long getLargeMaxRows() throws SQLException {
        return st.getLargeMaxRows();
    }

    @Override
    public long[] executeLargeBatch() throws SQLException {
        return st.executeLargeBatch();
    }

    @Override
    public long executeLargeUpdate(String sql) throws SQLException {
        return st.executeLargeUpdate(sql);
    }

    @Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return st.executeLargeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return st.executeLargeUpdate(sql, columnIndexes);
    }

    @Override
    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
        return st.executeLargeUpdate(sql, columnNames);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return st.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return st.isWrapperFor(iface);
    }

    @Override
    public int hashCode() {
        return st.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return st.equals(obj);
    }

    @Override
    public String toString() {
        return st.toString();
    }

}