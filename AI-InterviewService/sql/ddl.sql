CREATE TABLE users (
    userId VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE coverletters (
    coverLetterId VARCHAR(255) PRIMARY KEY,
    userId VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    uploadDate Date,
    content VARCHAR(255) NOT NULL
);

CREATE TABLE questions (
    id Long AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255),
    ques VARCHAR(MAX)
);
