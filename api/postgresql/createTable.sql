CREATE SCHEMA IF NOT EXISTS dbo;

CREATE TABLE IF NOT EXISTS dbo.users(
    id               UUID PRIMARY KEY,
    name             VARCHAR(50) NOT NULL,
    email            VARCHAR(80) NOT NULL UNIQUE,
    password_hash    text NOT NULL,

    CONSTRAINT name_length CHECK ( char_length(name) >= 3 ),
    CONSTRAINT email_is_valid CHECK ( email ~ '^[A-Za-z0-9+_.-]+@(.+)$' )
);

CREATE TABLE IF NOT EXISTS dbo.tokens(
    token_hash       text PRIMARY KEY,
    user_id          UUID REFERENCES dbo.users(id)
);

CREATE TABLE IF NOT EXISTS dbo.monitors(
    m_id    UUID PRIMARY KEY REFERENCES dbo.users(id)
);

CREATE TABLE IF NOT EXISTS dbo.clients(
    c_id                    UUID PRIMARY KEY REFERENCES dbo.users (id),
    birth_date              DATE,
    weight                  INT,
    height                  INT,
    physical_condition      VARCHAR(50),

    CONSTRAINT age_is_valid  CHECK ( date_part('years', age(CURRENT_DATE, birth_date)) >= 7 ),
    CONSTRAINT weight_is_valid CHECK ( weight >= 30 AND weight <= 300 ),
    CONSTRAINT height_is_valid CHECK ( height >= 100 AND height <= 250 ),
    CONSTRAINT physical_condition_length CHECK ( char_length(physical_condition) >= 5 )

);

CREATE TABLE IF NOT EXISTS dbo.client_to_monitor(
      monitor_id UUID  REFERENCES dbo.monitors (m_id),
      client_id  UUID UNIQUE REFERENCES dbo.users (id)

);

CREATE TABLE IF NOT EXISTS dbo.client_requests(
    request_id      UUID PRIMARY KEY,
    monitor_id      UUID REFERENCES dbo.monitors (m_id),
    client_id       UUID REFERENCES dbo.clients (c_id),
    request_text    TEXT,

    UNIQUE (monitor_id, client_id),

    CONSTRAINT request_yourself CHECK ( client_id != monitor_id )
);

CREATE TABLE IF NOT EXISTS dbo.monitor_rating(
    monitor_id       UUID NOT NULL REFERENCES dbo.monitors (m_id),
    client_id        UUID NOT NULL REFERENCES dbo.clients (c_id),
    stars            INT NOT NULL,

    PRIMARY KEY (monitor_id, client_id),

    CONSTRAINT rate_yourself CHECK ( client_id != monitor_id ),
    CONSTRAINT stars_are_valid CHECK ( stars >= 1 AND stars <= 5 )
);

CREATE TABLE IF NOT EXISTS dbo.docs_authenticity(
    monitor_id      UUID PRIMARY KEY REFERENCES dbo.monitors (m_id),
    state           VARCHAR(10) NOT NULL,
    dt_submit       DATE NOT NULL,

    CONSTRAINT state_check CHECK ( state IN ('invalid', 'waiting', 'valid') )
);

CREATE TABLE IF NOT EXISTS dbo.plans(
    id               SERIAL PRIMARY KEY,
    monitor_id       UUID NOT NULL REFERENCES dbo.monitors (m_id),
    title            VARCHAR(50) NOT NULL,

    CONSTRAINT title_length CHECK ( char_length(title) >= 5 )
);

CREATE TABLE IF NOT EXISTS dbo.client_plans(
    plan_id   INT NOT NULL REFERENCES dbo.plans (id),
    client_id UUID NOT NULL REFERENCES dbo.clients (c_id),
    dt_start  DATE NOT NULL,

    PRIMARY KEY (plan_id, client_id),

    CONSTRAINT date_greater_than_current CHECK ( dt_start > CURRENT_DATE )
);

CREATE TABLE IF NOT EXISTS dbo.daily_lists(
    id          SERIAL PRIMARY KEY,
    index       INT NOT NULL,
    plan_id     INT NOT NULL REFERENCES dbo.plans (id),

    UNIQUE (index, plan_id),

    CONSTRAINT index_is_valid CHECK ( index >= 0 )
);

CREATE TABLE IF NOT EXISTS dbo.exercises_info(
    id               UUID PRIMARY KEY,
    title            VARCHAR(50) NOT NULL,
    description      TEXT NOT NULL,
    type             VARCHAR(20) NOT NULL,

    CONSTRAINT title_length CHECK ( char_length(title) >= 5 ),
    CONSTRAINT description_length CHECK ( char_length(description) >= 10 ),
    CONSTRAINT type_length CHECK ( char_length(description) >= 2 )
);

CREATE TABLE IF NOT EXISTS dbo.daily_exercises(
    id                  SERIAL PRIMARY KEY,
    ex_id               UUID NOT NULL REFERENCES dbo.exercises_info (id),
    daily_list_id       INT NOT NULL REFERENCES dbo.daily_lists (id),
    sets                INT NOT NULL,
    reps                INT NOT NULL,

    CONSTRAINT sets_is_valid CHECK ( sets >= 1 AND sets <= 10 ),
    CONSTRAINT reps_is_valid CHECK ( reps >= 1 AND reps <= 50 )
);

CREATE TABLE IF NOT EXISTS dbo.exercises_video(
    id                  UUID PRIMARY KEY,
    ex_id               INT NOT NULL REFERENCES dbo.daily_exercises (id),
    client_id           UUID NOT NULL REFERENCES dbo.clients (c_id),
    dt_submit           DATE NOT NULL,
    feedback_monitor    VARCHAR(200)-- can be null, no answer or waiting
)