package com.gradebook.data.entity;

import lombok.*;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.LinkedList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Lazy(false)

public class TimeTableLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private DayOfWeek day;

    private String hour;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private ClassGroup classGroup;

    @OneToMany(mappedBy="timeTableLesson", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Lesson> lessons = new LinkedList<>();
}
