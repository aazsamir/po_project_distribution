package com.Distribution.Repository;

import java.sql.SQLException;

public class Repository {
    private Database database;

    public Repository(
            Database database) {
        this.database = database;
    }

    public void migrate() throws SQLException {
        this.database.query("""
                    DROP TABLE IF EXISTS targets
                """);
        this.database.query("""
                    CREATE TABLE targets (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name VARCHAR(255)
                    )
                """);
        this.database.query("""
                    DROP TABLE IF EXISTS inputs
                """);
        this.database.query("""
                    CREATE TABLE inputs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        id_target INTEGER,
                        rougness DOUBLE,
                        time DOUBLE,
                        cost DOUBLE,
                        people DOUBLE,
                        is_special TINYINT,
                        FOREIGN KEY (id_target) REFERENCES targets(id)
                    )
                """);
    }

    public void close() throws SQLException {
        this.database.close();
    }
}
