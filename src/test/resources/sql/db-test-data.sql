INSERT INTO gift_certificate
VALUES (1,'TEST1', 'TEST1_1', 111, 12, '2021-08-21', '2021-08-27');

INSERT INTO gift_certificate
VALUES (2,'TEST2', 'TEST2_2', 111, 12, '2021-08-23', '2021-08-27');

INSERT INTO gift_certificate
VALUES (3,'TEST3', 'TEST3_3', 111, 12, '2021-08-25', '2021-08-27');

INSERT INTO gift_certificate
VALUES (4,'TEST4', 'TEST4_4', 111, 12, '2021-08-29', '2021-08-29');

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