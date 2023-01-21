SELECT
    id_target,
    AVG(roughness),
    AVG(time),
    AVG(cost),
    AVG(people),
    SUM(is_special),
    COUNT(*)
FROM
    inputs
WHERE
    id_iteration >= (
        SELECT
            MIN(id_iteration)
        FROM (
                SELECT
                    id_iteration
                FROM
                    inputs
                ORDER BY
                    id_iteration DESC
                LIMIT 100
            ) cte
        )
GROUP BY
    id_target;