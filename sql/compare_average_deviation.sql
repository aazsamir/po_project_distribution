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
),
average_deviations AS (
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
        ) AS roughness,
        SQRT(
            ABS(
                AVG(time) - (
                    SELECT
                        time
                    FROM
                        averages
                )
            )
        ) AS time,
        SQRT(
            ABS(
                AVG(cost) - (
                    SELECT
                        cost
                    FROM
                        averages
                )
            )
        ) AS cost,
        SQRT(
            ABS(
                AVG(people) - (
                    SELECT
                        people
                    FROM
                        averages
                )
            )
        ) AS people,
        SQRT(
            ABS(
                SUM(is_special) - (
                    SELECT
                        is_special
                    FROM
                        averages
                )
            )
        ) AS is_special,
        SQRT(
            ABS(
                COUNT(*) - (
                    SELECT
                        count
                    FROM
                        averages
                )
            )
        ) AS count
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
        id_target
)
SELECT
    AVG(roughness),
    AVG(time),
    AVG(cost),
    AVG(people),
    AVG(is_special),
    AVG(count)
FROM
    average_deviations;