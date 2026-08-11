-- V2__add_reservation_overlap_constraint.sql

CREATE EXTENSION IF NOT EXISTS btree_gist;

ALTER TABLE reservation
    ADD COLUMN date_range daterange
        GENERATED ALWAYS AS (daterange(start_date, end_date, '[)')) STORED;

CREATE INDEX idx_reservation_room_daterange
    ON reservation USING gist (room_id, date_range)
    WHERE status <> 'CANCELLED';

ALTER TABLE reservation
    ADD CONSTRAINT no_overlapping_reservations
    EXCLUDE USING gist (
        room_id WITH =,
        date_range WITH &&
    ) WHERE (status <> 'CANCELLED');