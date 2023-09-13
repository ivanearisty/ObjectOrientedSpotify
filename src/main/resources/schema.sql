-- Establish the connection and set the time zone
SET TIME_ZONE = '-04:00';

-- Create the schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS ObjectOrientedSpotify;

-- Select the database
USE ObjectOrientedSpotify;

-- Drop existing tables (in the correct order due to foreign keys)

DROP TABLE IF EXISTS Playlists_Tracks;
DROP TABLE IF EXISTS User_Events;
DROP TABLE IF EXISTS Genres_Playlists;
DROP TABLE IF EXISTS Events;
DROP TABLE IF EXISTS Reset_Password_Verifications;
DROP TABLE IF EXISTS Account_Verifications;
DROP TABLE IF EXISTS Multi_Factor_Authentications;
DROP TABLE IF EXISTS Tracks;
DROP TABLE IF EXISTS Genres;
DROP TABLE IF EXISTS Playlists;
DROP TABLE IF EXISTS Users;

-- Create the Users table
CREATE TABLE Users
(
    user_id        VARCHAR(50)  NOT NULL PRIMARY KEY,
    first_name     VARCHAR(50)  NOT NULL,
    last_name      VARCHAR(50)  NOT NULL,
    email          VARCHAR(100) NOT NULL,
    password       VARCHAR(255) NOT NULL, -- Increased length for password storage
    phone_number   VARCHAR(20) DEFAULT NULL,
    is_non_locked  BOOLEAN     DEFAULT TRUE,
    is_enabled     BOOLEAN     DEFAULT FALSE,
    is_using_mfa   BOOLEAN     DEFAULT FALSE,
    profile_image  VARCHAR(255),
    created_date   DATETIME    DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT UQ_Users_email UNIQUE (email)
);

-- Create the Genres table
CREATE TABLE Genres
(
    genre_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    genre_name   VARCHAR(50) NOT NULL -- Increased length for genre names
);

-- Create the Playlists table
CREATE TABLE Playlists
(
    playlist_id VARCHAR(50) NOT NULL PRIMARY KEY, -- Increased length for playlist IDs
    owner_id    VARCHAR(50) NOT NULL,
    user_id     VARCHAR(50) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE NO ACTION ON UPDATE CASCADE
);

-- Create the Genres_Playlists table
CREATE TABLE Genres_Playlists
(
    genre_id       BIGINT UNSIGNED NOT NULL,
    playlist_id    VARCHAR(50) NOT NULL,

    PRIMARY KEY (genre_id, playlist_id),

    INDEX fk_genres_playlists_1_idx (genre_id ASC) VISIBLE,
    FOREIGN KEY (genre_id)
        REFERENCES Genres (genre_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (playlist_id)
        REFERENCES Playlists (playlist_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Create the Tracks table
CREATE TABLE Tracks
(
    track_id              VARCHAR(50)        NOT NULL PRIMARY KEY, -- Increased length for track IDs
    artist_names          VARCHAR(255)       NOT NULL,
    track_name            VARCHAR(255)       NOT NULL,
    album_name            VARCHAR(255)       NOT NULL,
    external_spotify_url  VARCHAR(255)       NOT NULL,
    length_seconds        MEDIUMINT UNSIGNED NOT NULL COMMENT 'The longest song in Spotify is about 2 days long',
    track_popularity      TINYINT UNSIGNED   NOT NULL,
    tempo                 TINYINT ZEROFILL   NOT NULL,
    camelot_key           VARCHAR(3)         NOT NULL COMMENT 'Key and mode mapping',
    energy                FLOAT              NOT NULL COMMENT 'Energy measure',
    is_explicit           BOOLEAN            NOT NULL COMMENT '1 if explicit, 0 otherwise'
);

-- Create the Playlists_Tracks table
CREATE TABLE Playlists_Tracks
(
    playlist_id VARCHAR(50) NOT NULL,
    track_id    VARCHAR(50) NOT NULL,

    PRIMARY KEY (playlist_id, track_id),

    INDEX fk_playlists_tracks_tracks1_idx (track_id ASC) VISIBLE,
    FOREIGN KEY (playlist_id)
        REFERENCES Playlists (playlist_id)
        ON DELETE NO ACTION
        ON UPDATE CASCADE,
    FOREIGN KEY (track_id)
        REFERENCES Tracks (track_id)
        ON DELETE NO ACTION
        ON UPDATE CASCADE
);

-- Create the Events table
CREATE TABLE Events
(
    event_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    event_type   VARCHAR(30)     NOT NULL CHECK (event_type IN ('SPOTIFY_UPDATE',
                                                                'PASSWORD_UPDATE',
                                                                'MFA_UPDATE',
                                                                'LOGOUT',
                                                                'LOGIN_ATTEMPT',
                                                                'LOGIN_ATTEMPT_SUCCESS',
                                                                'LOGIN_ATTEMPT_FAILURE')),
    description  VARCHAR(255)    NOT NULL,

    CONSTRAINT UQ_Events_event_type UNIQUE (event_type)
);

-- Create the User_Events table
CREATE TABLE User_Events
(
    user_event_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id       VARCHAR(50)     NOT NULL,
    event_id      BIGINT UNSIGNED NOT NULL,
    device        VARCHAR(50)  DEFAULT NULL,
    ip_address    VARCHAR(100) DEFAULT NULL,
    created_date  DATETIME     DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (event_id) REFERENCES Events (event_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create the Account_Verifications table
CREATE TABLE Account_Verifications
(
    account_verification_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id                 VARCHAR(50)     NOT NULL,
    url                     VARCHAR(255) DEFAULT NULL,

    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_Account_Verifications_user_id UNIQUE (user_id),
    CONSTRAINT UQ_Account_Verifications_url UNIQUE (url)
);

-- Create the Reset_Password_Verifications table
CREATE TABLE Reset_Password_Verifications
(
    reset_verification_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id               VARCHAR(50)     NOT NULL,
    url                   VARCHAR(255) DEFAULT NULL,
    expiry_date           DATETIME        NOT NULL,

    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_Reset_Password_Verifications_user_id UNIQUE (user_id),
    CONSTRAINT UQ_Reset_Password_Verifications_url UNIQUE (url)
);

-- Create the Multi_Factor_Authentications table
CREATE TABLE Multi_Factor_Authentications
(
    mfa_id       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id      VARCHAR(50)     NOT NULL,
    mfa_code     VARCHAR(10) DEFAULT NULL,
    expiry_date  DATETIME        NOT NULL,

    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_mfa_user_id UNIQUE (user_id),
    CONSTRAINT UQ_mfa_code UNIQUE (mfa_code)
);

