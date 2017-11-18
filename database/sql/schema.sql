-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: test2
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address_line1` varchar(255) NOT NULL,
  `address_line2` varchar(255) DEFAULT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `postcode` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `super_admin` bit(1) NOT NULL,
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK1ja8rua032fgnk9jmq7du3b3a` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `album`
--

DROP TABLE IF EXISTS `album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `album` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL DEFAULT b'1',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(255) NOT NULL,
  `mbid` char(36) NOT NULL,
  `num_songs` int(11) NOT NULL DEFAULT '0',
  `release_date` datetime DEFAULT NULL,
  `image_id` int(11) DEFAULT NULL,
  `owner_id` int(11) DEFAULT NULL,
  `artist_id` int(11) NOT NULL,
  `length` int(11) NOT NULL DEFAULT '0',
  `type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_driqobnir6h5lpxjh3tp9wl60` (`mbid`),
  KEY `FKrso6yh9eofot314029iyqpvqx` (`image_id`),
  KEY `FKb30xrq6mdr8pv80vafw2fvdam` (`owner_id`),
  KEY `FKmwc4fyyxb6tfi0qba26gcf8s1` (`artist_id`),
  CONSTRAINT `FKb30xrq6mdr8pv80vafw2fvdam` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKmwc4fyyxb6tfi0qba26gcf8s1` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`),
  CONSTRAINT `FKrso6yh9eofot314029iyqpvqx` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2296 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `album_songs`
--

DROP TABLE IF EXISTS `album_songs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `album_songs` (
  `album_id` int(11) NOT NULL,
  `song_id` int(11) NOT NULL,
  PRIMARY KEY (`album_id`,`song_id`),
  UNIQUE KEY `UK_nhrqol95oahelegmtmrmv7tf` (`song_id`),
  CONSTRAINT `FK4p67gti7olml2nebymwwgx3uw` FOREIGN KEY (`album_id`) REFERENCES `album` (`id`),
  CONSTRAINT `FKefchjqwks846oxggnhrg4jypo` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `artist`
--

DROP TABLE IF EXISTS `artist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL DEFAULT b'1',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(255) NOT NULL,
  `monthly_listeners` int(11) NOT NULL DEFAULT '0',
  `mbid` char(36) NOT NULL,
  `image_id` int(11) DEFAULT NULL,
  `owner_id` int(11) DEFAULT NULL,
  `cover_image_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_b6mw7vk7h2nwfhaisad3vnstf` (`mbid`),
  KEY `FKhya76u0yfcqx6sr4qhx1ny6nk` (`image_id`),
  KEY `FKetdfw4flyg4n0p062lejw91m7` (`owner_id`),
  KEY `FKsmh3342g8ftc9elcki2nvlhoh` (`cover_image_id`),
  CONSTRAINT `FKetdfw4flyg4n0p062lejw91m7` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKhya76u0yfcqx6sr4qhx1ny6nk` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FKsmh3342g8ftc9elcki2nvlhoh` FOREIGN KEY (`cover_image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1541 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `artist_albums`
--

DROP TABLE IF EXISTS `artist_albums`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist_albums` (
  `artist_id` int(11) NOT NULL,
  `album_id` int(11) NOT NULL,
  UNIQUE KEY `UK_hyog1vavjscecgdqewaifhsky` (`album_id`),
  KEY `FKbcvl559apfrlou44wn8x039f4` (`artist_id`),
  CONSTRAINT `FKbcvl559apfrlou44wn8x039f4` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`),
  CONSTRAINT `FKtd9i1se0noi8swo0y6c20hie7` FOREIGN KEY (`album_id`) REFERENCES `album` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `artist_aliases`
--

