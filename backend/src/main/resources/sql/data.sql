-- Добавление записей в таблицу user_details
INSERT INTO "user_details" (birth_date, name, surname)
VALUES ('1990-01-01', 'Admin', 'Adminov');

INSERT INTO "user_details" (birth_date, name, surname)
VALUES ('1995-05-05', 'Client', 'Clientov');

INSERT INTO "user_details" (birth_date, name, surname)
VALUES ('1985-10-10', 'Trainer', 'Trainerov');

INSERT INTO "users" (email, password, role, is_confirmed)
VALUES ('admin@mail.ru', '$2a$10$vAJSpYZIPg77wKzyB0rRMOsU0MNz1VJynnGPW026p2.J3WcqRcPE.', 'ADMINISTRATOR', TRUE);

INSERT INTO "users" (email, password, role, is_confirmed)
VALUES ('client@mail.ru', '$2a$10$vAJSpYZIPg77wKzyB0rRMOsU0MNz1VJynnGPW026p2.J3WcqRcPE.', 'CLIENT', TRUE);

INSERT INTO "users" (email, password, role, is_confirmed)
VALUES ('trainer@mail.ru', '$2a$10$vAJSpYZIPg77wKzyB0rRMOsU0MNz1VJynnGPW026p2.J3WcqRcPE.', 'TRAINER', TRUE);

-- Обновление записей в таблице users для связи с user_details
UPDATE "users"
SET user_details = (SELECT id FROM "user_details" WHERE name = 'Admin')
WHERE email = 'admin@mail.ru';

UPDATE "users"
SET user_details = (SELECT id FROM "user_details" WHERE name = 'Client')
WHERE email = 'client@mail.ru';

UPDATE "users"
SET user_details = (SELECT id FROM "user_details" WHERE name = 'Trainer')
WHERE email = 'trainer@mail.ru';