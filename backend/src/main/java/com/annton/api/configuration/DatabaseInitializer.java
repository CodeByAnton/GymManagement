package com.annton.api.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {

        // Создание функций
        jdbcTemplate.execute("CREATE OR REPLACE FUNCTION check_client_role(user_id INT) RETURNS BOOLEAN AS $$ " +
                "BEGIN " +
                "    IF (SELECT role FROM \"users\" WHERE id = user_id) = 'CLIENT' THEN " +
                "        RETURN TRUE; " +
                "    ELSE " +
                "        RAISE EXCEPTION 'User must have role CLIENT'; " +
                "    END IF; " +
                "END; $$ LANGUAGE plpgsql;");

        jdbcTemplate.execute("CREATE OR REPLACE FUNCTION check_trainer_role(user_id INT) RETURNS BOOLEAN AS $$ " +
                "BEGIN " +
                "    IF (SELECT role FROM \"users\" WHERE id = user_id) = 'TRAINER' THEN " +
                "        RETURN TRUE; " +
                "    ELSE " +
                "        RAISE EXCEPTION 'User must have role TRAINER'; " +
                "    END IF; " +
                "END; $$ LANGUAGE plpgsql;");

        jdbcTemplate.execute("CREATE OR REPLACE FUNCTION check_admin_role(user_id INT) RETURNS BOOLEAN AS $$ " +
                "BEGIN " +
                "    IF (SELECT role FROM \"users\" WHERE id = user_id) = 'ADMINISTRATOR' THEN " +
                "        RETURN TRUE; " +
                "    ELSE " +
                "        RAISE EXCEPTION 'User must have role ADMINISTRATOR'; " +
                "    END IF; " +
                "END; $$ LANGUAGE plpgsql;");

        // Создание триггеров
        jdbcTemplate.execute("CREATE OR REPLACE FUNCTION check_personal_service_roles() RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    PERFORM check_client_role(NEW.client_id); " +
                "    PERFORM check_trainer_role(NEW.trainer_id); " +
                "    RETURN NEW; " +
                "END; $$ LANGUAGE plpgsql;");

        jdbcTemplate.execute("CREATE TRIGGER personal_service_roles_check " +
                "BEFORE INSERT OR UPDATE ON \"personal_service\" " +
                "FOR EACH ROW EXECUTE FUNCTION check_personal_service_roles();");

        jdbcTemplate.execute("CREATE OR REPLACE FUNCTION check_group_training_roles() RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    PERFORM check_trainer_role(NEW.trainer_id); " +
                "    RETURN NEW; " +
                "END; $$ LANGUAGE plpgsql;");

        jdbcTemplate.execute("CREATE TRIGGER group_training_roles_check " +
                "BEFORE INSERT OR UPDATE ON \"group_training\" " +
                "FOR EACH ROW EXECUTE FUNCTION check_group_training_roles();");

        jdbcTemplate.execute("CREATE OR REPLACE FUNCTION check_group_training_booking_roles() RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    PERFORM check_client_role(NEW.client_id); " +
                "    RETURN NEW; " +
                "END; $$ LANGUAGE plpgsql;");

        jdbcTemplate.execute("CREATE TRIGGER group_training_booking_roles_check " +
                "BEFORE INSERT OR UPDATE ON \"group_training_booking\" " +
                "FOR EACH ROW EXECUTE FUNCTION check_group_training_booking_roles();");

        // Создание индексов
        jdbcTemplate.execute("CREATE INDEX ON \"users\" USING hash (email);");

        // Добавление записей в таблицу user_details
        jdbcTemplate.execute("INSERT INTO \"user_details\" (birth_date, name, surname) " +
                "VALUES ('1990-01-01', 'Admin', 'Adminov')");

        jdbcTemplate.execute("INSERT INTO \"user_details\" (birth_date, name, surname) " +
                "VALUES ('1995-05-05', 'Client', 'Clientov')");

        jdbcTemplate.execute("INSERT INTO \"user_details\" (birth_date, name, surname) " +
                "VALUES ('1985-10-10', 'Trainer', 'Trainerov')");

        // Добавление записей в таблицу users
        jdbcTemplate.execute("INSERT INTO \"users\" (email, password, role, is_confirmed) " +
                "VALUES ('admin@mail.ru', '$2a$10$vAJSpYZIPg77wKzyB0rRMOsU0MNz1VJynnGPW026p2.J3WcqRcPE.', 'ADMINISTRATOR', TRUE)");

        jdbcTemplate.execute("INSERT INTO \"users\" (email, password, role, is_confirmed) " +
                "VALUES ('client@mail.ru', '$2a$10$vAJSpYZIPg77wKzyB0rRMOsU0MNz1VJynnGPW026p2.J3WcqRcPE.', 'CLIENT', TRUE)");

        jdbcTemplate.execute("INSERT INTO \"users\" (email, password, role, is_confirmed) " +
                "VALUES ('trainer@mail.ru', '$2a$10$vAJSpYZIPg77wKzyB0rRMOsU0MNz1VJynnGPW026p2.J3WcqRcPE.', 'TRAINER', TRUE)");

        // Обновление записей в таблице users для связи с user_details
        jdbcTemplate.execute("UPDATE \"users\" " +
                "SET user_details = (SELECT id FROM \"user_details\" WHERE name = 'Admin') " +
                "WHERE email = 'admin@mail.ru'");

        jdbcTemplate.execute("UPDATE \"users\" " +
                "SET user_details = (SELECT id FROM \"user_details\" WHERE name = 'Client') " +
                "WHERE email = 'client@mail.ru'");

        jdbcTemplate.execute("UPDATE \"users\" " +
                "SET user_details = (SELECT id FROM \"user_details\" WHERE name = 'Trainer') " +
                "WHERE email = 'trainer@mail.ru'");
    }
}