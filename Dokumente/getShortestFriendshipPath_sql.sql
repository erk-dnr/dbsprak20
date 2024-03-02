WITH RECURSIVE Friendship (fromID, toID, distance, path, cycle) AS (
	SELECT pkp.personOne, pkp.personTwo, 0, array[pkp.personOne], false
	FROM pkp_symmetric pkp
	WHERE pkp.personOne = 15393162788948 -- first parameter (from)

	UNION

	SELECT pkp.personOne, pkp.personTwo, F.distance+1, path || pkp.personOne, pkp.personOne = ANY(path)
	FROM Friendship F, pkp_symmetric pkp
	WHERE F.toID = pkp.personOne
		AND NOT cycle AND distance < 5 -- hardcoded
),

friendship_paths AS (
	SELECT DISTINCT distance, path
	FROM Friendship
	WHERE fromID = 2199023255625 -- second parameter (to)
)
SELECT *
FROM friendship_paths
WHERE distance <= ALL (SELECT distance FROM friendship_paths)