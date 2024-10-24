-- h2 실행시 테스트용 데이터 삽입

INSERT INTO USER (user_uuid, point) VALUES (UUID(), 10000), (UUID(), 20000), (UUID(), 30000), (UUID(), 40000), (UUID(), 50000);

INSERT INTO CONCERT (title, singer, place) VALUES ('The Best of 2024', 'John Doe', 'Seoul Stadium'), ('Melody Night', 'Jane Smith', 'Busan Concert Hall'), ('Rock Fest', 'The Rock Band', 'Incheon Arena'), ('Pop Extravaganza', 'Pop Star', 'Gwangju Sports Complex'), ('Jazz Evening', 'Jazz Quartet', 'Daegu Arts Center');

INSERT INTO CONCERT_SCHEDULE (concert_id, date, seat_capacity, seat_left) VALUES (1, '2024-11-01', 50, 50), (2, '2024-11-05', 50, 50), (3, '2024-11-10', 50, 50), (4, '2024-11-15', 50, 50), (5, '2024-11-20', 50, 50);

INSERT INTO CONCERT_SEAT (concert_id, schedule_id, seat_number, status) VALUES (1, 1, 1, 'EMPTY'), (1, 1, 2, 'EMPTY'), (1, 1, 3, 'EMPTY'), (1, 2, 1, 'EMPTY'), (2, 1, 1, 'EMPTY'), (2, 1, 2, 'EMPTY'), (2, 2, 1, 'EMPTY'),(3, 1, 1, 'EMPTY'), (3, 1, 2, 'EMPTY'), (3, 2, 1, 'EMPTY');

INSERT INTO CONCERT_RESERVATION (seat_id, user_id, status, expired_time) VALUES (1, 1, 'PENDING_PAYMENT', '2024-11-01 12:00:00'), (2, 1, 'PENDING_PAYMENT', '2024-11-01 12:05:00'), (3, 2, 'PENDING_PAYMENT', '2024-11-01 12:10:00'), (4, 3, 'PENDING_PAYMENT', '2024-11-01 12:15:00');