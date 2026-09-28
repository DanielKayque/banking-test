CREATE TABLE users(
    id UUID PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    document VARCHAR(11) NOT NULL
    email VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    createdAt TIMESTAMPZ DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMPZ DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_email UNIQUE (email),
    CONSTRAINT uk_document UNIQUE (document)
);