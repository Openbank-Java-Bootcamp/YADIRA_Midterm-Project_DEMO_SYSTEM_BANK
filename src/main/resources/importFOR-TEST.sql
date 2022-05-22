-- roles
insert into role (id, name) VALUES (1, 'ACCOUNT_HOLDER');
insert into role (name) VALUES ('ADMIN');

-- admin

INSERT INTO user (role_name, name, password, username, role_id) VALUES ('ADMIN', 'Jose Luis', '$2a$10$gOfDyxLGEgVuBSfHtdAWluOttI8dZ3obQanQFMLp7ltP26SC49eIG', 'jose', 2);

-- account holders

INSERT INTO system_bank.user(role_name, date_of_birth, mailing_city, mailing_street, mailing_zip_code, name, password, city, street_address, zip_code, username, role_id) VALUES ('ACCOUNT_HOLDER','1985-10-11', 'Madrid', 'Pavones 4', '28036', 'Yadira Calzadilla', '$2a$10$gOfDyxLGEgVuBSfHtdAWluOttI8dZ3obQanQFMLp7ltP26SC49eIG', 'Madrid', 'Pavones 4', '28036', 'yadi', 1 );

INSERT INTO system_bank.user(role_name, date_of_birth, mailing_city, mailing_street, mailing_zip_code, name, password, city, street_address, zip_code, username, role_id) VALUES ('ACCOUNT_HOLDER','1978-12-11', 'Madrid', 'Pavones 4', '28036', 'Elena Lopez', '$2a$10$gOfDyxLGEgVuBSfHtdAWluOttI8dZ3obQanQFMLp7ltP26SC49eIG', 'Madrid', 'Pavones 4', '28036', 'elena', 1 );

INSERT INTO system_bank.user(role_name, date_of_birth, mailing_city, mailing_street, mailing_zip_code, name, password, city, street_address, zip_code, username, role_id) VALUES ('ACCOUNT_HOLDER','2000-12-11', 'Madrid', 'Universidad 6', '28911', 'Camila Lopez', '$2a$10$gOfDyxLGEgVuBSfHtdAWluOttI8dZ3obQanQFMLp7ltP26SC49eIG', 'Madrid', 'Pavones 4', '28036', 'cami', 1 );

-- accounts

insert into system_bank.account (type, amount, currency, creation_date, last_consultation, penalty_fee, secret_key, status, minimum_balance, monthly_maintenance_fee, interest_rate, primary_owner_id, secondary_owner_id) VALUES ('saving', '2000.0000', 'USD', '2020-02-10', '2020-02-10', '40', '123', 'ACTIVE', 1000.0000, null, 0.0025, 2, null);

insert into system_bank.account (type, amount, currency, creation_date, last_consultation, penalty_fee, secret_key, status, minimum_balance, monthly_maintenance_fee, interest_rate, primary_owner_id, secondary_owner_id) VALUES ('checking', '90.0000', 'USD', '2021-12-10', '2021-12-10', '40', '111', 'ACTIVE', 250, 12, null, 3, 2);

insert into system_bank.account (type, amount, currency, creation_date, last_consultation, penalty_fee, secret_key, status, minimum_balance, monthly_maintenance_fee, interest_rate, primary_owner_id, secondary_owner_id) VALUES ('student_checking', '800.0000', 'USD', '2021-12-10','2021-12-10', 0, '321', 'ACTIVE', null, null, null, 4, null);

-- credit card

insert into system_bank.credit_card (amount, currency, credit_limit, interest_rate, last_consultation, primary_owner_id, secondary_owner_id) VALUES (2300.0000, 'USD', 500.0000, 0.1500, '2015-10-20', 4, null);

-- third party

insert into system_bank.third_party (hashed_key, name) VALUES ('3721ceb154c49efcb0d5a425c68410197f3872651df9fc37724cf9a9cda6c1e2', 'Albert');

-- transfer

insert into system_bank.transfer (amount, currency, date, receiver_account_id, receiver_third_party_id, sender_account_id, sender_third_party_id) values (50.0000, 'USD', '2022-02-20 17:12:27.972000', 1, null, 2, null);

insert into system_bank.transfer (amount, currency, date, receiver_account_id, receiver_third_party_id, sender_account_id, sender_third_party_id) values (50.0000, 'USD', '2022-02-20 19:12:27.972000', 1, null, 2, null);

insert into system_bank.transfer (amount, currency, date, receiver_account_id, receiver_third_party_id, sender_account_id, sender_third_party_id) values (30.0000, 'USD', '2022-02-25 19:12:27.972000', 1, null, 2, null);

insert into system_bank.transfer (amount, currency, date, receiver_account_id, receiver_third_party_id, sender_account_id, sender_third_party_id) values (300.0000, 'USD', '2022-02-21 19:12:27.972000', 2, null, 3, null);

insert into system_bank.transfer (amount, currency, date, receiver_account_id, receiver_third_party_id, sender_account_id, sender_third_party_id) values (300.0000, 'USD', '2022-02-21 19:12:27.972001', 2, null, null, 1);

insert into system_bank.transfer (amount, currency, date, receiver_account_id, receiver_third_party_id, sender_account_id, sender_third_party_id) values (300.0000, 'USD', '2022-02-21 19:12:27.972001', 3, null, null, 1);