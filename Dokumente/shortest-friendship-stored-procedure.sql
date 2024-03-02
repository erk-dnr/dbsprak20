CREATE OR REPLACE FUNCTION shortest_friendship_path(pid1 bigint, pid2 bigint)
	RETURNS TABLE (distance integer, path bigint[])
AS $$
BEGIN
	RETURN QUERY

	WITH RECURSIVE Friendship (node, distance, path, cycle) AS (
		SELECT pkp.personTwo, 1, array[pkp.personOne, pkp.personTwo], false
		FROM pkp_symmetric pkp
		WHERE pkp.personOne = $1 -- pid1

		UNION ALL

		SELECT pkp.personTwo, F.distance + 1, F.path || pkp.personTwo, pkp.personTwo = ANY(F.path)
		FROM Friendship F, pkp_symmetric pkp
		WHERE F.node = pkp.personOne 
			AND F.node <> $2 -- pid2
			AND NOT cycle 
			AND F.distance < 6
	),
	
	friendship_paths AS (
	SELECT DISTINCT F.distance, F.path
	FROM Friendship F
	WHERE F.node = $2 -- pid2
	)

	SELECT *
	FROM friendship_paths fp
	WHERE fp.distance <= ALL (SELECT fp.distance FROM friendship_paths fp);

END;
$$ LANGUAGE plpgsql;