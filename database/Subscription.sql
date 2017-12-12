Use test;

UPDATE Admin SET super_admin = 1 WHERE Admin.id = 4;

-- Manually add subscription
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE subscription;
SET FOREIGN_KEY_CHECKS=1;

-- Remove the uniquness constrain for stripe_id
ALTER TABLE subscription DROP INDEX UK_lh45n72a8s6l55lm924ddamxr;

SHOW INDEX FROM Subscription;

INSERT INTO Subscription (start, end, stripe_id) VALUES ('2015-03-01', '2018-03-01', 'stripe_id1');
SET @last_id_in_table1 = LAST_INSERT_ID();
UPDATE Customer SET subscription_id = @last_id_in_table1 WHERE customer.id = 1;

INSERT INTO Subscription (start, end, stripe_id) VALUES ('2017-03-01', '2018-03-01', 'stripe_id2');
SET @last_id_in_table1 = LAST_INSERT_ID();
UPDATE Customer SET subscription_id = @last_id_in_table1 WHERE customer.id = 2;

INSERT INTO Subscription (start, end, stripe_id) VALUES ('2017-03-01', '2018-03-01', 'stripe_id3');
SET @last_id_in_table1 = LAST_INSERT_ID();
UPDATE Customer SET subscription_id = @last_id_in_table1 WHERE customer.id = 3;

INSERT INTO Subscription (start, end, stripe_id) VALUES ('2017-11-01', '2017-11-30', 'stripe_id_853');
SET @last_id_in_table1 = LAST_INSERT_ID();
UPDATE Customer SET subscription_id = @last_id_in_table1 WHERE customer.id = 853;

-- Customer 856
INSERT INTO Subscription (start, end, stripe_id) VALUES ('2017-10-01', '2017-10-31', 'stripe_id_856');
SET @last_id_in_table1 = LAST_INSERT_ID();
UPDATE Customer SET subscription_id = @last_id_in_table1 WHERE customer.id = 856;

INSERT INTO Subscription (start, end, stripe_id) VALUES ('2017-11-01', '2017-11-30', 'stripe_id_856');
SET @last_id_in_table1 = LAST_INSERT_ID();
UPDATE Customer SET subscription_id = @last_id_in_table1 WHERE customer.id = 856;

-- Customer 860
INSERT INTO Subscription (start, end, stripe_id) VALUES ('2017-10-01', '2017-10-30', 'stripe_id_860');
SET @last_id_in_table1 = LAST_INSERT_ID();
UPDATE Customer SET subscription_id = @last_id_in_table1 WHERE customer.id = 860;

INSERT INTO Subscription (start, end, stripe_id) VALUES ('2017-12-01', '2017-12-31', 'stripe_id_860');
SET @last_id_in_table1 = LAST_INSERT_ID();
UPDATE Customer SET subscription_id = @last_id_in_table1 WHERE customer.id = 860;

-- Customer 869
INSERT INTO Subscription (start, end, stripe_id) VALUES ('2017-10-01', '2017-10-30', 'stripe_id_869');
SET @last_id_in_table1 = LAST_INSERT_ID();
UPDATE Customer SET subscription_id = @last_id_in_table1 WHERE customer.id = 869;
