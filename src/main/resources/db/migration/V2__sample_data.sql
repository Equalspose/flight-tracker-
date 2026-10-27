INSERT INTO airport (iata_code, name, city, country, timezone) VALUES
    ('SVO', 'Шереметьево',          'Москва',           'Россия',   'Europe/Moscow'),
    ('DME', 'Домодедово',           'Москва',           'Россия',   'Europe/Moscow'),
    ('LED', 'Пулково',              'Санкт-Петербург',  'Россия',   'Europe/Moscow'),
    ('AER', 'Сочи',                 'Сочи',             'Россия',   'Europe/Moscow'),
    ('KZN', 'Казань',               'Казань',           'Россия',   'Europe/Moscow'),
    ('EVN', 'Звартноц',             'Ереван',           'Армения',  'Asia/Yerevan'),
    ('IST', 'Стамбул',              'Стамбул',          'Турция',   'Europe/Istanbul'),
    ('DXB', 'Дубай',                'Дубай',            'ОАЭ',      'Asia/Dubai');

INSERT INTO airline (iata_code, name, country) VALUES
    ('SU', 'Аэрофлот',          'Россия'),
    ('S7', 'S7 Airlines',       'Россия'),
    ('DP', 'Победа',            'Россия'),
    ('TK', 'Turkish Airlines',  'Турция'),
    ('EK', 'Emirates',          'ОАЭ');

INSERT INTO flight (flight_number, airline_id, departure_airport_id, arrival_airport_id,
                    scheduled_departure, scheduled_arrival, actual_departure, actual_arrival,
                    status, gate, aircraft_type)
VALUES
    ('SU6',    (SELECT id FROM airline WHERE iata_code='SU'), (SELECT id FROM airport WHERE iata_code='SVO'), (SELECT id FROM airport WHERE iata_code='LED'),
     '2026-10-14 08:00+03', '2026-10-14 09:30+03', NULL, NULL, 'SCHEDULED', 'B12', 'Airbus A320'),
    ('SU1860', (SELECT id FROM airline WHERE iata_code='SU'), (SELECT id FROM airport WHERE iata_code='SVO'), (SELECT id FROM airport WHERE iata_code='EVN'),
     '2026-10-14 10:15+03', '2026-10-14 13:20+04', NULL, NULL, 'SCHEDULED', 'D5', 'Boeing 737-800'),
    ('S71021', (SELECT id FROM airline WHERE iata_code='S7'), (SELECT id FROM airport WHERE iata_code='DME'), (SELECT id FROM airport WHERE iata_code='AER'),
     '2026-10-13 14:00+03', '2026-10-13 16:30+03', '2026-10-13 14:40+03', NULL, 'IN_AIR', '7', 'Airbus A321neo'),
    ('DP405',  (SELECT id FROM airline WHERE iata_code='DP'), (SELECT id FROM airport WHERE iata_code='SVO'), (SELECT id FROM airport WHERE iata_code='KZN'),
     '2026-10-13 07:30+03', '2026-10-13 09:00+03', '2026-10-13 07:35+03', '2026-10-13 08:55+03', 'LANDED', '21', 'Boeing 737-800'),
    ('TK414',  (SELECT id FROM airline WHERE iata_code='TK'), (SELECT id FROM airport WHERE iata_code='SVO'), (SELECT id FROM airport WHERE iata_code='IST'),
     '2026-10-13 19:45+03', '2026-10-13 23:10+03', NULL, NULL, 'DELAYED', 'F3', 'Airbus A330'),
    ('EK132',  (SELECT id FROM airline WHERE iata_code='EK'), (SELECT id FROM airport WHERE iata_code='DME'), (SELECT id FROM airport WHERE iata_code='DXB'),
     '2026-10-14 22:00+03', '2026-10-15 04:10+04', NULL, NULL, 'SCHEDULED', '14', 'Boeing 777-300ER'),
    ('SU1863', (SELECT id FROM airline WHERE iata_code='SU'), (SELECT id FROM airport WHERE iata_code='EVN'), (SELECT id FROM airport WHERE iata_code='SVO'),
     '2026-10-13 05:00+04', '2026-10-13 06:10+03', NULL, NULL, 'CANCELLED', NULL, 'Boeing 737-800');

INSERT INTO flight_event (flight_id, old_status, new_status, comment, created_at) VALUES
    ((SELECT id FROM flight WHERE flight_number='DP405'),  'SCHEDULED', 'BOARDING', 'Начата посадка',        '2026-10-13 07:00+03'),
    ((SELECT id FROM flight WHERE flight_number='DP405'),  'BOARDING',  'IN_AIR',   'Вылет',                 '2026-10-13 07:35+03'),
    ((SELECT id FROM flight WHERE flight_number='DP405'),  'IN_AIR',    'LANDED',   'Посадка в Казани',      '2026-10-13 08:55+03'),
    ((SELECT id FROM flight WHERE flight_number='S71021'), 'SCHEDULED', 'DELAYED',  'Позднее прибытие ВС',   '2026-10-13 13:30+03'),
    ((SELECT id FROM flight WHERE flight_number='S71021'), 'DELAYED',   'IN_AIR',   'Вылет',                 '2026-10-13 14:40+03'),
    ((SELECT id FROM flight WHERE flight_number='TK414'),  'SCHEDULED', 'DELAYED',  'Метеоусловия в Стамбуле','2026-10-13 17:00+03'),
    ((SELECT id FROM flight WHERE flight_number='SU1863'), 'SCHEDULED', 'CANCELLED','Отмена по решению АК',   '2026-10-12 20:00+03');
