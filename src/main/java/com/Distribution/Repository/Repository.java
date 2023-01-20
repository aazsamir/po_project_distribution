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

    public void migrateIfEmpty() throws SQLException {
        boolean isEmpty = !(this.database.isTableExists("targets") && this.database.isTableExists("inputs"));

        if (isEmpty) {
            System.out.println("migrate!");
            this.migrate();
        }

        return;
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
        TargetCollection targetCollection = this.faker.mockTestTargetCollection(2);
        Target[] targets = targetCollection.getTargets();

        for (Target target : targets) {
            this.updateTarget(target);

            for (Input input : target.getInputCollection().getInputs()) {
                this.updateInput(input);
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
        boolean isExists = input.getId() != null && this.database.select(String.format(Locale.US,
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

        Vector<Target> vectorTargets = new Vector<Target>();
        InputCollection tempInputCollection;

        while (results.next()) {
            tempInputCollection = this.findInputsByIdTarget(results.getInt("id"));

            vectorTargets.add(new Target(
                    results.getInt("id"),
                    results.getString("name"),
                    tempInputCollection));
        }

        Target[] targets = new Target[vectorTargets.size()];

        for (int i = 0; i < vectorTargets.size(); i++) {
            targets[i] = vectorTargets.elementAt(i);
        }

        return new TargetCollection(targets);
    }

    public InputCollection findInputsByIdTarget(Integer idTarget) throws SQLException {
        ResultSet result = this.database.select(String.format(Locale.US,
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

        while (result.next()) {
            tempIndicator = new Indicator(
                    result.getDouble("roughness"),
                    result.getDouble("time"),
                    result.getDouble("cost"),
                    result.getDouble("people"),
                    result.getBoolean("is_special"));

            inputsVector.add(new Input(
                    tempIndicator,
                    result.getInt("id"),
                    result.getInt("id_target")));
        }

        Input[] inputs = new Input[inputsVector.size()];

        for (int i = 0; i < inputsVector.size(); i++) {
            inputs[i] = inputsVector.elementAt(i);
        }

        return new InputCollection(inputs);
    }

    public void close() throws SQLException {
        this.database.close();
    }
}
