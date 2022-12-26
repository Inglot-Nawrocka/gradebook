package com.gradebook.data.repository;

import com.gradebook.data.entity.TimeTableLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Transactional
@Repository
public interface TimeTableLessonRepository extends JpaRepository<TimeTableLesson, Long> {
    @Transactional
    @Query("select t from TimeTableLesson t where t.day = :day ")
    List<TimeTableLesson> findByDayOfWeek(@Param("day") DayOfWeek day);

}
