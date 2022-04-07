CREATE KEYSPACE IF NOT EXISTS hotelapp WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE hotelapp.hotels(
  id uuid PRIMARY KEY,
  location text,
  name text,
  email text,
  phone text,
);

CREATE TABLE hotelapp.rooms(
  id int PRIMARY KEY,
  hotel_id int,
  room_no int,
  room_area int,
  availability Boolean,
  booking_price double,
  status text,
  capacity int
);

CREATE TABLE hotelapp.room_type(
  id uuid PRIMARY KEY,
  wifi Boolean,
  AC Boolean,
  heater Boolean,
  phone Boolean,
  minibar Boolean,
  tv Boolean,
  hotel_services Boolean,
  Bath Boolean,
  more_facilities list<text>
);

CREATE TABLE hotelapp.reviews(
  id uuid PRIMARY KEY,
  user_id uuid,
  hotel_id uuid,
  rating int,
  description text
);

CREATE TABLE hotelapp.bookings(
   id uuid PRIMARY KEY,
   room_id int,
   durationDays int,
   checkin date,
   checkout date,
   booking_status text
);

CREATE TABLE hotelapp.cities(
   id int PRIMARY KEY,
   name text,
   lat double,
   lng double,
   region text,
   population int
);

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

INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (a1b73570-cacf-40b5-90ee-78765e5c69f2, 'Pohjois-Karjala, Joensuu', 'Hotelli Laaksola', 'laaksola@hotelli.fi', '+358 40 225 4474 ');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (1787bad8-6e98-41d3-92d0-e4c7214a211b, 'Uusimaa, Helsinki', 'Hotelli Muonala', 'muonala@hotelli.fi', '+358 40 638 7274');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (01e9b44c-3dc4-4314-8cf0-6f13785b94d6, 'Uusimaa, Helsinki', 'Hotelli Testila', 'testila@hotelli.fi', '+358 40 869 9894');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (3bcda5ae-fc0e-47da-be10-1c18fc89cc1e, 'Uusimaa, Helsinki', 'Hotelli Toimela', 'toimela@hotelli.fi', '+358 40 796 2536');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (37821d5f-b6fc-42ec-bbc3-7b57d46b51e4, 'Uusimaa, Espoo', 'Hotelli Vanninen', 'vanninen@hotelli.fi', '+358 40 828 8388');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (1d94dcfc-0ce6-426b-b1de-b6fc42e380b6, 'Kymenlaakso, Kotka', 'Hotelli Kotka', 'kotka@hotelli.fi', '+358 40 166 4782');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (0ac5e0e8-dcf3-4cc1-9866-260cb1e3110b, 'Satakunta, Pori', 'Hotelli Lehti', 'lehti@hotelli.fi', '+358 40 028 3337');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (0590801a-752e-487e-bc26-33e5a42a7381, 'Pirkanmaa, Tampere', 'Hotelli Harjula', 'harjula@hotelli.fi', '+358 40 344 7136');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (f162436d-66ac-48bf-8448-991d33e12b5e, 'Uusimaa, Vantaa', 'Hotelli Koskela', 'koskela@hotelli.fi', '+358 40 681 2052');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (0064ca4e-6b88-4d0b-8b9b-6aebfcb17c4b, 'Pohjois-Pohjanmaa, Oulu', 'Hotelli Malinen', 'malinen@hotelli.fi', '+358 40 158 2942');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (7b3971bc-404f-44fe-ac60-e5e85f3a84cf, 'Varsinais-Suomi, Turku', 'Hotelli Oiva', 'oiva@hotelli.fi', '+358 40 052 5020');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (6cbfcf8e-079d-43b5-810b-4f2306f72ab0, 'Keski-Suomi, Jyväskylä', 'Hotelli Vuorela', 'vuorela@hotelli.fi', '+358 40 008 9253');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (4f554b46-486a-43c9-8d58-7b035a9e0133, 'Pohjois-Savo, Kuopio', 'Hotelli Elo', 'elo@hotelli.fi', '+358 40 868 3363');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (2d0262c4-c148-441f-9629-cc429c89718c, 'Etelä-Savo, Savonlinna', 'Hotelli Korpela', 'korpela@hotelli.fi', '+358 40 120 3923');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (8c33d8fb-61e3-4d19-ab60-89988890019b, 'Etelä-Pohjanmaa, Seinäjoki', 'Hotelli Hattula', 'hattula@hotelli.fi', '+358 40 121 3923');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (cbcaaf24-db75-4bc8-823c-3e1107120c1a, 'Pohjanmaa, Vaasa', 'Hotelli Kulma', 'kulma@hotelli.fi', '+358 50 121 3923');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (a84bdf6e-ba11-41a9-ab7d-7fd036db430c, 'Lappi, Saariselkä', 'Hotelli Keto', 'keto@hotelli.fi', '+358 90 123 4949');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (a3e9befa-2586-4432-bfdb-b5e84cde68e7, 'Lappi, Rovaniemi', 'Hotelli Sibelius', 'sibelius@hotelli.fi', '+358 40 627 6373');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (c5d2c7a2-0692-4c18-a210-4b6df5cb7a9d, 'Rauma, Satakunta', 'Hotelli Toot', 'toot@hotelli.fi', '+358 40 843 5676');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (b4e2ae72-2a1d-48e2-be3e-66776c89f3ad, 'Pori, Satakunta', 'Hotelli Tylli', 'tylli@hotelli.fi', '+358 40 182 9961');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (fb105fdc-7279-47f1-ac5a-7ffd42d9a695, 'Varsinais-Suomi, Naantali', 'Hotelli Sirri', 'sirri@hotelli.fi', '+358 40 976 2994');
INSERT INTO hotelapp.hotels(id, location, name, email, phone) VALUES (14f6b967-a156-449d-afe8-d438f0000bc4, 'Åland, Mariehamn', 'Hotelli Taigakuovi', 'taigakuovi@hotelli.fi', '+358 40 020 4681');
