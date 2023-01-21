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
    private final int TARGET_COUNT = 3;
    private Integer idIteration = null;
    private final int lastIterations = 10;

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
                        id_iteration INTEGER,
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
        TargetCollection targetCollection = this.faker.mockTestTargetCollection(TARGET_COUNT);
        Target[] targets = targetCollection.getTargets();

        for (Target target : targets) {
            this.updateTarget(target);

            for (Input input : target.getInputCollection().getInputs()) {
                this.updateInput(input);
            }
        }
    }

    public void updateTargetCollectionWithInputs(TargetCollection targetCollection) throws SQLException {
        for (Target target : targetCollection.getTargets()) {
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
                                            id_iteration,
                                            roughness,
                                            time,
                                            cost,
                                            people,
                                            is_special
                                        ) VALUES (
                                            %d,
                                            %d,
                                            %f,
                                            %f,
                                            %f,
                                            %f,
                                            %d
                                        )
                            """,
                    input.getIdTarget(),
                    this.getIdIteration(),
                    input.getIndicator().getRoughness(),
                    input.getIndicator().getTime(),
                    input.getIndicator().getCost(),
                    input.getIndicator().getPeople(),
                    input.getIndicator().isSpecial() ? 1 : 0));
        }
    }

    private Integer getIdIteration() throws SQLException {
        if (this.idIteration == null) {
            ResultSet result = this.database.select("""
                    SELECT
                        MAX(id_iteration) AS max
                    FROM
                        inputs
                    """);

            this.idIteration = 0;

            while (result.next()) {
                this.idIteration = result.getInt("max");
            }

            this.idIteration += 1;
        }

        return this.idIteration;
    }

    public TargetCollection findTargets() throws SQLException {
        ResultSet results = this.database.select("""
                    SELECT
                        id,
                        name
                    FROM
                        targets
                    ORDER BY
                        random()
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
                                AND id_iteration >= %d
                        """,
                idTarget, this.getLastConsideredIdIteration()));

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

    private int getLastConsideredIdIteration() throws SQLException {
        ResultSet result = this.database.select(String.format("""
                    SELECT
                        id_iteration
                    FROM
                        inputs
                    ORDER BY
                        id_iteration DESC
                    LIMIT
                        %d
                """,
                this.lastIterations));

        int lastIteration = 0;

        while (result.next()) {
            lastIteration = result.getInt("id_iteration");
        }

        return lastIteration;
    }

    public void close() throws SQLException {
        this.database.close();
    }
}
