DROP DATABASE IF EXISTS miao_bau;
CREATE DATABASE IF NOT EXISTS miao_bau
    CHARACTER SET utf8mb4 -- codifica che gestisce correttamente accenti e caratteri speciali
    COLLATE utf8mb4_unicode_ci;
USE miao_bau;

-- CATEGORY (lookup)
CREATE TABLE category (
    category_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
);

-- SPECIES (lookup)
CREATE TABLE species (
    species_id   INT AUTO_INCREMENT PRIMARY KEY,
    species_name VARCHAR(30) NOT NULL UNIQUE
);

-- CUSTOMER
CREATE TABLE customer (
    customer_id   INT AUTO_INCREMENT PRIMARY KEY,
    first_name    VARCHAR(50)  NOT NULL,
    last_name     VARCHAR(50)  NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    phone         VARCHAR(20)  NULL,
    birth_date    DATE         NULL,
    password_hash VARCHAR(255) NOT NULL
);

-- ADMIN
CREATE TABLE admin (
    admin_id      INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(20)  NOT NULL UNIQUE,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

-- PRODUCT
CREATE TABLE product (
    product_id          INT AUTO_INCREMENT PRIMARY KEY,
    category_id         INT           NOT NULL,
    species_id          INT           NOT NULL,
    name                VARCHAR(100)  NOT NULL,
    description         VARCHAR(500)  NULL,
    price               DECIMAL(10,2) NOT NULL,
    vat                 DECIMAL(5,2)  NOT NULL,
    on_sale             BOOLEAN       NOT NULL DEFAULT FALSE,
    discount_percentage DECIMAL(5,2)  NULL,
    image               VARCHAR(255)  NULL,
    brand               VARCHAR(50)   NULL,
    is_deleted          BOOLEAN       NOT NULL DEFAULT FALSE,
    -- attributi della gerarchia collassata (nullable)
    weight              DECIMAL(6,2)  NULL,
    ingredients         VARCHAR(500)  NULL,
    size                VARCHAR(20)   NULL,
    color               VARCHAR(30)   NULL,
    material            VARCHAR(50)   NULL,
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES category(category_id),
    CONSTRAINT fk_product_species
        FOREIGN KEY (species_id) REFERENCES species(species_id),
    -- vincolo coerenza sconto
    CONSTRAINT chk_discount_consistency CHECK (
        (on_sale = TRUE  AND discount_percentage IS NOT NULL)
     OR (on_sale = FALSE AND discount_percentage IS NULL)
    ),
    -- vincolo range percentuale
    CONSTRAINT chk_discount_range CHECK (
        discount_percentage IS NULL
     OR (discount_percentage >= 0 AND discount_percentage <= 100)
    ),
    -- vincolo prezzo positivo
    CONSTRAINT chk_price_positive CHECK (price >= 0)
);

-- ADDRESS (un customer ha molti indirizzi)
CREATE TABLE address (
    address_id  INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT         NOT NULL,
    street      VARCHAR(50) NOT NULL,
    civic_number VARCHAR(10) NOT NULL,
    postal_code VARCHAR(5)  NOT NULL,
    city        VARCHAR(50) NOT NULL,
    country     VARCHAR(50) NOT NULL,
    CONSTRAINT fk_address_customer
        FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
        ON DELETE CASCADE
);

-- ORDERS (testata ordine — plurale perché ORDER è parola riservata)
CREATE TABLE orders (
    order_id    INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT           NOT NULL,
    order_date  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    CONSTRAINT chk_total_positive CHECK (total_price >= 0)
);

-- ORDER_ITEM (ex Composizione: i prodotti dentro ogni ordine)
CREATE TABLE order_item (
    order_id      INT           NOT NULL,
    product_id    INT           NOT NULL,
    quantity      INT           NOT NULL,
    unit_price    DECIMAL(10,2) NOT NULL,
    vat_frozen    DECIMAL(5,2)  NOT NULL, -- iva
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_item_order
        FOREIGN KEY (order_id) REFERENCES orders(order_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_item_product
        FOREIGN KEY (product_id) REFERENCES product(product_id),
    CONSTRAINT chk_quantity_positive CHECK (quantity > 0)
);
