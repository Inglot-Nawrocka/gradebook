package com.gradebook.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ClassGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String schoolYear;

    @OneToMany(mappedBy = "classGroup", cascade = CascadeType.REMOVE)
    private List<Student> students = new LinkedList<>();

    @OneToMany(mappedBy="classGroup", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<TimeTableLesson> timeTableLessons = new LinkedList<>();
}
