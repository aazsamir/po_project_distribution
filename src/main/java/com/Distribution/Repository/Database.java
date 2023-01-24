package com.Distribution.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

public class Database {
    private Connection connection;
    private final int TIMEOUT = 30;
    private boolean isDebug = false;

    public Database(String database) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + database);
    }

    public Database(String database, boolean isDebug) throws SQLException {
        this(database);
        this.isDebug = isDebug;
    }

    public void query(String query) throws SQLException {
        if (this.isDebug) {
            System.out.println("======");
            System.out.println(query);
            System.out.println("======");
        }

        try {
            Statement statement = this.getStatement();
            statement.executeUpdate(query);
        } catch (SQLException sqlException) {
            throw createException(sqlException, query);
        }
    }

    public ResultSet select(String query) throws SQLException {
        if (this.isDebug) {
            System.out.println("======");
            System.out.println(query);
            System.out.println("======");
        }

        Statement statement = this.getStatement();

        try {
            return statement.executeQuery(query);
        } catch (SQLException sqlException) {
            throw createException(sqlException, query);
        }
    }

    private SQLException createException(SQLException sqlException, String query) {
        return new SQLException(sqlException.getMessage() + " query:\n " + query);
    }

    public void close() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
    }

    private Statement getStatement() throws SQLException {
        Statement statement = this.connection.createStatement();
        statement.setQueryTimeout(this.TIMEOUT);

        return statement;
    }

    public boolean isTableExists(String table) throws SQLException {
        ResultSet result = select(String.format(Locale.US, """
                    SELECT
                        *
                    FROM
                        sqlite_master
                    WHERE
                        type = 'table'
                        AND name = '%s'
                """,
                table));

        while (result.next()) {
            return true;
        }

        return false;
    }
}