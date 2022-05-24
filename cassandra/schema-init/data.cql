-- hotelapp Database insertation data for mocking

-- cities
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (1, 'Helsinki', 60.1756, 24.9342, 'Uusimaa', 642045);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (2, 'Espoo', 60.21,24.66, 'Uusimaa', 269802);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (3, 'Tampere', 61.4981,23.7608, 'Pirkanmaa', 225118);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (4, 'Vantaa', 60.3,25.0333, 'Uusimaa', 214605);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (5, 'Oulu', 65.0142,25.4719, 'Pohjois-Pohjanmaa', 200526);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (6, 'Turku', 60.4517,22.27, 'Varsinais-Suomi', 187604);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (7, 'Jyväskylä', 62.2333, 25.7333, 'Keski-Suomi', 137368);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (8, 'Kuopio', 62.8925, 27.6783, 'Pohjois-Savo', 120246);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (9, 'Savonlinna', 61.8667, 28.8831, 'Etelä-Savo', 35523);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (10, 'Seinäjoki', 62.7903, 22.8403, 'Etelä-Pohjanmaa', 61530);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (11, 'Vaasa', 63.1, 21.6167, 'Pohjanmaa', 67619);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (12, 'Saariselkä', 68.4197, 27.4100, 'Lappi', 177161);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (13, 'Rovaniemi', 66.5028, 25.7285, 'Lappi', 61763);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (14, 'Rauma', 61.1167, 21.5, 'Satakunta', 39809);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (15, 'Pori', 61.4847, 21.7972, 'Satakunta', 85363);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (16, 'Naantali', 60.4681, 22.0264, 'Varsinais-Suomi', 18916);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (17, 'Mariehamn', 60.0986, 19.9444, 'Åland', 11461);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (18, 'Kotka', 60.4667, 26.9458, 'Kymenlaakso', 54319);
INSERT INTO hotelapp.cities(id, name, lat, lng, region, population) VALUES (19, 'Joensuu', 62.6, 29.7639, 'Pohjois-Karjala', 75514);

-- hotels
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (1, 'placeholder.jpg', 'Pohjois-Karjala, Joensuu', 'Hotelli Laaksola', 'laaksola@hotelli.fi', '+358 40 225 4474 ');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (2, 'placeholder.jpg', 'Uusimaa, Helsinki', 'Hotelli Muonala', 'muonala@hotelli.fi', '+358 40 638 7274');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (3, 'placeholder.jpg', 'Uusimaa, Helsinki', 'Hotelli Testila', 'testila@hotelli.fi', '+358 40 869 9894');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (4, 'placeholder.jpg', 'Uusimaa, Helsinki', 'Hotelli Toimela', 'toimela@hotelli.fi', '+358 40 796 2536');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (5, 'placeholder.jpg', 'Uusimaa, Espoo', 'Hotelli Vanninen', 'vanninen@hotelli.fi', '+358 40 828 8388');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (6, 'placeholder.jpg', 'Kymenlaakso, Kotka', 'Hotelli Kotka', 'kotka@hotelli.fi', '+358 40 166 4782');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (7, 'placeholder.jpg', 'Satakunta, Pori', 'Hotelli Lehti', 'lehti@hotelli.fi', '+358 40 028 3337');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (8, 'placeholder.jpg', 'Pirkanmaa, Tampere', 'Hotelli Harjula', 'harjula@hotelli.fi', '+358 40 344 7136');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (9, 'placeholder.jpg', 'Uusimaa, Vantaa', 'Hotelli Koskela', 'koskela@hotelli.fi', '+358 40 681 2052');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (10, 'placeholder.jpg', 'Pohjois-Pohjanmaa, Oulu', 'Hotelli Malinen', 'malinen@hotelli.fi', '+358 40 158 2942');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (11, 'placeholder.jpg', 'Varsinais-Suomi, Turku', 'Hotelli Oiva', 'oiva@hotelli.fi', '+358 40 052 5020');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (12, 'placeholder.jpg', 'Keski-Suomi, Jyväskylä', 'Hotelli Vuorela', 'vuorela@hotelli.fi', '+358 40 008 9253');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (13, 'placeholder.jpg', 'Pohjois-Savo, Kuopio', 'Hotelli Elo', 'elo@hotelli.fi', '+358 40 868 3363');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (14, 'placeholder.jpg', 'Etelä-Savo, Savonlinna', 'Hotelli Korpela', 'korpela@hotelli.fi', '+358 40 120 3923');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (15, 'placeholder.jpg', 'Etelä-Pohjanmaa, Seinäjoki', 'Hotelli Hattula', 'hattula@hotelli.fi', '+358 40 121 3923');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (16, 'placeholder.jpg', 'Pohjanmaa, Vaasa', 'Hotelli Kulma', 'kulma@hotelli.fi', '+358 50 121 3923');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (17, 'placeholder.jpg', 'Lappi, Saariselkä', 'Hotelli Keto', 'keto@hotelli.fi', '+358 90 123 4949');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (18, 'placeholder.jpg', 'Lappi, Rovaniemi', 'Hotelli Sibelius', 'sibelius@hotelli.fi', '+358 40 627 6373');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (19, 'placeholder.jpg', 'Rauma, Satakunta', 'Hotelli Toot', 'toot@hotelli.fi', '+358 40 843 5676');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (20, 'placeholder.jpg', 'Pori, Satakunta', 'Hotelli Tylli', 'tylli@hotelli.fi', '+358 40 182 9961');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (21, 'placeholder.jpg', 'Varsinais-Suomi, Naantali', 'Hotelli Sirri', 'sirri@hotelli.fi', '+358 40 976 2994');
INSERT INTO hotelapp.hotels(id, img, location, name, email, phone) VALUES (22, 'placeholder.jpg', 'Åland, Mariehamn', 'Hotelli Taigakuovi', 'taigakuovi@hotelli.fi', '+358 40 020 4681');

