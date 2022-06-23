-- hotelapp database schema

CREATE KEYSPACE hotelapp WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

CREATE TABLE hotelapp.bookings (
    id uuid PRIMARY KEY,
    customer_id uuid,
    hotel_id int,
    room_id int,
    booking_status text,
    end_date date,
    start_date date
);

CREATE TABLE hotelapp.cities (
    id int PRIMARY KEY,
    lat double,
    lng double,
    name text,
    population bigint,
    region text
);

CREATE TABLE hotelapp.hotels (
    id int PRIMARY KEY,
    email text,
    img text,
    location text,
    name text,
    phone text
);

CREATE TABLE hotelapp.reviews (
    id uuid PRIMARY KEY,
    customer_id uuid,
    hotel_id int,
    rating int,
    description text
);

CREATE TABLE hotelapp.rooms (
    id int PRIMARY KEY,
    hotel_id int,
    availability boolean,
    booking_price float,
    capacity int,
    room_area int,
    room_status text
);
