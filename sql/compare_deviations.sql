WITH min_iteration AS (
    SELECT
        MIN(id_iteration) AS min_iteration
    FROM
        (
            SELECT
                id_iteration
            FROM
                inputs
            ORDER BY
                id_iteration DESC
            LIMIT
                100
        ) cte
), averages AS (
    SELECT
        AVG(roughness) AS roughness,
        AVG(time) AS time,
        AVG(cost) AS cost,
        AVG(people) AS people,
        SUM(is_special) / COUNT(id_target) AS is_special,
        COUNT(*) / COUNT(id_target) AS count
    FROM
        inputs
    WHERE
        id_iteration >= (
            SELECT
                min_iteration
            FROM
                min_iteration
        )
)
SELECT
    id_target,
    SQRT(
        ABS(
            AVG(roughness) - (
                SELECT
                    roughness
                FROM
                    averages
            )
        )
    ),
    SQRT(
        ABS(
            AVG(time) - (
                SELECT
                    time
                FROM
                    averages
            )
        )
    ),
    SQRT(
        ABS(
            AVG(cost) - (
                SELECT
                    cost
                FROM
                    averages
            )
        )
    ),
    SQRT(
        ABS(
            AVG(people) - (
                SELECT
                    people
                FROM
                    averages
            )
        )
    ),
    SQRT(
        ABS(
            SUM(is_special) - (
                SELECT
                    is_special
                FROM
                    averages
            )
        )
    ),
    SQRT(
        ABS(
            COUNT(*) - (
                SELECT
                    count
                FROM
                    averages
            )
        )
    )
FROM
    inputs
WHERE
    id_iteration >= (
        SELECT
            min_iteration
        FROM
            min_iteration
    )
GROUP BY
    id_target;