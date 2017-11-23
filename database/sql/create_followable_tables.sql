CREATE TABLE `customer_playlists` (
	`customer_id` int(11),
    `playlist_id` int(11),
    PRIMARY KEY (`customer_id`, `playlist_id`),
    FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`playlist_id`) REFERENCES `playlist` (`id`) ON DELETE CASCADE
);

CREATE TABLE `customer_friends` (
	`customer_id` int(11),
    `friend_id` int(11),
    PRIMARY KEY (`customer_id`, `friend_id`),
    FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`friend_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE
);

CREATE TABLE `customer_artists` (
	`customer_id` int(11),
    `artist_id` int(11),
    PRIMARY KEY (`customer_id`, `artist_id`),
    FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`) ON DELETE CASCADE
);

CREATE TABLE `customer_followers` (
	`customer_id` int(11),
    `follower_id` int(11),
    PRIMARY KEY (`customer_id`, `follower_id`),
    FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE,
	FOREIGN KEY (`follower_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE
);

CREATE TABLE `playlist_followers` (
	`playlist_id` int(11),
    `follower_id` int(11),
    PRIMARY KEY (`playlist_id`, `follower_id`),
    FOREIGN KEY (`playlist_id`) REFERENCES `playlist` (`id`) ON DELETE CASCADE,
	FOREIGN KEY (`follower_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE
);

CREATE TABLE `artist_followers` (
	`artist_id` int(11),
    `follower_id` int(11),
    PRIMARY KEY (`artist_id`, `follower_id`),
    FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`) ON DELETE CASCADE,
	FOREIGN KEY (`follower_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE
);