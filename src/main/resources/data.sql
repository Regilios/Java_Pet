INSERT INTO cathedra (name) VALUES ('General magic');

INSERT INTO groups (name, cathedra_id) VALUES ('Этонсель',(SELECT id FROM cathedra WHERE name='General magic'));
INSERT INTO groups (name, cathedra_id) VALUES ('Ардор',(SELECT id FROM cathedra WHERE name='General magic'));

INSERT INTO students (firstName, lastName, gender, address, email, phone, birthday, group_id) VALUES ('Bob','Peterson','MALE','45 May-dwell House','bob@mail.ru','89942342349','1991-01-01',(SELECT id FROM groups WHERE name='Этонсель'));
INSERT INTO students (firstName, lastName, gender, address, email, phone, birthday, group_id) VALUES ('Margaret','Techer','FEMALE','69 South Street','margaret@mail.ru','8213123123723','1992-10-09',(SELECT id FROM groups WHERE name='Этонсель'));
INSERT INTO students (firstName, lastName, gender, address, email, phone, birthday, group_id) VALUES ('Skot','Malkins','MALE','24 Copthorne Way','skot@mail.ru','89333333333','1993-05-06',(SELECT id FROM groups WHERE name='Этонсель'));
INSERT INTO students (firstName, lastName, gender, address, email, phone, birthday, group_id) VALUES ('Anna','Kollins','FEMALE','14 Ilchester Road','anna@mail.ru','857562323122','1992-08-03',(SELECT id FROM groups WHERE name='Ардор'));
INSERT INTO students (firstName, lastName, gender, address, email, phone, birthday, group_id) VALUES ('Kenny','Braflofski','MALE','22 Wenlock Terrace','kenny@mail.ru','899234234499','1991-02-21',(SELECT id FROM groups WHERE name='Ардор'));
INSERT INTO students (firstName, lastName, gender, address, email, phone, birthday, group_id) VALUES ('Vendy','Testaburger','FEMALE','32 New Dover Rd','vendy@mail.ru','898768761999','1996-11-11',(SELECT id FROM groups WHERE name='Ардор'));

INSERT INTO teacher (firstName, lastName, gender, address, email, phone, birthday, cathedra_id) VALUES ('Жанна','Матвиенко','FEMALE','г. Мытищи, Вокзальная ул., д. 23 кв.181','janna4537@outlook.com','79548458671','1972-02-23', (SELECT id FROM cathedra WHERE name='General magic'));
INSERT INTO teacher (firstName, lastName, gender, address, email, phone, birthday, cathedra_id) VALUES ('Виктор','Лызлов','MALE','г. Муром, Лесной пер., д. 1 кв.69','viktor.lyzlov@outlook.com','79542258111','1980-01-03',(SELECT id FROM cathedra WHERE name='General magic'));

INSERT INTO subject (name, description) VALUES ('Универсальная магия','Применение и изучение общей магии');
INSERT INTO subject (name, description) VALUES ('Мировая магия','Изучение мировой магии и мира духов');
INSERT INTO subject (name, description) VALUES ('История магии','Изучение магии тёмных веков');
INSERT INTO subject (name, description) VALUES ('Защита от тёмных исскуств','Практика и теория применения защитных заклинаний');

