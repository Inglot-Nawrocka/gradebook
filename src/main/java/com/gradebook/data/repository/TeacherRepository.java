package com.gradebook.data.repository;

import com.gradebook.data.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
