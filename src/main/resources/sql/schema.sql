CREATE TABLE IF NOT EXISTS CUSTOMERS (
    CUSTOMER_ID BIGINT NOT NULL,
    CUSTOMER_NICKNAME VARCHAR(255),
    CUSTOMER_EMAIL VARCHAR(255),
    CUSTOMER_ADDRESS VARCHAR(255),
    CUSTOMER_LATITUDE DOUBLE,
    CUSTOMER_LONGITUDE DOUBLE,
    CUSTOMER_PHONE VARCHAR(255),
    PRIMARY KEY (CUSTOMER_ID)
);

CREATE TABLE IF NOT EXISTS OWNERS (
    OWNER_ID BIGINT NOT NULL,
    OWNER_NAME VARCHAR(255),
    OWNER_EMAIL VARCHAR(255),
    OWNER_BUSINESS_NUMBER VARCHAR(255),
    PRIMARY KEY (OWNER_ID)
);

CREATE TABLE IF NOT EXISTS RIDERS (
    RIDER_ID BIGINT NOT NULL,
    RIDER_NICKNAME VARCHAR(255),
    RIDER_EMAIL VARCHAR(255),
    RIDER_PHONE VARCHAR(50),
    RIDER_ACTIVATE TINYINT(1),
    RIDER_TRANSPORTATION VARCHAR(255),
    RIDER_ACCOUNT VARCHAR(255),
    RIDER_IS_DELETED TINYINT(1),
    PRIMARY KEY (RIDER_ID)
);

CREATE TABLE IF NOT EXISTS RIDER_ACTIVITY_AREAS (
    RIDER_ACTIVITY_AREAS_ID BIGINT NOT NULL AUTO_INCREMENT,
    RIDER_ID BIGINT,
    RIDER_ACTIVITY_AREA VARCHAR(255),
    PRIMARY KEY (RIDER_ACTIVITY_AREAS_ID)
);
