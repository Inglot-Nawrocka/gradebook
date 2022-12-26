package com.gradebook.data.service;

import com.gradebook.data.repository.StudentRepository;
import com.gradebook.data.entity.Lesson;
import com.gradebook.data.entity.Student;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

import java.util.*;

@Service
public class StudentService implements CrudListener<Student> {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findStudentsByClassGroup(Long id) {
        return studentRepository.findByClass(id);
    }

    @Override
    public Student add(Student student) {
        return this.studentRepository.save(student);
    }

    @Override
    public void delete(Student student) {
        this.studentRepository.delete(student);
    }

    public void updateStudentPresence(Long studentId, Lesson lesson, Boolean isPresent) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if(student != null) {
            List<Lesson> lessons = student.getStudentPresence();
            boolean isInLessons = lessons
                    .stream()
                    .anyMatch(o -> o.getId().equals(lesson.getId()));

            if(isPresent && !isInLessons) {
                lessons.add(lesson);
            } else {
                lessons.removeIf(o -> o.getId().equals(lesson.getId()));
            }
            student.setStudentPresence(lessons);
            studentRepository.save(student);
        }
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student update(Student student) {
        return studentRepository.save(student);
    }

}