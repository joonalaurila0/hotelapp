-- hotelapp2 database schema

CREATE KEYSPACE hotelapp2 WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

CREATE TABLE hotelapp2.bookings (
    id uuid PRIMARY KEY,
    customer_id uuid,
    hotel_id int,
    room_id int,
    booking_status text,
    end_date date,
    start_date date
);

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

CREATE TABLE hotelapp2.rooms (
    id int PRIMARY KEY,
    hotel_id int,
    availability boolean,
    booking_price double,
    capacity int,
    room_area int,
    room_status text
);
