create schema if not exists dbo;

CREATE TABLE IF NOT EXISTS dbo.USERS(
   id UUID PRIMARY KEY NOT NULL,
   u_name VARCHAR(50) NOT NULL,
   u_email VARCHAR(50) UNIQUE CHECK (u_email LIKE '%@%.%') NOT NULL,
   password_hash VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS dbo.MONITOR (
   m_id UUID PRIMARY KEY NOT NULL,

  FOREIGN KEY (m_id) REFERENCES dbo.USERS(id)
);

CREATE TABLE IF NOT EXISTS dbo.CLIENT (
    c_id UUID PRIMARY KEY NOT NULL,
    physical_condition VARCHAR(200),
    monitor_id UUID,--can be null because of the free exercise option

    FOREIGN KEY (c_id) REFERENCES dbo.USERS(id),
    FOREIGN KEY(monitor_id) REFERENCES dbo.MONITOR (m_id)
);

CREATE TABLE dbo.TOKENS(
   token_validation VARCHAR(256) primary key,
   user_id UUID references dbo.USERS(id)
);

CREATE TABLE IF NOT EXISTS dbo.MONITOR_RATING (
    monitor_id UUID NOT NULL,
    client_id UUID NOT NULL,
    stars INT NOT NULL CHECK ( stars >= 1 and stars <= 5 ),

    PRIMARY KEY(monitor_id,client_id),
    FOREIGN KEY(monitor_id) REFERENCES dbo.MONITOR(m_id),
    FOREIGN KEY (client_id) REFERENCES dbo.CLIENT(c_id)
);

CREATE TABLE IF NOT EXISTS dbo.DOC_AUTHENTICITY(
    monitor_id UUID PRIMARY KEY NOT NULL,
    doc_valid BOOLEAN NOT NULL,
    dt_submit TIMESTAMP NOT NULL,

    FOREIGN KEY (monitor_id) REFERENCES dbo.MONITOR(m_id)
);


CREATE TABLE IF NOT EXISTS dbo.PLAN(
    id SERIAL PRIMARY KEY NOT NULL,
    p_name VARCHAR(50) NOT NULL,
    monitor_id UUID NOT NULL,

    FOREIGN KEY (monitor_id) REFERENCES dbo.MONITOR(m_id)

);

CREATE TABLE IF NOT EXISTS dbo.CLIENT_PLAN(
    plan_id INT NOT NULL,
    client_id UUID  NOT NULL,
    dt_start TIMESTAMP NOT NULL,
    dt_end TIMESTAMP NOT NULL CHECK ( dt_end > dt_start ),

    PRIMARY KEY(plan_id,client_id),
    FOREIGN KEY (client_id) REFERENCES dbo.CLIENT(c_id),
    FOREIGN KEY (plan_id) REFERENCES dbo.PLAN(id)
);

CREATE TABLE IF NOT EXISTS dbo.DAILY_LIST(
   id SERIAL PRIMARY KEY NOT NULL,
   plan_id INT NOT NULL,
   dt_to_do TIMESTAMP NOT NULL,

   FOREIGN KEY (plan_id) REFERENCES dbo.PLAN(id)
);

CREATE TABLE IF NOT EXISTS dbo.EXERCISE_INFORMATION(
    id UUID PRIMARY KEY NOT NULL,
    title varchar(20) NOT NULL,
    description VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS dbo.EXERCISE(
    id SERIAL PRIMARY KEY NOT NULL,
    ex_info_id UUID NOT NULL,
    e_sets INT NOT NULL CHECK ( e_sets > 0 ),
    e_reps INT NOT NULL CHECK ( e_reps > 0 ),

    FOREIGN KEY (ex_info_id) REFERENCES dbo.EXERCISE_INFORMATION(id)
);

CREATE TABLE IF NOT EXISTS dbo.DAILY_EXERCISE(
    list_id INT NOT NULL,
    ex_id INT NOT NULL,

    PRIMARY KEY (list_id,ex_id),
    FOREIGN KEY (list_id) REFERENCES dbo.DAILY_LIST(id),
    FOREIGN KEY (ex_id) REFERENCES dbo.EXERCISE(id)

);

CREATE TABLE IF NOT EXISTS dbo.EXERCISE_VIDEO(
   id UUID NOT NULL,
   submitted TIMESTAMP NOT NULL,
   list_id INT NOT NULL,
   ex_id INT NOT NULL,
   client_id UUID NOT NULL,
   feedback_monitor VARCHAR(200),--can be null, no answer or waiting

   CONSTRAINT not_repeat unique ( list_id,ex_id,client_id ),

   PRIMARY KEY(id),
   FOREIGN KEY (client_id) REFERENCES dbo.CLIENT(c_id),
   FOREIGN KEY (list_id,ex_id) REFERENCES dbo.DAILY_EXERCISE(list_id, ex_id)
);


