package com.gradebook.data.repository;

import com.gradebook.data.entity.StudentMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface StudentMarkRepository extends JpaRepository<StudentMark, Long> {

    @Transactional
    @Query("select s from StudentMark s where s.student.id = :studentId and s.lesson.id = :lessonId")
    List<StudentMark> findByStudentAndLesson(@Param("studentId") Long studentId, @Param("lessonId") Long lessonId);
}
