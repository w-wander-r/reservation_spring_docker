-- V1__create_reservation_table.sql

CREATE TABLE reservation (
     id           BIGSERIAL PRIMARY KEY,
     user_id      BIGINT NOT NULL,
     room_id      BIGINT NOT NULL,
     start_date   DATE NOT NULL,
     end_date     DATE NOT NULL,
     status       VARCHAR(20) NOT NULL,
     version      INTEGER NOT NULL DEFAULT 0
);