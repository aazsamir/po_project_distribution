package com.Distribution.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Vector;

import com.Distribution.Indicator.Indicator;
import com.Distribution.Input.Input;
import com.Distribution.Input.InputCollection;
import com.Distribution.Target.Target;
import com.Distribution.Target.TargetCollection;

public class Repository {
    private Database database;
    private Faker faker;

    public Repository(
            Database database) {
        this.database = database;
        this.faker = new Faker();
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
                        roughness DOUBLE,
                        time DOUBLE,
                        cost DOUBLE,
                        people DOUBLE,
                        is_special TINYINT,
                        FOREIGN KEY (id_target) REFERENCES targets(id)
                    )
                """);

        this.seed();
    }

    private void seed() throws SQLException {
        TargetCollection targetCollection = this.faker.mockTestTargetCollection(5);
        Target[] targets = targetCollection.getTargets();

        for (Target target : targets) {
            updateTarget(target);

            for (Input input : target.getInputCollection().getInputs()) {
                updateInput(input);
            }
        }
    }

    public void updateTarget(Target target) throws SQLException {
        boolean isExists = this.database.select(String.format(Locale.US,
                """
                            SELECT id FROM targets WHERE id = %d
                        """,
                target.getId())).next();

        if (isExists) {
            this.database.query(String.format(Locale.US,
                    """
                                UPDATE targets SET name = '%s' WHERE id = '%s'
                            """,
                    target.getName(), target.getId()));
        } else {
            this.database.query(String.format(Locale.US,
                    """
                                INSERT INTO targets (id, name) VALUES (%d, '%s')
                            """,
                    target.getId(), target.getName()));
        }
    }

    public void updateInput(Input input) throws SQLException {
        boolean isExists = this.database.select(String.format(Locale.US,
                """
                            SELECT id FROM inputs WHERE id = %s
                        """,
                input.getId())).next();

        if (isExists) {
            this.database.query(String.format(Locale.US,
                    """
                                UPDATE inputs SET
                                    id_target = %d,
                                    roughness = %f,
                                    time = %f,
                                    cost = %f,
                                    people = %f,
                                    is_special = %d
                                WHERE id = %d
                            """,
                    input.getIdTarget(),
                    input.getIndicator().getRoughness(),
                    input.getIndicator().getTime(),
                    input.getIndicator().getCost(),
                    input.getIndicator().getPeople(),
                    input.getIndicator().isSpecial() ? 1 : 0,
                    input.getId()));
        } else {
            this.database.query(String.format(Locale.US,
                    """
                                        INSERT INTO inputs (
                                            id_target,
                                            roughness,
                                            time,
                                            cost,
                                            people,
                                            is_special
                                        ) VALUES (
                                            %d,
                                            %f,
                                            %f,
                                            %f,
                                            %f,
                                            %d
                                        )
                            """,
                    input.getIdTarget(),
                    input.getIndicator().getRoughness(),
                    input.getIndicator().getTime(),
                    input.getIndicator().getCost(),
                    input.getIndicator().getPeople(),
                    input.getIndicator().isSpecial() ? 1 : 0));
        }
    }

    public TargetCollection findTargets() throws SQLException {
        ResultSet results = this.database.select("""
                    SELECT
                        id,
                        name
                    FROM
                        targets
                """);

        Target[] targets = new Target[getFetchSize(results)];
        InputCollection temInputCollection;

        while (results.next()) {
            temInputCollection = findInputsByIdTarget(results.getInt("id"));
            targets[results.getRow() - 1] = new Target(
                    results.getInt("id"),
                    results.getString("name"),
                    temInputCollection);
        }

        return new TargetCollection(targets);
    }

    public InputCollection findInputsByIdTarget(Integer idTarget) throws SQLException {
        ResultSet results = this.database.select(String.format(Locale.US,
                """
                            SELECT
                                id,
                                id_target,
                                roughness,
                                time,
                                cost,
                                people,
                                is_special
                            FROM
                                inputs
                            WHERE
                                id_target = %d
                        """,
                idTarget));

        Vector<Input> inputsVector = new Vector<Input>();
        Indicator tempIndicator;
        int i = 0;

        while (results.next()) {
            tempIndicator = new Indicator(
                    results.getDouble("roughness"),
                    results.getDouble("time"),
                    results.getDouble("cost"),
                    results.getDouble("people"),
                    results.getBoolean("is_special"));

            inputsVector.add(new Input(
                    tempIndicator,
                    results.getInt("id"),
                    results.getInt("id_target")));
        }

        Input[] inputs = inputsVector.toArray();

        return new InputCollection(inputs.toArray());
    }

    public void close() throws SQLException {
        this.database.close();
    }
}
