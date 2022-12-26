package com.gradebook.data.service;

import com.gradebook.data.repository.TimeTableLessonRepository;
import com.gradebook.data.entity.TimeTableLesson;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class TimeTableLessonService implements CrudListener<TimeTableLesson> {
    private final TimeTableLessonRepository timeTableLessonRepository;

    public TimeTableLessonService(TimeTableLessonRepository timeTableLessonRepository) {
        this.timeTableLessonRepository = timeTableLessonRepository;
    }

    public List<TimeTableLesson> findByDayOfWeek(DayOfWeek day){ return timeTableLessonRepository.findByDayOfWeek(day);}

    @Override
    public List<TimeTableLesson> findAll() {
        return timeTableLessonRepository.findAll();
    }

    @Override
    public TimeTableLesson add(TimeTableLesson lesson) {
        return this.timeTableLessonRepository.save(lesson);
    }

    @Override
    public TimeTableLesson update(TimeTableLesson lesson) {
        return this.timeTableLessonRepository.save(lesson);
    }

    @Override
    public void delete(TimeTableLesson lesson) {
        timeTableLessonRepository.delete(lesson);
    }
}
