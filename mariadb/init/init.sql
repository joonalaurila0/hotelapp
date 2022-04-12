CREATE DATABASE IF NOT EXISTS hotelapp;
USE hotelapp;

GRANT ALL PRIVILEGES on *.* to 'root'@'localhost' IDENTIFIED BY '32';
FLUSH PRIVILEGES;

CREATE TABLE customers(
  id BINARY(16) NOT NULL PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255),
  phone VARCHAR(255),
  address VARCHAR(255)
);

CREATE TABLE invoices(
  id BINARY(16) NOT NULL PRIMARY KEY,
  customer_id BINARY(16) references customers(id),
  invoice_amount INTEGER NOT NULL,
  issued NUMERIC(7,4) NOT NULL,
  paid BOOLEAN NOT NULL,
  canceled BOOLEAN NOT NULL
);

ALTER TABLE invoices ADD FOREIGN KEY (customer_id) references customers (id);
