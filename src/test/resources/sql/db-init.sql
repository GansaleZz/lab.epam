create table gift_certificate
(
    gift_id          bigint auto_increment
        primary key,
    name             varchar(25)  not null,
    description      varchar(100) null,
    price            double       not null,
    duration         bigint       not null,
    create_date      datetime     not null,
    last_update_date datetime     not null
);

create table tag
(
    tag_id bigint auto_increment
        primary key,
    name   varchar(25) not null,
    constraint tag_name_uindex
        unique (name)
);

create table gift_tags
(
    gift_id bigint not null,
    tag_id  bigint not null,
    primary key (gift_id, tag_id),
    constraint gift_certificate_fk
        foreign key (gift_id) references gift_certificate (gift_id)
            on delete cascade,
    constraint tag_fk
        foreign key (tag_id) references tag (tag_id)
            on delete cascade
);

create table user
(
    user_id bigint not null
        primary key
);

create table user_order
(
    order_id  bigint auto_increment
        primary key,
    timestamp datetime not null,
    cost      double   not null,
    user_id   bigint   not null,
    gift_id   bigint   null,
    constraint FKe9drqlvogxhvi2cdm8kfobhh3
        foreign key (gift_id) references gift_certificate (gift_id)
            on delete cascade ,
    constraint FKj86u1x7csa8yd68ql2y1ibrou
        foreign key (user_id) references user (user_id)
);


INSERT INTO gift_certificate
VALUES (1,'TEST1', 'TEST1_1', 111, 12, '2021-08-21', '2021-08-27');

INSERT INTO gift_certificate
VALUES (2,'TEST2', 'TEST2_2', 200, 12, '2021-08-23', '2021-08-27');

INSERT INTO gift_certificate
VALUES (3,'TEST3', 'TEST3_3', 600, 12, '2021-08-25', '2021-08-27');

INSERT INTO gift_certificate
VALUES (4,'TEST4', 'TEST4_4', 2900, 12, '2021-08-29', '2021-08-29');

INSERT INTO tag VALUES (1,'TAG_TEST_1');
INSERT INTO tag VALUES (2,'TAG_TEST_2');
INSERT INTO tag VALUES (3,'TAG_TEST_3');
INSERT INTO tag VALUES (4,'TAG_TEST_4');
INSERT INTO tag VALUES (5,'TAG_TEST_5');
INSERT INTO tag VALUES (6,'TAG_TEST_6');

INSERT INTO gift_tags(gift_id, tag_id) VALUES (1,1);
INSERT INTO gift_tags(gift_id, tag_id) VALUES (1,2);
INSERT INTO gift_tags(gift_id, tag_id) VALUES (2,2);
INSERT INTO gift_tags(gift_id, tag_id) VALUES (3,4);
INSERT INTO gift_tags(gift_id, tag_id) VALUES (1,4);

INSERT INTO user(user_id) VALUES (1);
INSERT INTO user(user_id) VALUES (2);
INSERT INTO user(user_id) VALUES (3);
INSERT INTO user(user_id) VALUES (4);

INSERT INTO user_order(order_id, timestamp, cost, user_id, gift_id)
VALUES (1, '2021-08-29', 2900, 1, 4);

INSERT INTO user_order(order_id, timestamp, cost, user_id, gift_id)
VALUES (2, '2021-08-29', 2900, 1, 4);

INSERT INTO user_order(order_id, timestamp, cost, user_id, gift_id)
VALUES (3, '2021-08-29', 600, 1, 3);

INSERT INTO user_order(order_id, timestamp, cost, user_id, gift_id)
VALUES (4, '2021-08-29', 111, 2, 1);

INSERT INTO user_order(order_id, timestamp, cost, user_id, gift_id)
VALUES (5, '2021-08-29', 200, 2, 2);

INSERT INTO user_order(order_id, timestamp, cost, user_id, gift_id)
VALUES (6, '2021-08-29', 111, 2, 1);

INSERT INTO user_order(order_id, timestamp, cost, user_id, gift_id)
VALUES (7, '2021-08-29', 200, 3, 2);