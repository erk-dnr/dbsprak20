CREATE TABLE the_termination_of_an_employment (
   employee BIGINT REFERENCES person(id) ON DELETE CASCADE ON UPDATE CASCADE,
   company BIGINT REFERENCES company(id) ON DELETE CASCADE ON UPDATE CASCADE,
   date TIMESTAMP WITH TIME ZONE,
   PRIMARY KEY(employee, company, date)
);

CREATE OR REPLACE FUNCTION log_termination() RETURNS trigger AS $log_termination$
BEGIN
	IF (TG_OP = 'DELETE') THEN
		INSERT INTO the_termination_of_an_employment VALUES(OLD.person, OLD.company, now());
	END IF;

	RETURN NEW;
END;
$log_termination$ LANGUAGE plpgsql;

CREATE TRIGGER log_termination AFTER DELETE ON workat
    FOR EACH ROW EXECUTE FUNCTION log_termination();