DROP TABLE IF EXISTS `artist_aliases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist_aliases` (
  `artist_id` int(11) NOT NULL,
  `alias` varchar(255) NOT NULL,
  KEY `FK9fw37tjt9r1bbjs3q2gjw4t0r` (`artist_id`),
  CONSTRAINT `FK9fw37tjt9r1bbjs3q2gjw4t0r` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `artist_merchandise`
--

DROP TABLE IF EXISTS `artist_merchandise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist_merchandise` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `purchase_url` varchar(255) NOT NULL,
  `artist_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9kvrcx7so145dw76x3sgvydr5` (`artist_id`),
  CONSTRAINT `artist_merchandise_ibfk_1` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `artist_related_artists`
--

DROP TABLE IF EXISTS `artist_related_artists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist_related_artists` (
  `artist_id` int(11) NOT NULL,
  `related_artists_id` int(11) NOT NULL,
  PRIMARY KEY (`artist_id`,`related_artists_id`),
  KEY `related_artists_id` (`related_artists_id`),
  CONSTRAINT `artist_related_artists_ibfk_1` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`),
  CONSTRAINT `artist_related_artists_ibfk_2` FOREIGN KEY (`related_artists_id`) REFERENCES `artist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `artist_request`
--

DROP TABLE IF EXISTS `artist_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `artist_id` int(11) NOT NULL,
  `label_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKao8rt6ev7vc6p3ryo91bt662e` (`artist_id`),
  KEY `FK56nav087haspqlp3fiimjoa75` (`label_id`),
  CONSTRAINT `FKao8rt6ev7vc6p3ryo91bt662e` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`),
  CONSTRAINT `artist_request_ibfk_1` FOREIGN KEY (`label_id`) REFERENCES `label_owner` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biography`
--

DROP TABLE IF EXISTS `biography`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biography` (
  `id` int(11) NOT NULL,
  `text` text,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKaaq3q74ww7n4cufqn8vwod44e` FOREIGN KEY (`id`) REFERENCES `artist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biography_images`
--

DROP TABLE IF EXISTS `biography_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biography_images` (
  `biography_id` int(11) NOT NULL,
  `image_id` int(11) NOT NULL,
  UNIQUE KEY `UK_mp16aowq1ptyq7pgjhd3uj6sj` (`image_id`),
  KEY `FK85o4yh702nhe9otgbis09k9vb` (`biography_id`),
  CONSTRAINT `FK85o4yh702nhe9otgbis09k9vb` FOREIGN KEY (`biography_id`) REFERENCES `biography` (`id`),
  CONSTRAINT `FKnr188x1qgav96xr73iedm1ujc` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chart`
--

DROP TABLE IF EXISTS `chart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chart` (
  `date` datetime NOT NULL,
  `id` int(11) NOT NULL,
  `previous_chart_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe3cwsgiy1wms3yd0mqiblkm7a` (`previous_chart_id`),
  CONSTRAINT `FK3dyu28f4geujii1ln3o6gu17c` FOREIGN KEY (`id`) REFERENCES `playlist` (`id`),
  CONSTRAINT `FKe3cwsgiy1wms3yd0mqiblkm7a` FOREIGN KEY (`previous_chart_id`) REFERENCES `chart` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `concert`
--

DROP TABLE IF EXISTS `concert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concert` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL DEFAULT b'1',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(255) NOT NULL,
  `time` datetime NOT NULL,
  `image_id` int(11) DEFAULT NULL,
  `owner_id` int(11) DEFAULT NULL,
  `venue_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7jenovp2ww39ssjyvvq7oigt2` (`image_id`),
  KEY `FKgijck1akuttcymxqhfd53bdyp` (`owner_id`),
  KEY `FKllw6cymtwrumecg1bcnsubw56` (`venue_id`),
  CONSTRAINT `FK7jenovp2ww39ssjyvvq7oigt2` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FKgijck1akuttcymxqhfd53bdyp` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKllw6cymtwrumecg1bcnsubw56` FOREIGN KEY (`venue_id`) REFERENCES `venue` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `concert_line_up`
--

DROP TABLE IF EXISTS `concert_line_up`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `concert_line_up` (
  `concert_id` int(11) NOT NULL,
  `artist_id` int(11) NOT NULL,
  PRIMARY KEY (`concert_id`,`artist_id`),
  KEY `artist_id` (`artist_id`),
  CONSTRAINT `FKqwic4ej9nrox2hfeqsy5k22ll` FOREIGN KEY (`concert_id`) REFERENCES `concert` (`id`),
  CONSTRAINT `concert_line_up_ibfk_1` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `birthday` datetime NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `id` int(11) NOT NULL,
  `library_id` int(11) NOT NULL,
  `play_queue_id` int(11) NOT NULL,
  `profile_image_id` int(11) DEFAULT NULL,
  `subscription_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl9r4verujnjudjivhni1nd4ym` (`library_id`),
  KEY `FKp427dndq05nhsvvdht5f531cq` (`play_queue_id`),
  KEY `FKfch8uho9nr0pf5p22ochdq001` (`profile_image_id`),
  KEY `FKbv6jvaps60yd0wm6qhb87p5ga` (`subscription_id`),
  CONSTRAINT `FKbv6jvaps60yd0wm6qhb87p5ga` FOREIGN KEY (`subscription_id`) REFERENCES `subscription` (`id`),
  CONSTRAINT `FKfch8uho9nr0pf5p22ochdq001` FOREIGN KEY (`profile_image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FKg2o3t8h0g17smtr9jgypagdtv` FOREIGN KEY (`id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKp427dndq05nhsvvdht5f531cq` FOREIGN KEY (`play_queue_id`) REFERENCES `play_queue` (`id`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`library_id`) REFERENCES `playlist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_preferences`
--

DROP TABLE IF EXISTS `customer_preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_preferences` (
  `customer_id` int(11) NOT NULL,
  `pref_value` varchar(255) DEFAULT NULL,
  `pref_key` varchar(255) NOT NULL,
  PRIMARY KEY (`customer_id`,`pref_key`),
  CONSTRAINT `FKgtc79fd21ph77dxs1vn6bke4e` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `genre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `image_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `image_id` (`image_id`),
  CONSTRAINT `genre_ibfk_1` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=836 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1174 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL DEFAULT b'1',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(255) NOT NULL,
  `mbid` char(36) NOT NULL,
  `image_id` int(11) DEFAULT NULL,
  `owner_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_m2qc3p1vsclhg4gejf93n495h` (`mbid`),
  KEY `FKpkuveyf6aygegr0j9qpss2qgw` (`image_id`),
  KEY `FK2q8wuoo70p3lwoutju8yvcgk7` (`owner_id`),
  CONSTRAINT `FK2q8wuoo70p3lwoutju8yvcgk7` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKpkuveyf6aygegr0j9qpss2qgw` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `label_owner`
--

DROP TABLE IF EXISTS `label_owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `label_owner` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `label_owner_ibfk_1` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL,
  `status` varchar(255) NOT NULL DEFAULT 'PENDING_PAYMENT',
  `payee_id` int(11) NOT NULL,
  `period_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs6fue04dwm42nhkb9ivrdlfp4` (`payee_id`),
  KEY `period_id` (`period_id`),
  CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`payee_id`) REFERENCES `label` (`id`),
  CONSTRAINT `payment_ibfk_2` FOREIGN KEY (`period_id`) REFERENCES `payment_period` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_period`
--

DROP TABLE IF EXISTS `payment_period`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_period` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `play_queue`
--

DROP TABLE IF EXISTS `play_queue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `play_queue` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `play_queue_songs`
--

DROP TABLE IF EXISTS `play_queue_songs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `play_queue_songs` (
  `play_queue_id` int(11) NOT NULL,
  `song_id` int(11) NOT NULL,
  KEY `play_queue_id` (`play_queue_id`),
  KEY `song_id` (`song_id`),
  CONSTRAINT `play_queue_songs_ibfk_1` FOREIGN KEY (`play_queue_id`) REFERENCES `play_queue` (`id`),
  CONSTRAINT `play_queue_songs_ibfk_2` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `playlist`
--

DROP TABLE IF EXISTS `playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `playlist` (
  `type` varchar(31) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL DEFAULT b'1',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `hidden` bit(1) NOT NULL,
  `position` int(11) NOT NULL,
  `image_id` int(11) DEFAULT NULL,
  `owner_id` int(11) NOT NULL,
  `parent_folder_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrlt93rdc0w07juyqnui9nc4nd` (`image_id`),
  KEY `FKth9shh6b71k2k9f8sdtuedunf` (`owner_id`),
  KEY `FK8e64e3ur48ib82wr43rmbsb7t` (`parent_folder_id`),
  CONSTRAINT `FK8e64e3ur48ib82wr43rmbsb7t` FOREIGN KEY (`parent_folder_id`) REFERENCES `playlist_folder` (`id`),
  CONSTRAINT `FKrlt93rdc0w07juyqnui9nc4nd` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FKth9shh6b71k2k9f8sdtuedunf` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `playlist_albums`
--

DROP TABLE IF EXISTS `playlist_albums`;
/*!50001 DROP VIEW IF EXISTS `playlist_albums`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `playlist_albums` AS SELECT 
 1 AS `playlist_id`,
 1 AS `album_id`,
 1 AS `date_saved`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `playlist_artists`
--

DROP TABLE IF EXISTS `playlist_artists`;
/*!50001 DROP VIEW IF EXISTS `playlist_artists`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `playlist_artists` AS SELECT 
 1 AS `playlist_id`,
 1 AS `artist_id`,
 1 AS `date_saved`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `playlist_folder`
--

DROP TABLE IF EXISTS `playlist_folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `playlist_folder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `position` int(11) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `parent_folder_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3gsy9iowwvpaa0ss6xre561rg` (`owner_id`),
  KEY `FKqnf78vu5x5s7kuph0f6kcy4io` (`parent_folder_id`),
  CONSTRAINT `FK3gsy9iowwvpaa0ss6xre561rg` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKqnf78vu5x5s7kuph0f6kcy4io` FOREIGN KEY (`parent_folder_id`) REFERENCES `playlist_folder` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `playlist_songs`
--

DROP TABLE IF EXISTS `playlist_songs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `playlist_songs` (
  `playlist_id` int(11) NOT NULL,
  `song_id` int(11) NOT NULL,
  `date_saved` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`playlist_id`,`song_id`),
  KEY `song_id` (`song_id`),
  CONSTRAINT `playlist_songs_ibfk_1` FOREIGN KEY (`playlist_id`) REFERENCES `playlist` (`id`),
  CONSTRAINT `playlist_songs_ibfk_2` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `song`
--

DROP TABLE IF EXISTS `song`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `song` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL DEFAULT b'1',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(255) NOT NULL,
  `mbid` char(36) NOT NULL,
  `length` int(11) NOT NULL,
  `play_count` int(11) NOT NULL DEFAULT '0',
  `track_number` int(11) NOT NULL,
  `image_id` int(11) DEFAULT NULL,
  `owner_id` int(11) DEFAULT NULL,
  `album_id` int(11) NOT NULL,
  `lyrics` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mbid` (`mbid`),
  KEY `FKe8f7vxtf0ddtotbpl28dk8voy` (`image_id`),
  KEY `FKfkw4q5q2b1fd0q2belp9olb2a` (`owner_id`),
  KEY `FKrcjmk41yqj3pl3iyii40niab0` (`album_id`),
  CONSTRAINT `FKe8f7vxtf0ddtotbpl28dk8voy` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FKfkw4q5q2b1fd0q2belp9olb2a` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKrcjmk41yqj3pl3iyii40niab0` FOREIGN KEY (`album_id`) REFERENCES `album` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22910 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_album_when_song_inserted
AFTER INSERT ON song
FOR EACH ROW
BEGIN
    CALL update_album_length(NEW.album_id, 0, NEW.LENGTH);
    CALL update_album_song_count(NEW.album_id, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_album_when_song_updated
AFTER UPDATE ON song
FOR EACH ROW
    CALL update_album_length(NEW.album_id, OLD.LENGTH, NEW.LENGTH) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_album_when_song_deleted
BEFORE DELETE ON song
FOR EACH ROW
BEGIN
    CALL update_album_length(OLD.album_id, OLD.LENGTH, 0);
    CALL update_album_song_count(OLD.album_id, -1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `song_featured_artists`
--

DROP TABLE IF EXISTS `song_featured_artists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `song_featured_artists` (
  `song_id` int(11) NOT NULL,
  `artist_id` int(11) NOT NULL,
  PRIMARY KEY (`song_id`,`artist_id`),
  KEY `artist_id` (`artist_id`),
  CONSTRAINT `FKbheih9peptu94q465lyk954pt` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`),
  CONSTRAINT `song_featured_artists_ibfk_1` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `song_genres`
--

DROP TABLE IF EXISTS `song_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `song_genres` (
  `song_id` int(11) NOT NULL,
  `genre_id` int(11) NOT NULL,
  PRIMARY KEY (`song_id`,`genre_id`),
  KEY `genre_id` (`genre_id`),
  CONSTRAINT `song_genres_ibfk_1` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`),
  CONSTRAINT `song_genres_ibfk_2` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `song_label`
--

DROP TABLE IF EXISTS `song_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `song_label` (
  `song_id` int(11) NOT NULL,
  `label_id` int(11) NOT NULL,
  PRIMARY KEY (`song_id`,`label_id`),
  KEY `label_id` (`label_id`),
  CONSTRAINT `song_label_ibfk_1` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`),
  CONSTRAINT `song_label_ibfk_2` FOREIGN KEY (`label_id`) REFERENCES `label` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stream`
--

DROP TABLE IF EXISTS `stream`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stream` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `premium` bit(1) NOT NULL,
  `time` datetime NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `playlist_id` int(11) DEFAULT NULL,
  `song_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbnhx4dknfan9ht1gq30ta241f` (`customer_id`),
  KEY `FK1rscd8rh4d8fwwdob25kncn4n` (`playlist_id`),
  KEY `FKqs0bap23gyohv733875udt0f2` (`song_id`),
  CONSTRAINT `FK1rscd8rh4d8fwwdob25kncn4n` FOREIGN KEY (`playlist_id`) REFERENCES `playlist` (`id`),
  CONSTRAINT `FKbnhx4dknfan9ht1gq30ta241f` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FKqs0bap23gyohv733875udt0f2` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stream_time_ranges`
--

DROP TABLE IF EXISTS `stream_time_ranges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stream_time_ranges` (
  `id` int(11) NOT NULL,
  `end` double NOT NULL,
  `start` double NOT NULL,
  `stream_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKaoit7stwtwn3axe39fdq0yqum` (`stream_id`),
  CONSTRAINT `stream_time_ranges_ibfk_1` FOREIGN KEY (`stream_id`) REFERENCES `stream` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subscription`
--

DROP TABLE IF EXISTS `subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `end` datetime DEFAULT NULL,
  `start` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `stripe_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lh45n72a8s6l55lm924ddamxr` (`stripe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_type` varchar(31) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `venue`
--

DROP TABLE IF EXISTS `venue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `venue` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `address_id` (`address_id`),
  CONSTRAINT `venue_ibfk_1` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'test2'
--
/*!50003 DROP PROCEDURE IF EXISTS `update_album_length` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_album_length`(IN album_id int,
                 IN old_song_length int,
                 IN new_song_length int)
UPDATE album
SET length = length - old_song_length + new_song_length
WHERE id = album_id ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_album_song_count` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_album_song_count`(IN album_id int,
                 IN delta int)
UPDATE album
SET num_songs = num_songs + delta
WHERE id = album_id ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `playlist_albums`
--

/*!50001 DROP VIEW IF EXISTS `playlist_albums`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `playlist_albums` AS select `ps`.`playlist_id` AS `playlist_id`,`s`.`album_id` AS `album_id`,min(`ps`.`date_saved`) AS `date_saved` from (`playlist_songs` `ps` join `song` `s`) where (`ps`.`song_id` = `s`.`id`) group by `ps`.`playlist_id`,`s`.`album_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `playlist_artists`
--

/*!50001 DROP VIEW IF EXISTS `playlist_artists`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `playlist_artists` AS select `ps`.`playlist_id` AS `playlist_id`,`a`.`artist_id` AS `artist_id`,min(`ps`.`date_saved`) AS `date_saved` from ((`playlist_songs` `ps` join `song` `s`) join `album` `a`) where ((`ps`.`song_id` = `s`.`id`) and (`s`.`album_id` = `a`.`id`)) group by `ps`.`playlist_id`,`a`.`artist_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-11-18 17:23:04
