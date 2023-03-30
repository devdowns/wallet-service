-- Dummy data for user table
INSERT INTO public.user(name, surname, email, password)
VALUES ('Little', 'Prince',  'little@prince.com', '$2a$12$E1pxxifMLHfQ2/2g/nc8UOZ6Ukfx6m1TJFnYRGm3.eUnzF1w6JvZ6'),
       ('The', 'Fox', 'the@fox.com', '$2a$12$E1pxxifMLHfQ2/2g/nc8UOZ6Ukfx6m1TJFnYRGm3.eUnzF1w6JvZ6');

-- Dummy data for bank_account table
INSERT INTO bank_account(name, surname, national_id, account_number, routing_number, bank_name)
VALUES ('OnTop', 'Bank', '1234567890', '1234567890123456', '123456789', 'OnTop'),
       ('Little', 'Prince', '0987654321', '9876543210987654', '987654321', 'B612'),
       ('The', 'Fox', '4567890123', '4567890123456789', '456789012', 'Desert');

-- Define variable to hold OnTop bank account id
DO $$
DECLARE ontop_bank INTEGER;
DECLARE b612_bank INTEGER;
DECLARE desert_bank INTEGER;
DECLARE little_prince_id INTEGER;
DECLARE the_fox_id INTEGER;
BEGIN
SELECT id INTO ontop_bank FROM bank_account WHERE bank_name = 'OnTop';
SELECT id INTO b612_bank FROM bank_account WHERE bank_name = 'B612';
SELECT id INTO desert_bank FROM bank_account WHERE bank_name = 'Desert';
SELECT id INTO little_prince_id FROM "user" WHERE email = 'little@prince.com';
SELECT id INTO the_fox_id FROM "user" WHERE email = 'the@fox.com';

-- Insert dummy data into wallet table
INSERT INTO wallet(balance, bank_account_id, user_id)
VALUES (10000, ontop_bank, little_prince_id),
       (5000, ontop_bank, the_fox_id);

-- Dummy data for transaction table
-- INSERT INTO public.transaction(amount, transaction_date, transaction_status, transaction_type,from_account, to_account )
-- VALUES (1000, NOW(), 'Failed', 'Withdraw', ontop_bank, b612_bank),
--        (2000, NOW(), 'Completed', 'Deposit', b612_bank, ontop_bank),
--        (500, NOW(), 'Processing', 'Deposit', desert_bank, ontop_bank);
END $$;