-- rooms, this gives each hotel a single room except hotelli kotka which gets three.
-- Example: insert into rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (1, 1, true, 55.0, 2, 25, 'Available')

insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (1, 1, true, 69.27, 3, 20, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (2, 2, true, 56.4, 2, 20, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (3, 3, false, 68.7, 4, 45, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (4, 4, true, 49.75, 3, 25, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (5, 5, false, 66.63, 4, 20, 'Available');

-- Hotelli Kotka
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (6, 6, false, 54.49, 4, 20, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (31, 6, false, 54.49, 4, 20, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (32, 6, false, 54.49, 4, 20, 'Available');

insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (7, 7, false, 46.75, 2, 30, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (8, 8, true, 38.33, 2, 45, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (9, 9, true, 47.29, 4, 20, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (10, 10, true, 35.53, 4, 20, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (11, 11, true, 78.89, 3, 25, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (12, 12, true, 94.76, 4, 25, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (13, 13, false, 26.43, 3, 30, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (14, 14, true, 50.68, 3, 45, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (15, 15, true, 37.22, 3, 25, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (16, 16, true, 41.6, 3, 45, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (17, 17, true, 74.88, 3, 20, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (18, 18, true, 95.47, 2, 20, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (19, 19, true, 88.46, 4, 45, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (20, 20, true, 91.14, 3, 25, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (21, 21, false, 26.7, 2, 30, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (22, 22, false, 64.93, 2, 45, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (23, 23, false, 61.32, 2, 20, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (24, 24, false, 30.22, 3, 30, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (25, 25, false, 38.78, 2, 25, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (26, 26, true, 94.91, 4, 20, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (27, 27, true, 85.85, 2, 45, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (28, 28, false, 70.39, 4, 30, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (29, 29, false, 30.67, 4, 30, 'Available');
insert into hotelapp.rooms (id, hotel_id, availability, booking_price, capacity, room_area, room_status) values (30, 30, false, 27.88, 4, 20, 'Available');

-- bookings
-- Example:  insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), CUSTOMER_ID, 2, 14, 'Confirmed', '2022-05-05');

-- Creates 30 bookings for three different rooms in Hotelli Kotka
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 673d4aff-9c1d-4b69-bdc0-c2ab0aed12c9, 6, 31, 'Confirmed', '2021-05-22', '2022-03-29');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 539c4da8-219b-4c89-afdb-99afbcbb3753, 6, 6, 'Confirmed', '2022-05-04', '2021-09-03');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 673d4aff-9c1d-4b69-bdc0-c2ab0aed12c9, 6, 32, 'Confirmed', '2022-05-09', '2022-01-30');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 91f7fd34-ce8c-4abc-955b-856e8c28c797, 6, 32, 'Confirmed', '2022-03-02', '2022-03-17');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), a519d565-e352-45f2-9b38-8357b84d4d4c, 6, 6, 'Confirmed', '2021-12-24', '2021-11-28');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 539c4da8-219b-4c89-afdb-99afbcbb3753, 6, 6, 'Confirmed', '2021-11-21', '2022-02-09');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 673d4aff-9c1d-4b69-bdc0-c2ab0aed12c9, 6, 32, 'Confirmed', '2021-08-02', '2022-05-14');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 2c452f9c-6a2a-49e3-905c-3ece8b712dfc, 6, 32, 'Confirmed', '2021-12-03', '2022-02-12');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 1372c830-57e4-4aa7-9909-75d77a835724, 6, 32, 'Confirmed', '2022-04-27', '2022-02-16');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 673d4aff-9c1d-4b69-bdc0-c2ab0aed12c9, 6, 32, 'Confirmed', '2022-04-21', '2021-11-21');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 673d4aff-9c1d-4b69-bdc0-c2ab0aed12c9, 6, 32, 'Confirmed', '2022-05-16', '2021-11-27');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 1372c830-57e4-4aa7-9909-75d77a835724, 6, 32, 'Confirmed', '2022-01-29', '2021-09-18');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), f494d63f-e862-4e30-9889-6a7ef2b61d45, 6, 31, 'Confirmed', '2021-08-21', '2021-06-04');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 539c4da8-219b-4c89-afdb-99afbcbb3753, 6, 32, 'Confirmed', '2022-03-14', '2021-07-20');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 91f7fd34-ce8c-4abc-955b-856e8c28c797, 6, 31, 'Confirmed', '2022-01-12', '2021-08-31');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), a519d565-e352-45f2-9b38-8357b84d4d4c, 6, 32, 'Confirmed', '2021-09-30', '2021-08-03');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 539c4da8-219b-4c89-afdb-99afbcbb3753, 6, 32, 'Confirmed', '2021-07-03', '2022-04-21');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 1372c830-57e4-4aa7-9909-75d77a835724, 6, 31, 'Confirmed', '2021-05-25', '2021-07-09');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), f494d63f-e862-4e30-9889-6a7ef2b61d45, 6, 6, 'Confirmed', '2021-07-23', '2022-03-08');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), be279449-eea0-427c-9707-77a7346ae53d, 6, 31, 'Confirmed', '2021-12-30', '2022-01-13');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 91f7fd34-ce8c-4abc-955b-856e8c28c797, 6, 32, 'Confirmed', '2021-10-22', '2021-06-15');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 91f7fd34-ce8c-4abc-955b-856e8c28c797, 6, 32, 'Confirmed', '2021-07-07', '2021-09-22');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 2c452f9c-6a2a-49e3-905c-3ece8b712dfc, 6, 31, 'Confirmed', '2022-02-14', '2021-12-26');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), be279449-eea0-427c-9707-77a7346ae53d, 6, 31, 'Confirmed', '2022-02-24', '2021-10-22');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 9966903b-088b-44b8-ad0b-c65d7d53d30a, 6, 6, 'Confirmed', '2021-07-11', '2021-11-03');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 539c4da8-219b-4c89-afdb-99afbcbb3753, 6, 6, 'Confirmed', '2021-12-16', '2021-12-20');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), a519d565-e352-45f2-9b38-8357b84d4d4c, 6, 31, 'Confirmed', '2022-04-19', '2021-08-29');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 673d4aff-9c1d-4b69-bdc0-c2ab0aed12c9, 6, 6, 'Confirmed', '2021-09-13', '2021-11-02');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 2c452f9c-6a2a-49e3-905c-3ece8b712dfc, 6, 6, 'Confirmed', '2021-09-30', '2022-01-11');
insert into hotelapp.bookings (id, customer_id, hotel_id, room_id, booking_status, start_date, end_date) values (uuid(), 514246ba-efca-4d78-905e-2261be2387b5, 6, 31, 'Confirmed', '2022-03-23', '2021-08-30');
