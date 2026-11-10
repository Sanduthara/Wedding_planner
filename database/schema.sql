CREATE DATABASE IF NOT EXISTS weddingplanner_vendor;
USE weddingplanner_vendor;
SET FOREIGN_KEY_CHECKS=0;
CREATE TABLE `admin` (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(120) NOT NULL,
  `username` varchar(80) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(40) NOT NULL DEFAULT 'ADMIN',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `uk_admin_username` (`username`),
  UNIQUE KEY `uk_admin_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `booking` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `event_id` int DEFAULT NULL,
  `customer_id` int NOT NULL,
  `booking_date` date NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `status` varchar(40) NOT NULL DEFAULT 'PENDING',
  `total_cost` decimal(12,2) NOT NULL DEFAULT '0.00',
  `payment_type` varchar(40) NOT NULL DEFAULT 'FULL',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`booking_id`),
  KEY `idx_booking_event` (`event_id`),
  KEY `idx_booking_customer` (`customer_id`),
  CONSTRAINT `fk_booking_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_booking_event` FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `booking_package` (
  `booking_package_id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int NOT NULL,
  `event_package_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `price_at_booking` decimal(12,2) NOT NULL,
  `vendor_status` varchar(40) NOT NULL DEFAULT 'PENDING',
  `rejection_reason` text,
  `vendor_responded_at` timestamp NULL DEFAULT NULL,
  `notes` text,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`booking_package_id`),
  KEY `idx_booking_package_booking` (`booking_id`),
  KEY `idx_booking_package_event_package` (`event_package_id`),
  CONSTRAINT `fk_booking_package_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_booking_package_event_package` FOREIGN KEY (`event_package_id`) REFERENCES `event_package` (`event_package_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `category` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(120) NOT NULL,
  `description` text,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `uk_category_name` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `company_finance` (
  `finance_id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(40) NOT NULL,
  `booking_id` int DEFAULT NULL,
  `payment_id` int DEFAULT NULL,
  `amount` decimal(12,2) NOT NULL,
  `description` text,
  `payment_method` varchar(60) DEFAULT NULL,
  `transaction_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`finance_id`),
  KEY `idx_company_finance_transaction_date` (`transaction_date`),
  KEY `idx_company_finance_booking` (`booking_id`),
  KEY `idx_company_finance_payment` (`payment_id`),
  CONSTRAINT `fk_company_finance_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`) ON DELETE SET NULL,
  CONSTRAINT `fk_company_finance_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `customer` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(30) DEFAULT NULL,
  `name` varchar(120) NOT NULL,
  `username` varchar(80) NOT NULL,
  `customer_role` varchar(40) DEFAULT NULL,
  `other_party_name` varchar(120) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(40) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `uk_customer_username` (`username`),
  UNIQUE KEY `uk_customer_email` (`email`),
  UNIQUE KEY `uk_customer_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `event` (
  `event_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `event_name` varchar(160) NOT NULL,
  `event_type` varchar(80) DEFAULT NULL,
  `event_date` date DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `description` text,
  `status` varchar(40) NOT NULL DEFAULT 'PENDING',
  `event_rating` int DEFAULT NULL,
  `event_review` text,
  `reviewed_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`event_id`),
  KEY `idx_event_customer_status` (`customer_id`,`status`),
  CONSTRAINT `fk_event_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `event_package` (
  `event_package_id` int NOT NULL AUTO_INCREMENT,
  `event_id` int NOT NULL,
  `package_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `notes` text,
  `added_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`event_package_id`),
  KEY `idx_event_package_event` (`event_id`),
  KEY `idx_event_package_package` (`package_id`),
  CONSTRAINT `fk_event_package_event` FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_event_package_package` FOREIGN KEY (`package_id`) REFERENCES `package` (`package_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `package` (
  `package_id` int NOT NULL AUTO_INCREMENT,
  `vendor_id` int NOT NULL,
  `category_id` int DEFAULT NULL,
  `title` varchar(160) NOT NULL,
  `description` text,
  `price` decimal(12,2) NOT NULL,
  `duration` varchar(100) DEFAULT NULL,
  `availability` varchar(40) NOT NULL DEFAULT 'AVAILABLE',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`package_id`),
  KEY `idx_package_vendor` (`vendor_id`),
  KEY `idx_package_category` (`category_id`),
  CONSTRAINT `fk_package_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE SET NULL,
  CONSTRAINT `fk_package_vendor` FOREIGN KEY (`vendor_id`) REFERENCES `vendor` (`vendor_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `payment` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `booking_id` int NOT NULL,
  `total_amount` decimal(12,2) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `due_amount` decimal(12,2) NOT NULL DEFAULT '0.00',
  `payment_type` varchar(40) NOT NULL,
  `status` varchar(40) NOT NULL DEFAULT 'PENDING',
  `payment_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`payment_id`),
  KEY `idx_payment_booking` (`booking_id`),
  CONSTRAINT `fk_payment_booking` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`booking_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `review` (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `event_id` int NOT NULL,
  `customer_id` int DEFAULT NULL,
  `package_id` int DEFAULT NULL,
  `package_rating` int DEFAULT NULL,
  `package_comment` text,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `reviewed_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`review_id`),
  KEY `idx_review_event` (`event_id`),
  KEY `idx_review_customer` (`customer_id`),
  KEY `idx_review_package` (`package_id`),
  CONSTRAINT `fk_review_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE SET NULL,
  CONSTRAINT `fk_review_event` FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_review_package` FOREIGN KEY (`package_id`) REFERENCES `package` (`package_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `vendor` (
  `vendor_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(120) NOT NULL,
  `username` varchar(80) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(40) NOT NULL,
  `availability` varchar(40) NOT NULL DEFAULT 'PENDING',
  `status` varchar(40) NOT NULL DEFAULT 'PENDING',
  `password` varchar(255) NOT NULL,
  `approved_by` int DEFAULT NULL,
  `approved_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`vendor_id`),
  UNIQUE KEY `uk_vendor_username` (`username`),
  UNIQUE KEY `uk_vendor_email` (`email`),
  UNIQUE KEY `uk_vendor_phone` (`phone`),
  KEY `fk_vendor_approved_by` (`approved_by`),
  CONSTRAINT `fk_vendor_approved_by` FOREIGN KEY (`approved_by`) REFERENCES `admin` (`admin_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO category (category_id, category_name) VALUES
  (1, 'Venue & Space Management'),
  (2, 'Catering & Beverage Services'),
  (3, 'Decoration & Event Styling'),
  (4, 'Photography & Cinematography'),
  (5, 'Entertainment & Music'),
  (6, 'Bridal Wear & Groom Attire'),
  (7, 'Beauty & Hair Services'),
  (8, 'Stationery & Invitations'),
  (9, 'Wedding Planning & Coordination'),
  (10, 'Transport & Wedding Cars');

SET FOREIGN_KEY_CHECKS=1;
