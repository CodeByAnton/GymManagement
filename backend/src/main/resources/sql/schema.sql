-- Schema

CREATE TABLE "user_details" --
(
    id         SERIAL PRIMARY KEY,
    birth_date DATE,
    name       VARCHAR(100),
    surname    VARCHAR(100)
);


CREATE TABLE "users" --
(
    id           SERIAL PRIMARY KEY,
    email        VARCHAR(100) UNIQUE                                                NOT NULL,
    password     VARCHAR(100)                                                       NOT NULL,
    role         VARCHAR(20) CHECK (role IN ('CLIENT', 'TRAINER', 'ADMINISTRATOR')) NOT NULL,
    user_details   INT UNIQUE REFERENCES "user_details" (id) ON DELETE CASCADE,
    is_confirmed BOOLEAN DEFAULT FALSE
);

CREATE TABLE "refresh_token" --
(
    id      SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES "users" (id) ON DELETE CASCADE,
    token   VARCHAR(255) NOT NULL
);

CREATE TABLE "otp_token" --
(
    id      SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES "users" (id) ON DELETE CASCADE,
    token   VARCHAR(6) NOT NULL,
    expires_at TIMESTAMP NOT NULL
);

CREATE TABLE "pass_code_token" --
(
    id      SERIAL PRIMARY KEY,
    user_id INT UNIQUE REFERENCES "users" (id) ON DELETE CASCADE,
    token   VARCHAR(6) NOT NULL,
    expires_at TIMESTAMP NOT NULL
);

CREATE TABLE "role_application" --
(
    id            SERIAL PRIMARY KEY,
    user_id       INT REFERENCES "users" (id),
    desired_role         VARCHAR(20) CHECK (desired_role IN ('CLIENT', 'TRAINER', 'ADMINISTRATOR')) NOT NULL,
    status        VARCHAR(20) CHECK (status IN ('CONSIDERATION', 'ACCEPTED', 'DECLINED')) DEFAULT 'CONSIDERATION',
    created_at    TIMESTAMP                                                               DEFAULT CURRENT_TIMESTAMP,
    considered_at TIMESTAMP                                                               DEFAULT NULL
);

CREATE TABLE "subscription" --
(
    id            SERIAL PRIMARY KEY,
    user_id       INT UNIQUE REFERENCES "users" (id) ON DELETE CASCADE,
    purchase_date DATE,
    start_date    DATE,
    end_date      DATE,
    price         INT CHECK (price >= 0)
);


CREATE TABLE "group_training_program_details" --
(
    id              SERIAL PRIMARY KEY,
    title           TEXT NOT NULL,
    description     TEXT NOT NULL,
    program_details TEXT NOT NULL
);

CREATE TABLE "group_training" --
(
    id                  SERIAL PRIMARY KEY,
    trainer_id          INT       REFERENCES "users" (id) ON DELETE SET NULL,
    training_date       TIMESTAMP NOT NULL,
    duration_in_minutes INT CHECK (max_participants > 0),
    max_participants    INT CHECK (max_participants > 0),
    start_time          TIMESTAMP NOT NULL,
    pass_code   VARCHAR(6) NOT NULL,
    details_id          INT REFERENCES "group_training_program_details" (id) ON DELETE CASCADE,
    status              VARCHAR(20) CHECK (status IN ('SCHEDULED', 'CANCELLED', 'CONDUCTED')) DEFAULT 'SCHEDULED'
);

CREATE TABLE "personal_service" --
(
    id           SERIAL PRIMARY KEY,
    trainer_id   INT REFERENCES "users" (id),
    client_id    INT REFERENCES "users" (id),
    start_time   TIMESTAMP NOT NULL,
    end_time   TIMESTAMP NOT NULL,
    pass_code   VARCHAR(6) NOT NULL,
    service_type VARCHAR(20) CHECK (service_type IN ('TRAINING', 'MESSAGE', 'CONSULTATION')) DEFAULT 'TRAINING',
    status       VARCHAR(20) CHECK (status IN ('SCHEDULED', 'CANCELLED', 'CONDUCTED'))       DEFAULT 'SCHEDULED'
);

