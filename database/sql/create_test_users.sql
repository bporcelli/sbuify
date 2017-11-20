-- drop existing
SET foreign_key_checks = 0;

TRUNCATE TABLE `user`;
TRUNCATE TABLE `customer`;
TRUNCATE TABLE `admin`;
TRUNCATE TABLE `label_owner`;
TRUNCATE TABLE `customer_preferences`;
TRUNCATE TABLE `play_queue`;
TRUNCATE TABLE `play_queue_songs`;
TRUNCATE TABLE `playlist`;
TRUNCATE TABLE `playlist_folder`;
TRUNCATE TABLE `playlist_songs`;
TRUNCATE TABLE `subscription`;

SET foreign_key_checks = 1;

-- create customers
-- FIRST
INSERT INTO `user`
            (`user_type`,
             `email`,
             `password`)
VALUES      ('Customer',
             'a@sbuify.com',
             '$2a$10$.KWRUTOqHC99gANfFc4xQ.OBCOSF/LrIw2q3un7EXh6GcIEM1Hy26'); -- 'a'

SET @user_id = LAST_INSERT_ID();

INSERT INTO `playlist` 
            (`type`,
             `active`,
             `name`,
             `hidden`,
             `position`,
             `owner_id`)
VALUES      ('Playlist',
             1,
             "a's playlist",
             1,
             0,
             LAST_INSERT_ID());

SET @library_id = LAST_INSERT_ID();

INSERT INTO `play_queue` VALUES (NULL);

SET @play_queue_id = LAST_INSERT_ID();

INSERT INTO `customer`
            (`birthday`,
			 `id`,
             `first_name`,
             `last_name`,
             `library_id`,
             `play_queue_id`)
VALUES      (STR_TO_DATE('06/04/1996', '%m/%d/%Y'),
			 @user_id,
             'Brett',
             'Porcelli',
             @library_id,
             @play_queue_id);
-- END FIRST

-- SECOND
INSERT INTO `user`
            (`user_type`,
             `email`,
             `password`)
VALUES      ('Customer',
             'b@sbuify.com',
             '$2a$10$DfaIJNL0eJsPxptze2pRT.jKDUpf3B8ys4PHe3NfifhwIFJtjYnMC'); -- 'b'

SET @user_id = LAST_INSERT_ID();

INSERT INTO `playlist` 
            (`type`,
             `active`,
             `name`,
             `hidden`,
             `position`,
             `owner_id`)
VALUES      ('Playlist',
             1,
             "b's playlist",
             1,
             0,
             LAST_INSERT_ID());

SET @library_id = LAST_INSERT_ID();

INSERT INTO `play_queue` VALUES (NULL);

SET @play_queue_id = LAST_INSERT_ID();

INSERT INTO `customer`
            (`birthday`,
			 `id`,
             `first_name`,
             `last_name`,
             `library_id`,
             `play_queue_id`)
VALUES      (STR_TO_DATE('07/04/1997', '%m/%d/%Y'),
			 @user_id,
             'Jerrel',
             'Sogoni',
             @library_id,
             @play_queue_id);
-- END SECOND

-- THIRD
INSERT INTO `user`
            (`user_type`,
             `email`,
             `password`)
VALUES      ('Customer',
             'c@sbuify.com',
             '$2a$10$fBgnuGdCez4miyWfegJ9o.CKXhkAak1ZRXquNchroulkKcDV5N4KG'); -- 'c'

SET @user_id = LAST_INSERT_ID();

INSERT INTO `playlist` 
            (`type`,
             `active`,
             `name`,
             `hidden`,
             `position`,
             `owner_id`)
VALUES      ('Playlist',
             1,
             "c's playlist",
             1,
             0,
             LAST_INSERT_ID());

SET @library_id = LAST_INSERT_ID();

INSERT INTO `play_queue` VALUES (NULL);

SET @play_queue_id = LAST_INSERT_ID();

INSERT INTO `customer`
            (`birthday`,
			 `id`,
             `first_name`,
             `last_name`,
             `library_id`,
             `play_queue_id`)
VALUES      (STR_TO_DATE('11/11/1995', '%m/%d/%Y'),
			 @user_id,
             'Daniel',
             'Soh',
             @library_id,
             @play_queue_id);
-- END THIRD

-- create admins
INSERT INTO `user`
            (`user_type`,
             `email`,
             `password`)
VALUES      ('Admin',
             'admin@sbuify.com',
             '$2a$10$.KWRUTOqHC99gANfFc4xQ.OBCOSF/LrIw2q3un7EXh6GcIEM1Hy26'); -- 'a'

INSERT INTO `admin`
			(`first_name`,
             `last_name`,
             `super_admin`,
             `id`)
VALUES		('John',
			 'Doe',
             0,
             LAST_INSERT_ID());