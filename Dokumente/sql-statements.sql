-- 1. In wie vielen verschiedenen afrikanischen Städten gibt es eine Universität?

SELECT COUNT(*) AS number 
FROM (
	SELECT DISTINCT city.id 
	FROM city INNER JOIN country ON city.isPartOf = country.id
			INNER JOIN continent ON country.isPartOf = continent.id
			INNER JOIN place ON continent.id = place.id
			INNER JOIN university ON city.id = university.place
	WHERE place.name = 'Africa'
) AS temp

-- Ergebnis: 1 Zeile


-- 2. Wie viele Forenbeiträge (Posts) hat die älteste Person verfasst (Ausgabe: Name, #Forenbeiträge)?

SELECT CONCAT(person.firstName, ' ', person.lastName) AS name, COUNT(post) AS number_of_posts
FROM post FULL OUTER JOIN person ON post.creator = person.id
WHERE person.birthday <= (
	SELECT MIN(person.birthday) 
	FROM person
)
GROUP BY name

-- Ergebnis: 1 Zeile


-- 3. Wie viele Kommentare zu Posts gibt es aus jedem Land 
-- (Ausgabe aufsteigend sortiert nach Kommentaranzahl)? 
-- Die Liste soll auch Länder enthalten, für die keine Post-Kommentare existieren,
-- d.h. die Kommentaranzahl = 0 ist! (Funktion Coalesce)

SELECT place.name AS country, count(comment.replyofpost) AS number_of_comments
FROM country INNER JOIN place ON country.id = place.id
	FULL OUTER JOIN comment ON country.id = comment.place
GROUP BY place.name
ORDER BY number_of_comments

-- Ergebnis: 111 Zeilen


-- 4. Aus welchen Städten stammen die meisten Nutzer (Ausgabe Name + Einwohnerzahl)?

WITH city_summary AS (
	SELECT place.name, COUNT(person) AS number_of_users
	FROM person INNER JOIN city ON person.place = city.id 
				INNER JOIN place ON city.id = place.id
	GROUP BY place.name
)

SELECT *
FROM city_summary
WHERE number_of_users >= (
	SELECT MAX(number_of_users) 
	FROM city_summary
)

-- Ergebnis: 2 Zeilen


-- 5. Mit wem ist ‘Hans Johansson’ befreundet?

WITH friends_ids AS (
	SELECT pkp_symmetric.personTwo AS id 
	FROM person INNER JOIN pkp_symmetric ON person.id = pkp_symmetric.personOne
	WHERE person.firstname = 'Hans' AND person.lastname = 'Johansson'
)

SELECT CONCAT(person.firstname, ' ', person.lastname) AS friend 
FROM person INNER JOIN friends_ids ON person.id = friends_ids.id


--Ergebnis: 12 Zeilen


-- 6. Wer sind die “echten” Freundesfreunde von ‘Hans Johansson’? 
-- “Echte” Freundesfreunde dürfen nicht gleichzeitig direkte Freunde von ‘Hans Johansson’ sein. 
-- Sortieren Sie die Ausgabe alphabetisch nach dem Nachnamen.

WITH RECURSIVE friends_of_hans_johansson (personOne, personTwo, distance) AS (
	SELECT pkp.personOne, pkp.personTwo, 0
	FROM pkp_symmetric pkp INNER JOIN person ON pkp.personOne = person.id
	WHERE person.firstName = 'Hans' AND person.lastName = 'Johansson'
	
	UNION
	
	SELECT pkp.personOne, pkp.personTwo, fr.distance + 1
	FROM friends_of_hans_johansson fr INNER JOIN pkp_symmetric pkp ON pkp.personOne = fr.personTwo
	WHERE distance < (
		SELECT COUNT(*) 
		FROM person
	)
) 

SELECT p.id, p.firstName, p.lastName
FROM (
	SELECT fr.personOne AS friend, MIN(fr.distance) AS distance
	FROM friends_of_hans_johansson fr
	GROUP BY friend
	HAVING MIN(fr.distance) > 1
) temp, person p
WHERE temp.friend = p.id
ORDER BY p.lastName

-- Ergebnis: 60 Zeilen


-- 7. Welche Nutzer sind Mitglied in allen Foren, in denen auch ‘Mehmet Koksal’ Mitglied ist (Angabe Name)? 

SELECT pn.firstname, pn.lastname
FROM person pn
WHERE NOT EXISTS (
	SELECT * 
	FROM hasmember INNER JOIN person pn2 ON hasmember.person = pn2.id
	WHERE pn2.firstname = 'Mehmet' 
		AND pn2.lastname = 'Koksal' 
		AND forum NOT IN (
			SELECT forum
			FROM hasMember hm
			WHERE pn.id = hm.person
		)
)
	AND pn.firstname <> 'Mehmet'
	AND pn.lastname <> 'Koksal'

-- Ergebnis: 3 Zeilen


-- 8. Geben Sie die prozentuale Verteilung der Nutzer bzgl. ihrer Herkunft aus verschiedenen Kontinenten an!

SELECT place.name, (ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM person), 2)) AS percentage
FROM person INNER JOIN city ON person.place = city.id
		INNER JOIN country ON city.isPartOf = country.id
		INNER JOIN continent ON country.isPartOf = continent.id
		INNER JOIN place ON continent.id = place.id
