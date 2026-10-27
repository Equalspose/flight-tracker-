-- Аэропорты
CREATE TABLE airport (
    id         BIGSERIAL PRIMARY KEY,
    iata_code  VARCHAR(3)   NOT NULL UNIQUE,
    name       VARCHAR(200) NOT NULL,
    city       VARCHAR(100) NOT NULL,
    country    VARCHAR(100) NOT NULL,
    timezone   VARCHAR(50)  NOT NULL
);

-- Авиакомпании
CREATE TABLE airline (
    id         BIGSERIAL PRIMARY KEY,
    iata_code  VARCHAR(2)   NOT NULL UNIQUE,
    name       VARCHAR(200) NOT NULL,
    country    VARCHAR(100) NOT NULL
);

-- Рейсы (конкретный перелёт в конкретную дату)
CREATE TABLE flight (
    id                   BIGSERIAL PRIMARY KEY,
    flight_number        VARCHAR(10)  NOT NULL,
    airline_id           BIGINT       NOT NULL REFERENCES airline (id),
    departure_airport_id BIGINT       NOT NULL REFERENCES airport (id),
    arrival_airport_id   BIGINT       NOT NULL REFERENCES airport (id),
    scheduled_departure  TIMESTAMPTZ  NOT NULL,
    scheduled_arrival    TIMESTAMPTZ  NOT NULL,
    actual_departure     TIMESTAMPTZ,
    actual_arrival       TIMESTAMPTZ,
    status               VARCHAR(20)  NOT NULL,
    gate                 VARCHAR(10),
    aircraft_type        VARCHAR(50),
    CONSTRAINT uq_flight_number_departure UNIQUE (flight_number, scheduled_departure),
    CONSTRAINT chk_airports_differ CHECK (departure_airport_id <> arrival_airport_id),
    CONSTRAINT chk_times CHECK (scheduled_arrival > scheduled_departure)
);

CREATE INDEX idx_flight_number ON flight (flight_number);
CREATE INDEX idx_flight_sched_dep ON flight (scheduled_departure);
CREATE INDEX idx_flight_status ON flight (status);

-- История изменений статуса рейса (журнал отслеживания)
CREATE TABLE flight_event (
    id          BIGSERIAL PRIMARY KEY,
    flight_id   BIGINT      NOT NULL REFERENCES flight (id) ON DELETE CASCADE,
    old_status  VARCHAR(20),
    new_status  VARCHAR(20) NOT NULL,
    comment     VARCHAR(500),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_flight_event_flight ON flight_event (flight_id);
