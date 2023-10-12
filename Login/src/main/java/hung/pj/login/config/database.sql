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
    avatar_path VARCHAR(255),
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
    category VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users(user_id)
);

--  Tạo bảng post_image chứa ảnh
CREATE TABLE IF NOT EXISTS post_images (
    image_id INT PRIMARY KEY AUTO_INCREMENT,
    post_id INT NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES post(post_id)
);

-- Tạo bảng categories
CREATE TABLE IF NOT EXISTS categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    creator_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users(user_id)
);


CREATE TABLE IF NOT EXISTS saved_posts (
    saved_post_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    post_id INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE
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
-- Bảng Conversations (Cuộc trò chuyện)
CREATE TABLE IF NOT EXISTS conversations (
    conversation_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),  -- Tên cuộc trò chuyện (nếu có)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Participants (Người tham gia cuộc trò chuyện)
CREATE TABLE IF NOT EXISTS participants (
    participant_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    conversation_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (conversation_id) REFERENCES conversations(conversation_id)
);

-- Bảng Messages (Tin nhắn)
CREATE TABLE IF NOT EXISTS messages (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    conversation_id INT NOT NULL,
    sender_id INT NOT NULL,
    message_text TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversations(conversation_id),
    FOREIGN KEY (sender_id) REFERENCES users(user_id)
);



