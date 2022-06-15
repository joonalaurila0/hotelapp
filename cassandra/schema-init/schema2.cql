-- hotelapp2 database schema

CREATE KEYSPACE hotelapp2 WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

CREATE TABLE hotelapp2.customers (
    id uuid PRIMARY KEY,
    email text,
    password text,
    role text,
    userstatus text
);

CREATE TABLE hotelapp2.invoices (
    id uuid PRIMARY KEY,
    customer_id uuid,
    booking_id uuid,
    total float,
    issued timestamp,
    paid boolean,
    payment_date date,
    cancelled boolean
);
