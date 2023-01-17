package com.Distribution.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection;
    private final int TIMEOUT = 30;

    public Database() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
    }

    public void query(String query) throws SQLException {
        try {
            Statement statement = this.getStatement();
            statement.executeUpdate(query);
        } catch (SQLException sqlException) {
            throw createException(sqlException, query);
        }
    }

    public ResultSet select(String query) throws SQLException {
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

    public static void main(String[] args) {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:distribution.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
}