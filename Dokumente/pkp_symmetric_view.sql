CREATE VIEW pkp_symmetric AS
	SELECT *
	FROM knows
	UNION
	SELECT personTwo AS personOne, personOne AS personTwo, creationDate
	FROM knows;