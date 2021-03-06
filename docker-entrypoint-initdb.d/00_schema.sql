CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       login TEXT NOT NULL UNIQUE,
                       password TEXT NOT NULL,
                       name TEXT NOT NULL,
                       secret TEXT NOT NULL,
                       roles TEXT[] NOT NULL DEFAULT '{}',
                       removed BOOLEAN NOT NULL DEFAULT false,
                       created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);