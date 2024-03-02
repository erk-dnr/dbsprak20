COPY place FROM 'your\absolute\path\to\file\place_0_0.csv' DELIMITER '|' CSV HEADER;

WITH selected_rows AS (SELECT id FROM place WHERE type = 'continent')
INSERT INTO continent SELECT * FROM selected_rows;

WITH selected_rows AS (SELECT id, isPartOf FROM place WHERE type = 'country')
INSERT INTO country SELECT * FROM selected_rows;

WITH selected_rows AS (SELECT id, isPartOf FROM place WHERE type = 'city')
INSERT INTO city SELECT * FROM selected_rows;

ALTER TABLE place 
DROP COLUMN type,
DROP COLUMN isPartOf;

COPY organisation FROM 'your\absolute\path\to\file\organisation_0_0.csv' DELIMITER '|' CSV HEADER;

WITH selected_rows AS (SELECT id, place FROM organisation WHERE type = 'university')
INSERT INTO university SELECT * FROM selected_rows;

WITH selected_rows AS (SELECT id, place FROM organisation WHERE type = 'company')
INSERT INTO company SELECT * FROM selected_rows;

ALTER TABLE organisation 
DROP COLUMN type,
DROP COLUMN place;

COPY person FROM 'your\absolute\path\to\file\person_0_0.csv' DELIMITER '|' CSV HEADER;

COPY personEmail FROM 'your\absolute\path\to\file\person_email_emailaddress_0_0.csv' DELIMITER '|' CSV HEADER;

COPY tag FROM 'your\absolute\path\to\file\tag_0_0.csv' DELIMITER '|' CSV HEADER;

COPY tagClass FROM 'your\absolute\path\to\file\tagclass_0_0.csv' DELIMITER '|' CSV HEADER;

COPY forum FROM 'your\absolute\path\to\file\forum_0_0.csv' DELIMITER '|' CSV HEADER;

COPY post FROM 'your\absolute\path\to\file\post_0_0.csv' DELIMITER '|' CSV HEADER;

COPY comment FROM 'your\absolute\path\to\file\comment_0_0.csv' DELIMITER '|' CSV HEADER;

COPY knows FROM 'your\absolute\path\to\file\person_knows_person_0_0.csv' DELIMITER '|' CSV HEADER;

COPY personSpeaks FROM 'your\absolute\path\to\file\person_speaks_language_0_0.csv' DELIMITER '|' CSV HEADER;

COPY studyAt FROM 'your\absolute\path\to\file\person_studyAt_organisation_0_0.csv' DELIMITER '|' CSV HEADER;

COPY workAt FROM 'your\absolute\path\to\file\person_workAt_organisation_0_0.csv' DELIMITER '|' CSV HEADER;

COPY likesComment FROM 'your\absolute\path\to\file\person_likes_comment_0_0.csv' DELIMITER '|' CSV HEADER;

COPY likesPost FROM 'your\absolute\path\to\file\person_likes_post_0_0.csv' DELIMITER '|' CSV HEADER;

COPY hasMember FROM 'your\absolute\path\to\file\forum_hasMember_person_0_0.csv' DELIMITER '|' CSV HEADER;

COPY hasInterest FROM 'your\absolute\path\to\file\person_hasInterest_tag_0_0.csv' DELIMITER '|' CSV HEADER;

COPY forumHasTag FROM 'your\absolute\path\to\file\forum_hasTag_tag_0_0.csv' DELIMITER '|' CSV HEADER;

COPY hasType FROM 'your\absolute\path\to\file\tag_hasType_tagclass_0_0.csv' DELIMITER '|' CSV HEADER;

COPY isSubclassOf FROM 'your\absolute\path\to\file\tagclass_isSubclassOf_tagclass_0_0.csv' DELIMITER '|' CSV HEADER;

COPY postHasTag FROM 'your\absolute\path\to\file\post_hasTag_tag_0_0.csv' DELIMITER '|' CSV HEADER;

COPY commentHasTag FROM 'your\absolute\path\to\file\comment_hasTag_tag_0_0.csv' DELIMITER '|' CSV HEADER;