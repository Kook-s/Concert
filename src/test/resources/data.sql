insert into users(email, password) values ("userA@google.com", "1234");
insert into users(email, password) values ("userB@google.com", "1234");
insert into users(email, password) values ("userC@google.com", "1234");

insert into point(user_id, amount, updated_at)values (1, 10000, now());
insert into point(user_id, amount, updated_at)values (2, 10000, now());
insert into point(user_id, amount, updated_at)values (3, 10000, now());
