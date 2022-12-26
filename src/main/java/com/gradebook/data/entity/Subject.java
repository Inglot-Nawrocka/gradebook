package com.gradebook.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy="subject", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<TimeTableLesson> timeTableLessons = new LinkedList<>();
}