INSERT INTO vacation (startJob, endJob, teacher_id) VALUES ('2024-01-01','2035-01-14', (SELECT id FROM teacher WHERE firstName='Жанна' AND lastName='Матвиенко'));
INSERT INTO vacation (startJob, endJob, teacher_id) VALUES ('2024-07-01','2035-07-14', (SELECT id FROM teacher WHERE firstName='Жанна' AND lastName='Матвиенко'));
INSERT INTO vacation (startJob, endJob, teacher_id) VALUES ('2024-02-01','2035-02-14', (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'));
INSERT INTO vacation (startJob, endJob, teacher_id) VALUES ('2024-08-01','2035-08-14', (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'));

INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-02 08:00:00','2024-02-02 10:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-02 11:00:00','2024-02-02 13:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-02 14:00:00','2024-02-02 15:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-02 16:00:00','2024-02-02 18:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-02 18:00:00','2024-02-02 19:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-02 19:00:00','2024-02-02 20:00:00');

INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-03 07:00:00','2024-02-03 08:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-03 09:00:00','2024-02-03 10:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-03 11:00:00','2024-02-03 13:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-03 13:00:00','2024-02-03 14:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-03 14:00:00','2024-02-03 15:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-03 16:00:00','2024-02-03 18:00:00');

INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-04 08:00:00','2024-02-04 10:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-04 10:00:00','2024-02-04 11:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-04 11:00:00','2024-02-04 12:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-04 12:00:00','2024-02-04 14:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-04 15:00:00','2024-02-04 16:00:00');
INSERT INTO lectionTime (start_lection, end_lection) VALUES ('2024-02-04 17:00:00','2024-02-04 19:00:00');

INSERT INTO audience (room_number, capacity) VALUES ('204','40');
INSERT INTO audience (room_number, capacity) VALUES ('402','20');
INSERT INTO audience (room_number, capacity) VALUES ('105','60');
INSERT INTO audience (room_number, capacity) VALUES ('101','40');
INSERT INTO audience (room_number, capacity) VALUES ('307','25');
INSERT INTO audience (room_number, capacity) VALUES ('304','45');

INSERT INTO holiday (description, start_holiday, end_holiday) VALUES ('зимние каникулы','2024-01-01','2024-01-14');
INSERT INTO holiday (description, start_holiday, end_holiday) VALUES ('летние каникулы','2024-07-01','2024-07-14');

INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
    (SELECT id FROM cathedra WHERE name='General magic'),
    (SELECT id FROM teacher WHERE firstName='Жанна' AND lastName='Матвиенко'),
    (SELECT id FROM subject WHERE name='Универсальная магия'),
    1,
    1
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Жанна' AND lastName='Матвиенко'),
   (SELECT id FROM subject WHERE name='Универсальная магия'),
   2,
   1
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Жанна' AND lastName='Матвиенко'),
   (SELECT id FROM subject WHERE name='Универсальная магия'),
   3,
   1
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
    (SELECT id FROM cathedra WHERE name='General magic'),
    (SELECT id FROM teacher WHERE firstName='Жанна' AND lastName='Матвиенко'),
    (SELECT id FROM subject WHERE name='Мировая магия'),
    4,
    2
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Жанна' AND lastName='Матвиенко'),
   (SELECT id FROM subject WHERE name='Мировая магия'),
   5,
   2
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Жанна' AND lastName='Матвиенко'),
   (SELECT id FROM subject WHERE name='Мировая магия'),
   6,
   2
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
    (SELECT id FROM cathedra WHERE name='General magic'),
    (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
    (SELECT id FROM subject WHERE name='История магии'),
    7,
    3
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
   (SELECT id FROM subject WHERE name='История магии'),
   8,
   3
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
   (SELECT id FROM subject WHERE name='История магии'),
   9,
   3
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
   (SELECT id FROM subject WHERE name='История магии'),
   10,
   3
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
   (SELECT id FROM subject WHERE name='История магии'),
   11,
   3
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
   (SELECT id FROM subject WHERE name='История магии'),
   12,
   3
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
    (SELECT id FROM cathedra WHERE name='General magic'),
    (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
    (SELECT id FROM subject WHERE name='Защита от тёмных исскуств'),
    13,
    4
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
   (SELECT id FROM subject WHERE name='Защита от тёмных исскуств'),
   14,
   4
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
   (SELECT id FROM subject WHERE name='Защита от тёмных исскуств'),
   15,
   4
);
INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (
   (SELECT id FROM cathedra WHERE name='General magic'),
   (SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
   (SELECT id FROM subject WHERE name='Защита от тёмных исскуств'),
   16,
   4
);

INSERT INTO teacher_subject (teacher_id, subject_id) VALUES ((SELECT id FROM teacher WHERE firstName='Жанна' AND lastName='Матвиенко'),
                                                             (SELECT id FROM subject WHERE name='Универсальная магия'));
INSERT INTO teacher_subject (teacher_id, subject_id) VALUES ((SELECT id FROM teacher WHERE firstName='Жанна' AND lastName='Матвиенко'),
                                                             (SELECT id FROM subject WHERE name='Мировая магия'));
INSERT INTO teacher_subject (teacher_id, subject_id) VALUES ((SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
                                                             (SELECT id FROM subject WHERE name='История магии'));
INSERT INTO teacher_subject (teacher_id, subject_id) VALUES ((SELECT id FROM teacher WHERE firstName='Виктор' AND lastName='Лызлов'),
                                                             (SELECT id FROM subject WHERE name='Защита от тёмных исскуств'));

INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Этонсель'), 1);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Этонсель'), 2);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Ардор'), 3);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Ардор'), 4);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Этонсель'), 5);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Этонсель'), 6);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Этонсель'), 7);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Ардор'), 8);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Ардор'), 9);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Ардор'), 10);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Этонсель'), 11);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Этонсель'), 12);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Ардор'), 13);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Ардор'), 14);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Ардор'), 15);
INSERT INTO group_lection (group_id, lection_id) VALUES ((SELECT id FROM groups WHERE name='Этонсель'), 16);



