CREATE DATABASE IF NOT EXISTS tdb; USE tdb;

CREATE TABLE IF NOT EXISTS `client` (
                                        `client_id` bigint NOT NULL AUTO_INCREMENT,
                                        `email` varchar(255) DEFAULT NULL,
                                        `name` varchar(255) DEFAULT NULL,
                                        `password` varchar(255) DEFAULT NULL,
                                        `role` varchar(255) DEFAULT NULL,
                                        PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `driver` (
                                        `driver_id` bigint NOT NULL AUTO_INCREMENT,
                                        `email` varchar(255) DEFAULT NULL,
                                        `name` varchar(255) DEFAULT NULL,
                                        `password` varchar(255) DEFAULT NULL,
                                        `role` varchar(255) DEFAULT NULL,
                                        `is_busy` bit(1) DEFAULT NULL,
                                        PRIMARY KEY (`driver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `car` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `model` varchar(255) DEFAULT NULL,
                                     `driver_id` bigint DEFAULT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY (`driver_id`),
                                     FOREIGN KEY (`driver_id`) REFERENCES `driver` (`driver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `order` (
                                       `order_id` bigint NOT NULL AUTO_INCREMENT,
                                       `price` double DEFAULT NULL,
                                       `driver_id` bigint DEFAULT NULL,
                                       `client_id` bigint DEFAULT NULL,
                                       PRIMARY KEY (`order_id`),
                                       KEY (`driver_id`),
                                       KEY (`client_id`),
                                       FOREIGN KEY (`driver_id`) REFERENCES `driver` (`driver_id`),
                                       FOREIGN KEY (`client_id`) REFERENCES `client` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



