DROP TABLE IF EXISTS  cathedra, groups, students, subject, teacher, vacation, holiday, audience, lection, lectionTime, teacher_subject, group_lection CASCADE;

CREATE TABLE IF NOT EXISTS cathedra (
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS groups (
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(20) NOT NULL,
    cathedra_id INTEGER,
    FOREIGN KEY (cathedra_id) REFERENCES cathedra (id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS students (
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(200),
    birthday TIMESTAMP NOT NULL,
    group_id INTEGER,
    FOREIGN KEY (group_id) REFERENCES Groups (id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS teacher (
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(200),
    birthday TIMESTAMP NOT NULL,
    cathedra_id INTEGER,
    FOREIGN KEY (cathedra_id) REFERENCES cathedra (id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS subject (
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(512) NOT NULL
);
CREATE TABLE IF NOT EXISTS vacation (
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    startJob TIMESTAMP NOT NULL,
    endJob TIMESTAMP NOT NULL,
    teacher_id INTEGER, -- description deleted, error in uml
    FOREIGN KEY (teacher_id) REFERENCES teacher (id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS lectionTime (
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    start_lection TIMESTAMP NOT NULL,
    end_lection TIMESTAMP NOT NULL
);
CREATE TABLE IF NOT EXISTS audience (
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    room INTEGER NOT NULL,
    size INTEGER NOT NULL
);
CREATE TABLE IF NOT EXISTS holiday (
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    description VARCHAR(200) NOT NULL,
    start_holiday TIMESTAMP NOT NULL,
    end_holiday TIMESTAMP NOT NULL
);
CREATE TABLE IF NOT EXISTS lection (
    id bigint PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    cathedra_id INTEGER,
    teacher_id INTEGER,
    subject_id INTEGER,
    lecture_time_id INTEGER,
    audience_id INTEGER, -- date deleted, error in uml
    FOREIGN KEY (cathedra_id) REFERENCES cathedra (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teacher (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (lecture_time_id) REFERENCES lectionTime (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (audience_id) REFERENCES audience (id) ON UPDATE CASCADE ON DELETE CASCADE
);

-- many-to-many
CREATE TABLE IF NOT EXISTS teacher_subject (
    teacher_id INTEGER,
    subject_id INTEGER,
    FOREIGN KEY (teacher_id) REFERENCES teacher (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject (id) ON UPDATE CASCADE ON DELETE CASCADE
    --UNIQUE (teacher_id, subject_id)
);
CREATE TABLE IF NOT EXISTS group_lection (
    group_id INTEGER,
    lection_id INTEGER,
    FOREIGN KEY (group_id) REFERENCES groups (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (lection_id) REFERENCES lection (id) ON UPDATE CASCADE ON DELETE CASCADE
    --UNIQUE (teacher_id, subject_id)
);


--
-- CREATE TABLE IF NOT EXISTS Groups_Students
-- (
--     group_id INTEGER,
--     student_id INTEGER,
--     FOREIGN KEY (group_id) REFERENCES Groups (id) ON UPDATE CASCADE ON DELETE CASCADE,
--     FOREIGN KEY (student_id) REFERENCES Students (id) ON UPDATE CASCADE ON DELETE CASCADE
--     --UNIQUE (group_id, student_id)
-- );
--
-- CREATE TABLE IF NOT EXISTS Cathedra_Groups
-- (
--     cathedra_id INTEGER,
--     group_id INTEGER,
--     FOREIGN KEY (cathedra_id) REFERENCES Cathedra (id) ON UPDATE CASCADE ON DELETE CASCADE,
--     FOREIGN KEY (group_id) REFERENCES Groups (id) ON UPDATE CASCADE ON DELETE CASCADE
--     --UNIQUE (cathedra_id, group_id)
-- );
--
