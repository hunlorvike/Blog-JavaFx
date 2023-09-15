-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS management;

-- Sử dụng cơ sở dữ liệu
USE management;

-- Tạo bảng users
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Super Admin', 'Admin', 'Moderator') NOT NULL,
    locked_until DATETIME,
    followers_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_followers (
    follower_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    follower_user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (follower_user_id) REFERENCES users(user_id)
);


CREATE TABLE IF NOT EXISTS social_media (
    social_media_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    platform ENUM('Facebook', 'Instagram', 'Twitter', 'Pinterest', 'Github', 'GitLab') NOT NULL,
    profile_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Tạo bảng posts
CREATE TABLE IF NOT EXISTS post (
    post_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    status ENUM('Draft', 'Published', 'Scheduled') NOT NULL,
    view_count INT DEFAULT 0,
    creator_id INT,
    scheduled_datetime TIMESTAMP, -- Thêm trường scheduled_datetime kiểu TIMESTAMP
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users(user_id)
);

-- Tạo bảng tags
CREATE TABLE IF NOT EXISTS tags (
    tag_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng post_tag
CREATE TABLE IF NOT EXISTS post_tag (
    post_id INT,
    tag_id INT,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES post(post_id),
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id)
);

CREATE TABLE IF NOT EXISTS saved_posts (
    saved_post_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    post_id INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (post_id) REFERENCES post(post_id)
);

-- Tạo bảng notifications
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    content TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

