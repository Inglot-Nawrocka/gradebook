package com.gradebook.data.service;

import com.gradebook.data.repository.LessonRepository;
import com.gradebook.data.entity.Lesson;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

import java.util.List;

@Service
public class LessonService implements CrudListener<Lesson> {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }
    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson add(Lesson lesson) {
        return this.lessonRepository.save(lesson);
    }

    public Lesson getLessonById(Long id) {
        return this.lessonRepository.findById(id).orElse(null);
    }

    @Override
    public Lesson update(Lesson lesson) {
        return add(lesson);
    }

    @Override
    public void delete(Lesson lesson) {
        this.lessonRepository.delete(lesson);
    }
}
