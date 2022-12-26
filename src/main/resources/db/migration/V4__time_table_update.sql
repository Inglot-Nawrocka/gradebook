LOCK TABLES
    lesson WRITE,
    time_table_lesson WRITE;

ALTER TABLE lesson
    DROP FOREIGN KEY FK_LESSON_ON_TIMETABLELESSON,
    MODIFY time_table_lesson_id BIGINT NULL;

ALTER TABLE time_table_lesson
modify id BIGINT AUTO_INCREMENT NOT NULL;

ALTER TABLE lesson
    ADD CONSTRAINT FK_LESSON_ON_TIMETABLELESSON FOREIGN KEY (time_table_lesson_id)
        REFERENCES time_table_lesson (id);

UNLOCK TABLES;