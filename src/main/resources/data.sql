insert into users(email, password) values
("userA@google.com", "1234"),
("userB@google.com", "1234"),
("userC@google.com", "1234"),
("userD@google.com", "1234");

insert into point(user_id, amount, updated_at) values
(1, 10000, now()),
(2, 10000, now()),
(3, 10000, now()),
(4, 10000, now());


insert into concert(title, description) values
("콘서트 1", "콘서트 설명 1"),
("콘서트 2", "콘서트 설명 2"),
("콘서트 3", "콘서트 설명 3"),
("콘서트 4", "콘서트 설명 4"),
("콘서트 5", "콘서트 설명 5");

insert into schedule(concert_id, status, concert_at, reservation_at, deadline_at) values
(1,"OPEN", date_add(now(), interval 7 day), now(), date_add(now(), interval 3 day)),
(2,"OPEN", date_add(now(), interval 7 day), now(), date_add(now(), interval 3 day)),
(3,"OPEN", date_add(now(), interval 7 day), now(), date_add(now(), interval 3 day)),
(4,"OPEN", date_add(now(), interval 7 day), now(), date_add(now(), interval 3 day)),
(5,"OPEN", date_add(now(), interval 7 day), now(), date_add(now(), interval 3 day));


insert into seat(schedule_id, seat_no, seat_price, status) values
(1, "seta_01", 10000, "AVAILABLE"),
(1, "seta_02", 10000, "AVAILABLE"),
(1, "seta_03", 10000, "AVAILABLE"),
(1, "seta_04", 10000, "AVAILABLE"),
(1, "seta_05", 10000, "AVAILABLE"),
(1, "seta_06", 10000, "AVAILABLE"),
(1, "seta_07", 10000, "AVAILABLE"),
(1, "seta_08", 10000, "AVAILABLE"),
(1, "seta_09", 10000, "AVAILABLE"),
(1, "seta_10", 10000, "AVAILABLE"),
(2, "seta_01", 10000, "AVAILABLE"),
(2, "seta_02", 10000, "AVAILABLE"),
(2, "seta_03", 10000, "AVAILABLE"),
(2, "seta_04", 10000, "AVAILABLE"),
(2, "seta_05", 10000, "AVAILABLE"),
(2, "seta_06", 10000, "AVAILABLE"),
(2, "seta_07", 10000, "AVAILABLE"),
(2, "seta_08", 10000, "AVAILABLE"),
(2, "seta_09", 10000, "AVAILABLE"),
(2, "seta_10", 10000, "AVAILABLE"),
(3, "seta_01", 10000, "AVAILABLE"),
(3, "seta_02", 10000, "AVAILABLE"),
(3, "seta_03", 10000, "AVAILABLE"),
(3, "seta_04", 10000, "AVAILABLE"),
(3, "seta_05", 10000, "AVAILABLE"),
(3, "seta_06", 10000, "AVAILABLE"),
(3, "seta_07", 10000, "AVAILABLE"),
(3, "seta_08", 10000, "AVAILABLE"),
(3, "seta_09", 10000, "AVAILABLE"),
(3, "seta_10", 10000, "AVAILABLE"),
(4, "seta_01", 10000, "AVAILABLE"),
(4, "seta_02", 10000, "AVAILABLE"),
(4, "seta_03", 10000, "AVAILABLE"),
(4, "seta_04", 10000, "AVAILABLE"),
(4, "seta_05", 10000, "AVAILABLE"),
(4, "seta_06", 10000, "AVAILABLE"),
(4, "seta_07", 10000, "AVAILABLE"),
(4, "seta_08", 10000, "AVAILABLE"),
(4, "seta_09", 10000, "AVAILABLE"),
(4, "seta_10", 10000, "AVAILABLE"),
(5, "seta_01", 10000, "AVAILABLE"),
(5, "seta_02", 10000, "AVAILABLE"),
(5, "seta_03", 10000, "AVAILABLE"),
(5, "seta_04", 10000, "AVAILABLE"),
(5, "seta_05", 10000, "AVAILABLE"),
(5, "seta_06", 10000, "AVAILABLE"),
(5, "seta_07", 10000, "AVAILABLE"),
(5, "seta_08", 10000, "AVAILABLE"),
(5, "seta_09", 10000, "AVAILABLE"),
(5, "seta_10", 10000, "AVAILABLE");