GROUP BY place.name
ORDER BY percentage DESC

-- Ergebnis: 5 Zeilen
 	

-- 9. Zu welchen Themen (‘tag classes’) gibt es die meisten Posts? 
-- Geben Sie die Namen der Top 10 ‘tag classes’ mit ihrer Häufigkeit aus!

SELECT tagclass.name AS tag_class, COUNT(*) AS number_of_posts_with_this_tag
FROM posthastag
	NATURAL JOIN hasType
	INNER JOIN tagclass ON hastype.tagclass = tagclass.id
GROUP BY tagclass.name
ORDER BY number_of_posts_with_this_tag DESC
LIMIT 10 

-- Ergebnis: 10 Zeilen 	


-- 10. Welche Personen haben noch nie ein “Like” für einen Kommentar oder Post bekommen? 
-- Sortieren Sie die Ausgabe alphabetisch nach dem Nachnamen.

WITH messages AS (
	SELECT id, creator
	FROM post
	UNION
	SELECT id, creator
	FROM comment
),
likedMessages AS (
	SELECT post AS id
	FROM likesPost
	UNION
	SELECT comment AS id
	FROM likesComment
),
personWithoutMessages AS (
	SELECT p.id, COUNT(m)
	FROM messages m RIGHT JOIN person p ON m.creator = p.id
	GROUP BY p.id
	HAVING COUNT(m) = 0
)
	
SELECT id, firstName, lastName
FROM (
	SELECT p.*
	FROM messages m INNER JOIN person p ON m.creator = p.id
	WHERE NOT EXISTS(
		SELECT *
		FROM messages m2
		WHERE m2.creator = p.id 
			AND m2.id IN (
				SELECT id
				FROM likedMessages
			)
	)
	EXCEPT
	-- Personen, die keine Kommentare oder Posts geschrieben haben
	SELECT p.*
	FROM person p
	WHERE p.id IN (
		SELECT id
		FROM personWithoutMessages
	)
) temp
ORDER BY lastName

-- Ergebnis: 27 Zeilen


-- 11. Welche Foren enthalten mehr Posts als die durchschnittliche Anzahl von Posts in Foren 
-- (Ausgabe alphabetisch sortiert nach Forumtitel)?

WITH summary AS (
	SELECT forum, count (id) AS sum_posts 
	FROM post 
	GROUP BY forum
)

SELECT forum.title 
FROM summary INNER JOIN forum ON summary.forum = forum.id 
WHERE sum_posts > (
	SELECT AVG(sum_posts) 
	FROM summary
)
ORDER BY forum.title

-- Ergebnis: 329 Zeilen


-- 12. Welche Personen sind mit der Person befreundet, die die meisten Likes auf einen Post bekommen hat? 
-- Sortieren Sie die Ausgabe alphabetisch nach dem Nachnamen.

WITH postLikesAggregation AS (
	SELECT post.*, COUNT(DISTINCT likesPost) AS number_of_likes
	FROM post INNER JOIN likesPost ON post.id = likesPost.post
	GROUP BY post.id
),
mostLikedPost AS (
	SELECT *
	FROM postLikesAggregation
	WHERE number_of_likes = (
		SELECT MAX(number_of_likes)
		FROM postLikesAggregation
	)
)

SELECT p.id, p.firstName, p.lastName
FROM person p, pkp_symmetric pkp
WHERE p.id = pkp.personOne 
	AND pkp.personTwo IN (
		SELECT p.id
		FROM mostLikedPost mlp INNER JOIN person p ON mlp.creator = p.id
	)
ORDER BY p.lastName

