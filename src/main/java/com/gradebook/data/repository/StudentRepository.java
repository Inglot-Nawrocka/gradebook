package com.gradebook.data.repository;

import com.gradebook.data.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Transactional
    @Query("select s from Student s where s.classGroup.id = :id ")
    List<Student> findByClass(@Param("id") Long id);
}
