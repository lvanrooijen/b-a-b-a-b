CREATE SEQUENCE IF NOT EXISTS password_reset_requests_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE password_reset_requests
(
    id                BIGINT                      NOT NULL,
    verification_code UUID                        NOT NULL,
    issued_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_expired        BOOLEAN                     NOT NULL,
    user_id           BIGINT                      NOT NULL,
    CONSTRAINT pk_passwordresetrequest PRIMARY KEY (id)
)