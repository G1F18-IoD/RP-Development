CREATE TYPE POSSIBLE_COMMANDS AS ENUM ('ARM', 'DISARM','CLOCKWISE','YAW COUNTER CLOCKWISE');

DROP TABLE IF EXISTS flightplan CASCADE;
CREATE TABLE IF NOT EXISTS flightplan (
  id serial NOT NULL,
  priority INT NOT NULL,
  cmd_delay INT NOT NULL,
  createdAt BIGINT NOT NULL,
  executedAt BIGINT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS flightplan_logs;
CREATE TABLE IF NOT EXISTS flightplan_logs (
  flightplan_id serial NOT NULL,
  logfile TEXT NOT NULL,
  CONSTRAINT fk_flightplan_logs_flightplan
    FOREIGN KEY (flightplan_id)
    REFERENCES flightplan (id)
);

DROP TABLE IF EXISTS flightplan_commands;
CREATE TABLE IF NOT EXISTS flightplan_commands (
  id serial NOT NULL,
  flightplan_id INT NOT NULL,
  cmd POSSIBLE_COMMANDS NOT NULL,
  payload integer[] NOT NULL,
  "order" INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_flightplan_commands_flightplan1
    FOREIGN KEY (flightplan_id)
    REFERENCES flightplan (id)
);