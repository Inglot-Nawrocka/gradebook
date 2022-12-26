package com.gradebook.data.service;

import com.gradebook.data.repository.ClassGroupRepository;
import com.gradebook.data.entity.ClassGroup;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

import java.util.List;

@Service
public class ClassGroupService implements CrudListener<ClassGroup> {
    private final ClassGroupRepository classGroupRepository;

    public ClassGroupService(ClassGroupRepository classGroupRepository) {
        this.classGroupRepository = classGroupRepository;
    }
    @Override
    public List<ClassGroup> findAll() {
        return classGroupRepository.findAll();
    }

    public ClassGroup getClassById(Long id) {
        return this.classGroupRepository.findById(id).orElse(null);
    }

    @Override
    public ClassGroup add(ClassGroup classGroup) {
        return classGroupRepository.save(classGroup);
    }

    @Override
    public ClassGroup update(ClassGroup classGroup) {
        return classGroupRepository.save(classGroup);
    }

    @Override
    public void delete(ClassGroup classGroup) {
        classGroupRepository.delete(classGroup);
    }
}
