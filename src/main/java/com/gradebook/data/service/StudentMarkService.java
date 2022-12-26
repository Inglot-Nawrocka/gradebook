package com.gradebook.data.service;

import com.gradebook.data.repository.StudentMarkRepository;
import com.gradebook.data.entity.StudentMark;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

import java.util.List;

@Service
public class StudentMarkService implements CrudListener<StudentMark>  {

    private final StudentMarkRepository studentMarkRepository;

    public StudentMarkService(StudentMarkRepository studentMarkRepository) {
        this.studentMarkRepository = studentMarkRepository;
    }
    @Override
    public List<StudentMark> findAll() {
        return studentMarkRepository.findAll();
    }

    public List<StudentMark> findByStudentAndLesson(Long studentId, Long lessonId) {
        return studentMarkRepository.findByStudentAndLesson(studentId, lessonId);
    }
    @Override
    public StudentMark add(StudentMark studentMark) {
       return studentMarkRepository.save(studentMark);
    }

    @Override
    public StudentMark update(StudentMark studentMark) {
        return studentMarkRepository.save(studentMark);
    }

    @Override
    public void delete(StudentMark studentMark) {
        studentMarkRepository.delete(studentMark);
    }
}


