package com.gradebook.data.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Teacher extends AbstractUser {

    @OneToMany(mappedBy="teacher", fetch = FetchType.EAGER)
    private List<TimeTableLesson> timeTableLessons = new LinkedList<>();
}