-- Ergebnis: 23 Zeilen


-- 13. Welche Personen sind direkt oder indirekt mit ‘Jun Hu’ (id 94) verbunden (befreundet)? 
-- Geben Sie für jede Person die Distanz zu Jun an. 

WITH RECURSIVE friend (personOne, personTwo, distance) AS (
	SELECT personOne, personTwo, 0
	FROM pkp_symmetric
	WHERE personOne = 94

	UNION

	SELECT ks.personOne, ks.personTwo, friend.distance + 1
	FROM pkp_symmetric ks INNER JOIN friend ON ks.personone = friend.persontwo
	WHERE distance < (SELECT COUNT(*) FROM person)	
) 

SELECT * 
FROM (
	SELECT CONCAT(person.firstname, ' ', person.lastname) AS friend, MIN(distance) AS min_distance
	FROM friend INNER JOIN person ON friend.personone = person.id
	WHERE person.id != 94
	GROUP BY friend
) temp
ORDER BY min_distance, friend

-- Ergebnis: 72 Zeilen


-- 14. Erweitern Sie die Anfrage zu Aufgabe 13 indem Sie zusätzlich zur Distanz den Pfad zwischen den Nutzern ausgeben.

-- entspricht 'friend' aus 13.
WITH RECURSIVE friend13 (personOne, personTwo, distance) AS (
	SELECT personOne, personTwo, 0
	FROM pkp_symmetric
	WHERE personOne = 94

	UNION

	SELECT ks.personOne, ks.personTwo, friend13.distance + 1
	FROM pkp_symmetric ks INNER JOIN friend13 ON ks.personone = friend13.persontwo
	WHERE distance < (SELECT COUNT(*) FROM person)	
),

-- Erweiterung von 'friend' um Pfade zwischen Personen anzugeben
friend14 (personOne, personTwo, distance, path, cycle) AS (
	SELECT personOne, personTwo, 0, array[personOne], false
	FROM pkp_symmetric
	WHERE personOne = 94

	UNION

	SELECT ks.personOne, ks.personTwo, friend14.distance + 1, path || ks.personOne, ks.personOne = ANY(path)
	FROM pkp_symmetric ks INNER JOIN friend14 ON ks.personone = friend14.persontwo
	WHERE NOT cycle
		AND distance < (
			-- gibt die größte Distanz zurück, die in Anfrage 13 aufgetreten ist
			SELECT MAX(min_distance)
			FROM (
				SELECT person.id, MIN(distance) AS min_distance
				FROM friend13 INNER JOIN person ON friend13.personone = person.id
				WHERE person.id != 94
				GROUP BY person.id
			) temp
		) 
) 

SELECT temp.friend, MIN(temp.min_distance) as min_distance, friend14.path 
FROM friend14 INNER JOIN (
	SELECT person.id, CONCAT(person.firstname, ' ', person.lastname) AS friend, MIN(distance) AS min_distance
	FROM friend14 INNER JOIN person ON friend14.personone = person.id
	WHERE person.id != 94
	GROUP BY person.id, friend
) temp ON friend14.personOne = temp.id
WHERE temp.min_distance = friend14.distance
GROUP BY temp.friend, friend14.path
ORDER BY min_distance, friend

-- Ergebnis: 217 Zeilen


-- Alternative zu 14.:
-- selbes Ergebnis, allerdings übersichtlicher, dafür mit hard-coded distance < 4

WITH RECURSIVE friend (personOne, personTwo, distance, path, cycle) AS (
	SELECT personOne, personTwo, 0, array[personOne], false
	FROM pkp_symmetric
	WHERE personOne = 94

	UNION

	SELECT ks.personOne, ks.personTwo, friend.distance + 1, path || ks.personOne, ks.personOne = ANY(path)
	FROM pkp_symmetric ks INNER JOIN friend ON ks.personone = friend.persontwo
	WHERE distance < 4 AND NOT cycle
) 

SELECT temp.friend, MIN(temp.min_distance) as min_distance, friend.path 
FROM friend INNER JOIN (
	SELECT person.id, CONCAT(person.firstname, ' ', person.lastname) AS friend, MIN(distance) AS min_distance
	FROM friend INNER JOIN person ON friend.personone = person.id
	WHERE person.id != 94
	GROUP BY person.id, friend
) temp ON friend.personOne = temp.id
WHERE temp.min_distance = friend.distance
GROUP BY temp.friend, friend.path
ORDER BY min_distance, friend