CREATE TABLE "notification"--
(
    id                SERIAL PRIMARY KEY,
    user_id           INT REFERENCES "users" (id),
    sended_by         INT REFERENCES "users" (id),
    message           TEXT,
    notification_date TIMESTAMP,
    status            VARCHAR(20) CHECK (status IN ('CHECKED', 'UNCHECKED')) DEFAULT 'CHECKED'
);

CREATE TABLE "group_training_booking"
(
    id                SERIAL PRIMARY KEY,
    client_id         INT REFERENCES "users" (id) ON DELETE CASCADE,
    group_training_id INT REFERENCES "group_training" (id) ON DELETE CASCADE,
    booking_status    VARCHAR(20) CHECK (booking_status IN ('SCHEDULED', 'CANCELLED', 'ATTENDED', 'SKIP')) DEFAULT 'SCHEDULED',
    created_at        TIMESTAMP                                                                    DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                                                                    DEFAULT CURRENT_TIMESTAMP
);

-- Functions and triggers for integrity check

CREATE FUNCTION check_client_role(user_id INT) RETURNS BOOLEAN AS
'BEGIN
    IF (SELECT role
        FROM "users"
        WHERE id = user_id) = ''CLIENT'' THEN
        RETURN TRUE;
    ELSE
        RAISE EXCEPTION ''User must have role CLIENT'';
    END IF;
END;' LANGUAGE plpgsql;

CREATE FUNCTION check_trainer_role(user_id INT) RETURNS BOOLEAN AS
'BEGIN
    IF (SELECT role
        FROM "users"
        WHERE id = user_id) = ''TRAINER'' THEN
        RETURN TRUE;
    ELSE
        RAISE EXCEPTION ''User must have role TRAINER'';
    END IF;
END;' LANGUAGE plpgsql;

CREATE FUNCTION check_admin_role(user_id INT) RETURNS BOOLEAN AS
'BEGIN
    IF (SELECT role
        FROM "users"
        WHERE id = user_id) = ''ADMINISTRATOR'' THEN
        RETURN TRUE;
    ELSE
        RAISE EXCEPTION ''User must have role ADMINISTRATOR'';
    END IF;
END;' LANGUAGE plpgsql;

-- PersonalService
CREATE FUNCTION check_personal_service_roles() RETURNS TRIGGER AS
'BEGIN
    PERFORM check_client_role(NEW.client_id);
    PERFORM check_trainer_role(NEW.trainer_id);
    RETURN NEW;
END;' LANGUAGE plpgsql;

CREATE TRIGGER personal_service_roles_check
    BEFORE INSERT OR UPDATE
    ON "personal_service"
    FOR EACH ROW
EXECUTE FUNCTION check_personal_service_roles();

-- GroupTraining
CREATE FUNCTION check_group_training_roles() RETURNS TRIGGER AS
'BEGIN
    PERFORM check_trainer_role(NEW.trainer_id);
    RETURN NEW;
END;' LANGUAGE plpgsql;

CREATE TRIGGER group_training_roles_check
    BEFORE INSERT OR UPDATE
    ON "group_training"
    FOR EACH ROW
EXECUTE FUNCTION check_group_training_roles();

-- GroupTrainingBooking
CREATE FUNCTION check_group_training_booking_roles() RETURNS TRIGGER AS
'BEGIN
    PERFORM check_client_role(NEW.client_id);
    RETURN NEW;
END;' LANGUAGE plpgsql;

CREATE TRIGGER group_training_booking_roles_check
    BEFORE INSERT OR UPDATE
    ON "group_training_booking"
    FOR EACH ROW
EXECUTE FUNCTION check_group_training_booking_roles();


-- Indexes

CREATE INDEX ON "users" USING hash (email);
