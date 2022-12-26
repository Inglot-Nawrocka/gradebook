package com.gradebook.data.service;

import com.gradebook.data.repository.SubjectRepository;
import com.gradebook.data.entity.Subject;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

import java.util.List;

@Service
public class SubjectService implements CrudListener<Subject> {

    private final SubjectRepository subjectRepository;


    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> findAll(){return subjectRepository.findAll();}

    @Override
    public void delete(Subject subject) {
        this.subjectRepository.delete(subject);
    }

    @Override
    public Subject add(Subject subject) {
        return this.subjectRepository.save(subject);
    }

    @Override
    public Subject update(Subject subject) {
        return this.subjectRepository.save(subject);
    }

}
