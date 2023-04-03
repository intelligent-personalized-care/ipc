CREATE TABLE IF NOT EXISTS USERS(
   id UUID PRIMARY KEY NOT NULL,
   u_name VARCHAR(50) NOT NULL,
   u_email VARCHAR(50) UNIQUE CHECK (u_email LIKE '%@%.%') NOT NULL
);



/*CREATE TABLE IF NOT EXISTS MONITOR (
   id UUID PRIMARY KEY NOT NULL,
   m_name VARCHAR(50) NOT NULL,
   m_email VARCHAR(50) UNIQUE CHECK (m_email LIKE '%@%.%') NOT NULL
);*/

CREATE TABLE IF NOT EXISTS MONITOR (
   m_id UUID PRIMARY KEY NOT NULL,

  FOREIGN KEY (m_id) REFERENCES USERS(id)
);

/*CREATE TABLE IF NOT EXISTS CLIENT (
    id UUID PRIMARY KEY NOT NULL,
    c_name VARCHAR(50) NOT NULL,
    c_email VARCHAR(50) UNIQUE CHECK (c_email LIKE '%@%.%') NOT NULL,
    physical_condition VARCHAR(200),
    monitor_id UUID,--can be null because of the free exercise option

    FOREIGN KEY(monitor_id) REFERENCES MONITOR (id)
);*/

CREATE TABLE IF NOT EXISTS CLIENT (
    c_id UUID PRIMARY KEY NOT NULL,
    physical_condition VARCHAR(200),
    monitor_id UUID,--can be null because of the free exercise option

    FOREIGN KEY (c_id) REFERENCES USERS(id),
    FOREIGN KEY(monitor_id) REFERENCES MONITOR (m_id)
);

CREATE TABLE TOKENS(
   token_validation VARCHAR(256) primary key,
   user_id int references USERS(id)
);

CREATE TABLE IF NOT EXISTS MONITOR_RATING (
    monitor_id UUID NOT NULL,
    client_id UUID NOT NULL,
    stars INT NOT NULL CHECK ( stars >= 1 and stars <= 5 ),

    PRIMARY KEY(monitor_id,client_id),
    FOREIGN KEY(monitor_id) REFERENCES MONITOR(id),
    FOREIGN KEY (client_id) REFERENCES CLIENT(id)
);

CREATE TABLE IF NOT EXISTS DOC_AUTHENTICITY(
    monitor_id UUID PRIMARY KEY NOT NULL,
    doc_valid BOOLEAN NOT NULL,
    dt_submit TIMESTAMP NOT NULL,

    FOREIGN KEY (monitor_id) REFERENCES MONITOR(id)
);


CREATE TABLE IF NOT EXISTS PLAN(
    id SERIAL PRIMARY KEY NOT NULL,
    p_name VARCHAR(50) NOT NULL,
    monitor_id UUID NOT NULL,

    FOREIGN KEY (monitor_id) REFERENCES MONITOR(id)

);

CREATE TABLE IF NOT EXISTS CLIENT_PLAN(
    plan_id INT NOT NULL,
    client_id UUID  NOT NULL,
    dt_start TIMESTAMP NOT NULL,
    dt_end TIMESTAMP NOT NULL CHECK ( dt_end > dt_start ),

    PRIMARY KEY(plan_id,client_id),
    FOREIGN KEY (client_id) REFERENCES CLIENT(id)
);

CREATE TABLE IF NOT EXISTS DAILY_LIST(
   id SERIAL PRIMARY KEY NOT NULL,
   plan_id INT NOT NULL,
   dt_to_do TIMESTAMP NOT NULL,

   FOREIGN KEY (plan_id) REFERENCES PLAN(id)
);

CREATE TABLE IF NOT EXISTS EXERCISE_PREVIEW(
     id UUID PRIMARY KEY NOT NULL,
     description VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS EXERCISE(
    id SERIAL PRIMARY KEY NOT NULL,
    preview_id UUID NOT NULL,
    list_id INT NOT NULL,
    e_sets INT NOT NULL CHECK ( e_sets > 0 ),
    e_reps INT NOT NULL CHECK ( e_reps > 0 ),

    FOREIGN KEY (preview_id) REFERENCES EXERCISE_PREVIEW(id),
    FOREIGN KEY (list_id) REFERENCES DAILY_LIST(id)
);

CREATE TABLE IF NOT EXISTS EXERCISE_VIDEO(
   id UUID NOT NULL,
   submitted TIMESTAMP NOT NULL,
   exercise_id INT NOT NULL,
   client_id UUID NOT NULL,
   feedback_monitor VARCHAR(200),--can be null, no answer or waiting

   PRIMARY KEY(id,client_id),
   FOREIGN KEY (exercise_id) REFERENCES EXERCISE(id),
   FOREIGN KEY (client_id) REFERENCES CLIENT(id)
);


