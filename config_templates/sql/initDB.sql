DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
CREATE TYPE group_status AS ENUM ('REGISTERING', 'CURRENT', 'FINISHED');

CREATE SEQUENCE user_seq START 100000;

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL,
  town_id   INTEGER NOT NULL,
  FOREIGN KEY (town_id) REFERENCES towns (id)
);

CREATE UNIQUE INDEX email_idx ON users (email);

CREATE TABLE towns (
                       id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
                       full_name TEXT NOT NULL,
                       shot_name TEXT NOT NULL
);

CREATE TABLE groups (
                        id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
                        name         TEXT NOT NULL,
                        status     group_status NOT NULL,
                        project_id INTEGER,
                        FOREIGN KEY (project_id) REFERENCES projects(id)
);

CREATE TABLE projects (
                          id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
                          name TEXT NOT NULL
);
CREATE TABLE users_groups (
                              user_id        INTEGER NOT NULL ,
                              group_id      INTEGER NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES users (id),
                              FOREIGN KEY (group_id) REFERENCES groups (id)
);