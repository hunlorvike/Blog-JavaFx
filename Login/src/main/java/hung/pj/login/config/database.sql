SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS management;

-- Sử dụng cơ sở dữ liệu
USE management;

-- Cơ sở dữ liệu: `management`

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `admin_logs`
CREATE TABLE IF NOT EXISTS `admin_logs` (
    `log_id` int(11) NOT NULL AUTO_INCREMENT,
    `admin_id` int(11) DEFAULT NULL,
    `action` varchar(255) NOT NULL,
    `details` text DEFAULT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`log_id`),
    KEY `admin_id` (`admin_id`),
    CONSTRAINT `admin_logs_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `categories`
CREATE TABLE IF NOT EXISTS `categories` (
    `category_id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `creator_id` int(11) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `conversations`
CREATE TABLE IF NOT EXISTS `conversations` (
    `conversation_id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`conversation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `messages`
CREATE TABLE IF NOT EXISTS `messages` (
    `message_id` int(11) NOT NULL AUTO_INCREMENT,
    `conversation_id` int(11) NOT NULL,
    `sender_id` int(11) NOT NULL,
    `message_text` text NOT NULL,
    `sent_at` timestamp NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`message_id`),
    KEY `conversation_id` (`conversation_id`),
    KEY `sender_id` (`sender_id`),
    CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`conversation_id`),
    CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `notifications`
CREATE TABLE IF NOT EXISTS `notifications` (
    `notification_id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) DEFAULT NULL,
    `content` text NOT NULL,
    `is_read` tinyint(1) DEFAULT 0,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`notification_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `participants`
CREATE TABLE IF NOT EXISTS `participants` (
    `participant_id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) NOT NULL,
    `conversation_id` int(11) NOT NULL,
    PRIMARY KEY (`participant_id`),
    KEY `user_id` (`user_id`),
    KEY `conversation_id` (`conversation_id`),
    CONSTRAINT `participants_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
    CONSTRAINT `participants_ibfk_2` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`conversation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `post`
CREATE TABLE IF NOT EXISTS `post` (
    `post_id` int(11) NOT NULL AUTO_INCREMENT,
    `title` varchar(255) NOT NULL,
    `content` text NOT NULL,
    `status` enum('Draft','Published','Scheduled') NOT NULL,
    `view_count` int(11) DEFAULT 0,
    `creator_id` int(11) DEFAULT NULL,
    `scheduled_datetime` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    `category` varchar(255) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`post_id`),
    KEY `creator_id` (`creator_id`),
    CONSTRAINT `post_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `post_images`
CREATE TABLE IF NOT EXISTS `post_images` (
    `image_id` int(11) NOT NULL AUTO_INCREMENT,
    `post_id` int(11) NOT NULL,
    `image_path` varchar(255) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`image_id`),
    KEY `post_id` (`post_id`),
    CONSTRAINT `post_images_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `saved_posts`
CREATE TABLE IF NOT EXISTS `saved_posts` (
    `saved_post_id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) DEFAULT NULL,
    `post_id` int(11) DEFAULT NULL,
    PRIMARY KEY (`saved_post_id`),
    KEY `user_id` (`user_id`),
    KEY `post_id` (`post_id`),
    CONSTRAINT `saved_posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
    CONSTRAINT `saved_posts_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `scheduled_posts`
CREATE TABLE IF NOT EXISTS `scheduled_posts` (
    `scheduled_post_id` int(11) NOT NULL AUTO_INCREMENT,
    `post_id` int(11) DEFAULT NULL,
    `scheduled_datetime` datetime NOT NULL,
    `status` enum('Scheduled','Published') NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`scheduled_post_id`),
    KEY `post_id` (`post_id`),
    CONSTRAINT `scheduled_posts_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `social_media`
CREATE TABLE IF NOT EXISTS `social_media` (
    `social_media_id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) DEFAULT NULL,
    `platform` enum('Facebook','Instagram','Twitter','Pinterest','Github','GitLab') NOT NULL,
    `profile_url` varchar(255) NOT NULL,
    PRIMARY KEY (`social_media_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `social_media_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `users`
CREATE TABLE IF NOT EXISTS `users` (
    `user_id` int(11) NOT NULL AUTO_INCREMENT,
    `fullname` varchar(255) NOT NULL,
    `email` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `role` enum('Super Admin','Admin','Moderator') NOT NULL,
    `locked_until` datetime DEFAULT NULL,
    `followers_count` int(11) NOT NULL DEFAULT 0,
    `avatar_path` varchar(255) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

-- Cấu trúc bảng cho bảng `user_followers`
CREATE TABLE IF NOT EXISTS `user_followers` (
    `follower_id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) NOT NULL,
    `follower_user_id` int(11) NOT NULL,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`follower_id`),
    KEY `user_id` (`user_id`),
    KEY `follower_user_id` (`follower_user_id`),
    CONSTRAINT `user_followers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
    CONSTRAINT `user_followers_ibfk_2` FOREIGN KEY (`follower_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

COMMIT;

