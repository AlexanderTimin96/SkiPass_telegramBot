CREATE TABLE IF NOT EXISTS clients
(
    chat_id   BIGINT PRIMARY KEY,
    firstname VARCHAR(200),
    lastname  VARCHAR(200),
    username  VARCHAR(200),
    phone     VARCHAR(20)
);


