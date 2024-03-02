CREATE TABLE place (
	id bigint PRIMARY KEY,
	name varchar(200) NOT NULL,
	url varchar(200),
	type varchar(50),
	isPartOf bigint
);

CREATE TABLE continent (
	id bigint PRIMARY KEY REFERENCES place (id) ON UPDATE CASCADE
);

CREATE TABLE country (
	id bigint PRIMARY KEY REFERENCES place (id) ON UPDATE CASCADE,
	isPartOf bigint NOT NULL REFERENCES continent (id) ON UPDATE CASCADE
);

CREATE TABLE city (
	id bigint PRIMARY KEY REFERENCES place (id) ON UPDATE CASCADE,
	isPartOf bigint NOT NULL REFERENCES country (id) ON UPDATE CASCADE
);

CREATE TABLE organisation (
	id bigint PRIMARY KEY,
	type varchar(50),
	name varchar(200) NOT NULL,
	url varchar(200),
	place bigint
);

CREATE TABLE university (
	id bigint PRIMARY KEY REFERENCES organisation (id) ON UPDATE CASCADE,
	place bigint NOT NULL REFERENCES city (id) ON UPDATE CASCADE
);

CREATE TABLE company (
	id bigint PRIMARY KEY REFERENCES organisation (id) ON UPDATE CASCADE,
	place bigint NOT NULL REFERENCES country (id) ON UPDATE CASCADE
);

CREATE TABLE person (
	id bigint PRIMARY KEY,
	firstName varchar(50) NOT NULL,
	lastName varchar(50) NOT NULL,
	gender varchar(20) NOT NULL,
	birthday Date NOT NULL,
	creationDate timestamp with time zone NOT NULL,
	locationIP varchar(50) NOT NULL,
	browserUsed varchar(50) NOT NULL,
	place bigint NOT NULL REFERENCES city (id) ON UPDATE CASCADE,
	CONSTRAINT check_birthday CHECK (birthday <= now ():: date )
);

CREATE TABLE personEmail (
	person bigint REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	email varchar(100) UNIQUE NOT NULL,
	PRIMARY KEY (person, email),
	CONSTRAINT check_email CHECK (email ~*
	'^(?:(?:[\w`~!#$%^&*\-=+;:{}|,?\/]+(?:(?:\.(?:"(?:\\?[\w`~!#$%^&*\
	-=+;:{}|,?\/\.()<>\[\]
	@]|\\"|\\\\)*"|[\w`~!#$%^&*\-=+;:{}|,?\/]+))*\.[\w`~!#$%^&*\-=+;:{
	}|,?\/]+)?)|(?:"(?:\\?[\w`~!#$%^&*\-=+;:{}|,?\/\.()<>\[\]
	@]|\\"|\\\\)+"))@(?:[a-zA-Z\d\-]+(?:\.[a-zA-Z\d\-]+)*|\[\d{1,3}\.\
	d{1,3}\.\d{1,3}\.\d{1,3}\])$' :: text )
);

CREATE TABLE personSpeaks (
	person bigint REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	language varchar(2) NOT NULL,
	PRIMARY KEY (person, language)
);

CREATE TABLE tag (
	id bigint PRIMARY KEY,
	name varchar(200) NOT NULL,
	url varchar(200)
);

CREATE TABLE tagClass (
	id bigint PRIMARY KEY,
	name varchar (200) NOT NULL,
	url varchar (200)
);

CREATE TABLE forum (
	id bigint PRIMARY KEY,
	title varchar(50) NOT NULL,
	creationDate timestamp with time zone NOT NULL,
	moderator bigint NOT NULL REFERENCES person (id) ON UPDATE CASCADE
);

CREATE TABLE post (
	id bigint PRIMARY KEY,
	imageFile varchar(100),
	creationDate timestamp with time zone NOT NULL,
	locationIP varchar(50) NOT NULL,
	browserUsed varchar(50) NOT NULL,
	language varchar(2),
	content text,
	length int NOT NULL,
	creator bigint NOT NULL REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	forum bigint NOT NULL REFERENCES forum (id) ON DELETE CASCADE ON UPDATE CASCADE,
	place bigint NOT NULL REFERENCES country (id) ON UPDATE CASCADE
);

CREATE TABLE comment (
	id bigint PRIMARY KEY,
	creationDate timestamp with time zone NOT NULL,
	locationIP varchar(50) NOT NULL,
	browserUsed varchar(50) NOT NULL,
	content text,
	length int NOT NULL,
	creator bigint NOT NULL REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	place bigint NOT NULL REFERENCES country (id) ON UPDATE CASCADE,
	replyOfPost bigint REFERENCES post (id) ON DELETE CASCADE ON UPDATE CASCADE,
	replyOfComment bigint REFERENCES comment (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CHECK ((replyOfPost IS NOT NULL) OR (replyOfComment IS NOT NULL))
);

CREATE TABLE knows (
	personOne bigint REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	personTwo bigint REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	creationDate timestamp with time zone NOT NULL,
	PRIMARY KEY (personOne, personTwo)
);

CREATE TABLE studyAt (
	person bigint REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	university bigint REFERENCES university (id) ON DELETE CASCADE ON UPDATE CASCADE,
	classYear int,
	PRIMARY KEY (person, university, classYear)
);

CREATE TABLE workAt (
	person bigint REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	company bigint REFERENCES company (id) ON DELETE CASCADE ON UPDATE CASCADE,
	workFrom int,
	PRIMARY KEY (person, company, workFrom)
);

CREATE TABLE likesComment (
	person bigint REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	comment bigint REFERENCES comment (id) ON DELETE CASCADE ON UPDATE CASCADE,
	creationDate timestamp with time zone NOT NULL,
	PRIMARY KEY (person, comment)
);

CREATE TABLE likesPost (
	person bigint REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	post bigint REFERENCES post (id) ON DELETE CASCADE ON UPDATE CASCADE,
	creationDate timestamp with time zone NOT NULL,
	PRIMARY KEY (person, post)
);

CREATE TABLE hasMember (
	forum bigint REFERENCES forum (id) ON DELETE CASCADE ON UPDATE CASCADE,
	person bigint REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	joinDate timestamp with time zone NOT NULL,
	PRIMARY KEY (forum, person)
);

CREATE TABLE hasInterest (
	person bigint REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE,
	tag bigint REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY (person, tag)
);

CREATE TABLE forumHasTag (
	forum bigint REFERENCES forum (id) ON DELETE CASCADE ON UPDATE CASCADE,
	tag bigint REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY (forum, tag)
);

CREATE TABLE hasType (
	tag bigint REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE,
	tagClass bigint REFERENCES tagClass (id) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY (tag, tagClass)
);

CREATE TABLE isSubclassOf (
	tagClassOne bigint REFERENCES tagClass (id) ON DELETE CASCADE ON UPDATE CASCADE,
	tagClassTwo bigint REFERENCES tagClass (id) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY (tagClassOne, tagClassTwo)
); 	

CREATE TABLE postHasTag (
	post bigint REFERENCES post (id) ON DELETE CASCADE ON UPDATE CASCADE,
	tag bigint REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY (post, tag)
);

CREATE TABLE commentHasTag (
	comment bigint REFERENCES comment (id) ON DELETE CASCADE ON UPDATE CASCADE,
	tag bigint REFERENCES tag (id) ON DELETE CASCADE ON UPDATE CASCADE,
	PRIMARY KEY (comment, tag)
);