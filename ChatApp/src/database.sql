CREATE DATABASE IF NOT EXISTS chatapp;
USE chatapp;

CREATE TABLE IF NOT EXISTS messages (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    message_content TEXT NOT NULL,
    is_file INTEGER DEFAULT 0, -- 0 for text, 1 for file/path
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
);