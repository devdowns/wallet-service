ALTER TABLE bank_account
    ALTER COLUMN balance SET DEFAULT 0.00;

ALTER TABLE wallet_transaction
    ALTER COLUMN transaction_status SET DEFAULT 'PROCESSING';

ALTER TABLE wallet_transaction
    ALTER COLUMN created_at SET DEFAULT NOW();

-- Dummy data for user table
INSERT INTO public.user(name, surname, email, password)
VALUES ('Little', 'Prince', 'little@prince.com',
        '$2a$12$E1pxxifMLHfQ2/2g/nc8UOZ6Ukfx6m1TJFnYRGm3.eUnzF1w6JvZ6'),
       ('The', 'Fox', 'the@fox.com',
        '$2a$12$E1pxxifMLHfQ2/2g/nc8UOZ6Ukfx6m1TJFnYRGm3.eUnzF1w6JvZ6');

-- Dummy data for bank_account table
INSERT INTO bank_account(name, surname, national_id, account_number, routing_number, bank_name,
                         balance)
VALUES ('OnTop', 'Bank', '1234567890', '1234567890123456', '123456789', 'OnTop', 10000000),
       ('Little', 'Prince', '0987654321', '9876543210987654', '987654321', 'B612', 20000),
       ('The', 'Fox', '4567890123', '4567890123456789', '456789012', 'Desert', 30000);

INSERT INTO wallet(balance, bank_account_id, user_id)
VALUES (10000, 1, 1),
       (5000, 1, 2);
