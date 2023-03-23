CREATE TABLE IF NOT EXISTS CLIENT (
    r_name VARCHAR(20) PRIMARY KEY NOT NULL
);

CREATE TABLE IF NOT EXISTS PLAYER (
                                      id  SERIAL PRIMARY KEY NOT NULL,
                                      email VARCHAR(50) UNIQUE CHECK (email LIKE '%@%.%') NOT NULL,
                                      username VARCHAR(20) UNIQUE NOT NULL,
                                      activity_state VARCHAR(100) CHECK( activity_state IN ('Active', 'Inactive', 'Banned') ),
                                      region_name VARCHAR(20) NOT NULL,

                                      FOREIGN KEY(region_name) REFERENCES REGION (r_name)
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP (
                                          player1_id INT NOT NULL,
                                          player2_id INT NOT NULL,

                                          PRIMARY KEY(player1_id,player2_id),
                                          FOREIGN KEY(player1_id) REFERENCES PLAYER (id),
                                          FOREIGN KEY(player2_id) REFERENCES PLAYER (id)
);