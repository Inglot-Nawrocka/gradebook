CREATE TABLE IF NOT EXISTS teacher
(
    id           BIGINT  AUTO_INCREMENT NOT NULL,
    first_name   VARCHAR(255) NULL,
    last_name    VARCHAR(255) NULL,
    address      VARCHAR(255) NULL,
    phone_number VARCHAR(255) NULL,
    email        VARCHAR(255) NULL,
    CONSTRAINT pk_teacher PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS class_group
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(255)          NULL,
    school_year VARCHAR(255)          NULL,
    CONSTRAINT pk_classgroup PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS subject
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NULL,
    CONSTRAINT pk_subject PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS student
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    first_name     VARCHAR(255) NULL,
    last_name      VARCHAR(255) NULL,
    address        VARCHAR(255) NULL,
    phone_number   VARCHAR(255) NULL,
    email          VARCHAR(255) NULL,
    class_group_id BIGINT       NULL,
    CONSTRAINT pk_student PRIMARY KEY (id)
);

ALTER TABLE student
    ADD CONSTRAINT FK_STUDENT_ON_CLASSGROUP FOREIGN KEY (class_group_id) REFERENCES class_group (id);

CREATE TABLE IF NOT EXISTS time_table_lesson
(
    id             BIGINT       NOT NULL,
    day            INT          NULL,
    hour           VARCHAR(255) NULL,
    teacher_id     BIGINT       NULL,
    subject_id     BIGINT       NULL,
    class_group_id BIGINT       NULL,
    CONSTRAINT pk_timetablelesson PRIMARY KEY (id)
);

ALTER TABLE time_table_lesson
    ADD CONSTRAINT FK_TIMETABLELESSON_ON_CLASSGROUP FOREIGN KEY (class_group_id) REFERENCES class_group (id);

ALTER TABLE time_table_lesson
    ADD CONSTRAINT FK_TIMETABLELESSON_ON_SUBJECT FOREIGN KEY (subject_id) REFERENCES subject (id);

ALTER TABLE time_table_lesson
    ADD CONSTRAINT FK_TIMETABLELESSON_ON_TEACHER FOREIGN KEY (teacher_id) REFERENCES teacher (id);

CREATE TABLE lesson
(
    id                   BIGINT AUTO_INCREMENT NOT NULL,
    date                 datetime              NULL,
    title                VARCHAR(255)          NULL,
    `description`        VARCHAR(255)          NULL,
    time_table_lesson_id BIGINT                NULL,
    CONSTRAINT pk_lesson PRIMARY KEY (id)
);

ALTER TABLE lesson
    ADD CONSTRAINT FK_LESSON_ON_TIMETABLELESSON FOREIGN KEY (time_table_lesson_id) REFERENCES time_table_lesson (id);

CREATE TABLE IF NOT EXISTS student_mark
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    mark          INT                   NULL,
    type          VARCHAR(255)          NULL,
    `description` VARCHAR(255)          NULL,
    student_id    BIGINT                NULL,
    lesson_id     BIGINT                NULL,
    CONSTRAINT pk_studentmark PRIMARY KEY (id)
);

ALTER TABLE student_mark
    ADD CONSTRAINT FK_STUDENTMARK_ON_LESSON FOREIGN KEY (lesson_id) REFERENCES lesson (id);

ALTER TABLE student_mark
    ADD CONSTRAINT FK_STUDENTMARK_ON_STUDENT FOREIGN KEY (student_id) REFERENCES student (id);