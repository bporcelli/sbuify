-- RCA
INSERT INTO `address` 
		(`address_line1`,
             `address_line2`,
             `city`,
             `country`,
             `postcode`,
             `state`)
VALUES	('550 Madison Ave.',
		 '6th Fl.',
             'New York City',
             'US',
             '10022',
             'NY');

INSERT INTO label
		(`mbid`,
             `name`,
             `address_id`)
VALUES 	('1ca5ed29-e00b-4ea5-b817-0bcca0e04946',
		 'RCA',
             LAST_INSERT_ID());

-- Columbia
INSERT INTO `address` 
		(`address_line1`,
             `address_line2`,
             `city`,
             `country`,
             `postcode`,
             `state`)
VALUES	('550 Madison Ave.',
		 '24th Fl.',
             'New York City',
             'US',
             '10022',
             'NY');

INSERT INTO label
		(`mbid`,
             `name`,
             `address_id`)
VALUES 	('011d1192-6f65-45bd-85c4-0400dd45693e',
		 'Columbia',
             LAST_INSERT_ID());

-- Epic
INSERT INTO `address` 
		(`address_line1`,
             `address_line2`,
             `city`,
             `country`,
             `postcode`,
             `state`)
VALUES	('2100 Colorado Ave.',
		 '',
             'Santa Monica',
             'US',
             '90404',
             'CA');

INSERT INTO label
		(`mbid`,
             `name`,
             `address_id`)
VALUES 	('8f638ddb-131a-4cc3-b3d4-7ebdac201b55',
		 'Epic',
             LAST_INSERT_ID());

-- Epic
INSERT INTO `address` 
		(`address_line1`,
             `address_line2`,
             `city`,
             `country`,
             `postcode`,
             `state`)
VALUES	('3300 Warner Blvd.',
		 '3rd Fl.',
             'Burbank',
             'US',
             '91505',
             'CA');

INSERT INTO label
		(`mbid`,
             `name`,
             `address_id`)
VALUES 	('c595c289-47ce-4fba-b999-b87503e8cb71',
		 'Warner Bros.',
             LAST_INSERT_ID());

-- Atlantic
INSERT INTO `address` 
		(`address_line1`,
             `address_line2`,
             `city`,
             `country`,
             `postcode`,
             `state`)
VALUES	('1290 Ave. Of The Americas',
		 '',
             'New York City',
             'US',
             '10104',
             'NY');

INSERT INTO label
		(`mbid`,
             `name`,
             `address_id`)
VALUES 	('50c384a2-0b44-401b-b893-8181173339c7',
		 'Atlantic',
             LAST_INSERT_ID());