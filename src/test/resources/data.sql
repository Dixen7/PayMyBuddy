INSERT INTO role(role_id, name)
values
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN');

INSERT INTO bank_account (bank_account_id, account_number, iban, bic, holder)
values
    (1, '1500', '2228222555522187', 'BIC125115', 'christophe balestrino'),
    (2, '2400', '2228222555458965', 'BIC145852', 'elodie escudero'),
    (3, '2025', '2228222541114125', 'BIC985632', 'tony escudero'),
    (4, '2087', '2228222555522785', 'BIC745896', 'mathis balestrino'),
    (5, '5100', '2228211125899632', 'BIC452366', 'clement balestrino'),
    (6, '2077', '2228022555522985', 'BIC745896', 'myriam khettab'),
    (7, '5900', '2228211145199632', 'BIC452366', 'jean brute de remur');


INSERT INTO user (user_id, email, password, first_name, last_name, bank_account_id, active)
VALUES
    (1, 'christophe@gmail.com', '$2a$10$w2CnowSA2DkceLOAiRKLH.CD2N/U0e1Krl52QsYMoE7m4xhbshssS', 'christophe', 'balestrino', (SELECT bank_account_id FROM bank_account WHERE holder = 'christophe balestrino'),1),
    (2, 'elodie@gmail.com', '$2a$10$w2CnowSA2DkceLOAiRKLH.CD2N/U0e1Krl52QsYMoE7m4xhbshssS', 'elodie', 'escudero', (SELECT bank_account_id FROM bank_account WHERE holder = 'elodie escudero'),1),
    (3, 'tony@gmail.com', '$2a$10$w2CnowSA2DkceLOAiRKLH.CD2N/U0e1Krl52QsYMoE7m4xhbshssS', 'tony', 'escudero', (SELECT bank_account_id FROM bank_account WHERE holder = 'tony escudero'),1),
    (4, 'mathis@gmail.com', '$2a$10$w2CnowSA2DkceLOAiRKLH.CD2N/U0e1Krl52QsYMoE7m4xhbshssS', 'mathis', 'balestrino', (SELECT bank_account_id FROM bank_account WHERE holder = 'mathis balestrino'),0),
    (5, 'admin@gmail.com', '$2a$10$w2CnowSA2DkceLOAiRKLH.CD2N/U0e1Krl52QsYMoE7m4xhbshssS', 'clement', 'balestrino', (SELECT bank_account_id FROM bank_account WHERE holder = 'clement balestrino'),1),
    (6, 'myriam@gmail.com', '$2a$10$w2CnowSA2DkceLOAiRKLH.CD2N/U0e1Krl52QsYMoE7m4xhbshssS', 'myriam', 'khettab', (SELECT bank_account_id FROM bank_account WHERE holder = 'myriam khettab'),1),
    (7, 'jean@gmail.com', '$2a$10$w2CnowSA2DkceLOAiRKLH.CD2N/U0e1Krl52QsYMoE7m4xhbshssS', 'jean', 'brute de remur', (SELECT bank_account_id FROM bank_account WHERE holder = 'jean brute de remur'),1);


INSERT INTO connection(user_id, user_associate_id)
values
    ((SELECT user_id from user where email='admin@gmail.com'),(SELECT user_id from user where email='elodie@gmail.com')),
    ((SELECT user_id from user where email='admin@gmail.com'),(SELECT user_id from user where email='christophe@gmail.com'));

INSERT INTO account (account_id, balance, user_id)
values
    (1,379, (SELECT user_id from user where email='christophe@gmail.com')),
    (2,20, (SELECT user_id from user where email='elodie@gmail.com')),
    (3,0, (SELECT user_id from user where email='tony@gmail.com')),
    (4,0, (SELECT user_id from user where email='mathis@gmail.com')),
    (5,401, (SELECT user_id from user where email='admin@gmail.com')),
    (6,10000, (SELECT user_id from user where email='myriam@gmail.com')),
    (7,1000000, (SELECT user_id from user where email='jean@gmail.com'));

INSERT INTO transaction(transaction_id, amount, date_transaction, type, description, fee, account_sender_id, account_beneficiary_id)
values
    (1,500, '2020/05/05', 'BANK_TRANSFER', 'premier versement', 0.0, (SELECT account_id from account where user_id = 5),(SELECT account_id from account where user_id = 1)),
    (2,20, '2020/07/05', 'USER_TO_USER', 'remboursement cadeau', 1.0,(SELECT account_id from account where user_id = 5),(SELECT account_id from account where user_id = 2)),
    (3,100, '2020/09/05', 'BANK_TRANSFER', 'retrait', 0.0,(SELECT account_id from account where user_id = 5),(SELECT account_id from account where user_id = 1));



INSERT INTO users_roles(user_id, role_id)
values
    ((SELECT user_id from user where email='christophe@gmail.com'),(SELECT role_id from role where name='ROLE_USER')),
    ((SELECT user_id from user where email='elodie@gmail.com'),(SELECT role_id from role where name='ROLE_USER')),
    ((SELECT user_id from user where email='tony@gmail.com'),(SELECT role_id from role where name='ROLE_USER')),
    ((SELECT user_id from user where email='mathis@gmail.com'),(SELECT role_id from role where name='ROLE_USER')),
    ((SELECT user_id from user where email='admin@gmail.com'),(SELECT role_id from role where name='ROLE_ADMIN')),
    ((SELECT user_id from user where email='myriam@gmail.com'),(SELECT role_id from role where name='ROLE_USER')),
    ((SELECT user_id from user where email='jean@gmail.com'),(SELECT role_id from role where name='ROLE_USER'));
