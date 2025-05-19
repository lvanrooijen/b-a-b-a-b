CREATE TABLE addresses
(
    id           BIGSERIAL PRIMARY KEY,
    street       VARCHAR(255) NOT NULL,
    house_number INT          NOT NULL,
    city         VARCHAR(255) NOT NULL,
    postal_code  VARCHAR(10)  NOT NULL
);

CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    role       VARCHAR(50)  NOT NULL,
    address_id BIGINT       NOT NULL UNIQUE,
    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES addresses (id)
);