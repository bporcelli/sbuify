CREATE TABLE followed_artist (
    `customer_id` int(11),
    `artist_id` int(11),
    PRIMARY KEY (`customer_id`, `artist_id`),
    FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
    FOREIGN KEY (`artist_id`) REFERENCES `artist` (`id`)
);

CREATE TABLE followed_customer (
    `customer_id` int(11),
    `friend_id` int(11),
    PRIMARY KEY (`customer_id`, `friend_id`),
    FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
    FOREIGN KEY (`friend_id`) REFERENCES `customer` (`id`)
);

CREATE TABLE followed_playlist (
    `customer_id` int(11),
    `playlist_id` int(11),
    `position` int(11) null,
    `parent_id` int(11) null,
    PRIMARY KEY (`customer_id`, `playlist_id`),
    FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
    FOREIGN KEY (`playlist_id`) REFERENCES `playlist` (`id`),
    FOREIGN KEY (`parent_id`) REFERENCES `playlist_folder` (`id`)
);