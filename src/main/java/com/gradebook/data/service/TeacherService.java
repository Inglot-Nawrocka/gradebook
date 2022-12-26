package com.gradebook.data.service;

import com.gradebook.data.repository.TeacherRepository;
import com.gradebook.data.entity.Teacher;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

import java.util.List;

@Service
public class TeacherService implements CrudListener<Teacher> {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<Teacher> findAll() {
        return this.teacherRepository.findAll();
    }

    @Override
    public Teacher add(Teacher teacher) {
        return this.teacherRepository.save(teacher);
    }

    @Override
    public Teacher update(Teacher teacher) {
        return this.teacherRepository.save(teacher);
    }

    @Override
    public void delete(Teacher teacher) {
        this.teacherRepository.delete(teacher);
    }
}
