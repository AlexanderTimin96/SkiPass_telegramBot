CREATE TABLE IF NOT EXISTS clients
(
    chat_id   BIGINT PRIMARY KEY,
    firstname VARCHAR(200),
    lastname  VARCHAR(200),
    username  VARCHAR(200),
    phone     VARCHAR(20) NOT NULL,
    ski_pass_id BIGINT
);

CREATE TABLE IF NOT EXISTS ski_pass
(
    ski_pass_id BIGINT PRIMARY KEY,
    ski_pass_number varchar(20),
    number_of_lifts INT,
    client_id BIGINT
);

