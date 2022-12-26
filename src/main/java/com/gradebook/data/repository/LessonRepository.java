package com.gradebook.data.repository;

import com.gradebook.data.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

}
