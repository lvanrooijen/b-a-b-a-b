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
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    role       VARCHAR(50)  NOT NULL,
    address_id BIGINT,
    created_on DATE         NOT NULL,
    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES addresses (id)
);

-- maak table voor customer user
CREATE TABLE customer_users
(
    id         BIGINT PRIMARY KEY,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    birthdate  DATE         NOT NULL,
    CONSTRAINT fk_customer_users FOREIGN KEY (id) REFERENCES users (id)
);
-- maak table voor business user
CREATE TABLE business_users
(
    id           BIGINT PRIMARY KEY,
    company_name VARCHAR(250) NOT NULL,
    kvk_number   VARCHAR(8)   NOT NULL,
    CONSTRAINT fk_business_users FOREIGN KEY (id) REFERENCES users (id)
);

-- maak table voor admin user
CREATE TABLE admin_users
(
    id                    BIGINT PRIMARY KEY,
    failed_login_attempts INT NOT NULL,
    last_login            TIMESTAMP without TIME ZONE,
    CONSTRAINT fk_admin_users FOREIGN KEY (id) REFERENCES users (id)
);
