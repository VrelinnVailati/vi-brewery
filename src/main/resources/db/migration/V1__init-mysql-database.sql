CREATE TABLE beer
(
    id               CHAR(36)       NOT NULL,
    beer_name        VARCHAR(50)    NULL,
    beer_style       TINYINT(3)     NOT NULL,
    created_date     datetime       NULL,
    price            DECIMAL(38, 2) NOT NULL,
    quantity_on_hand INT            NULL,
    upc              VARCHAR(255)   NULL,
    update_date      datetime       NULL,
    version          INT            NULL,
    CONSTRAINT PK_BEER PRIMARY KEY (id)
);

CREATE TABLE customer
(
    id                 CHAR(36)     NOT NULL,
    created_date       datetime     NULL,
    customer_name      VARCHAR(255) NULL,
    last_modified_date datetime     NULL,
    version            INT          NULL,
    CONSTRAINT PK_CUSTOMER PRIMARY KEY (id)
);