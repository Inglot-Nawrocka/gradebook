package com.gradebook.data.entity;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "studentPresence", cascade = CascadeType.ALL)
    private List<Student> presentStudents = new LinkedList<>();

    private LocalDate date;

    private String title;

    private String description;

    @ManyToOne
    private TimeTableLesson timeTableLesson;

    @OneToMany(mappedBy="lesson", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<StudentMark> studentMarks = new LinkedList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) && Objects.equals(presentStudents, lesson.presentStudents) && Objects.equals(date, lesson.date) && Objects.equals(title, lesson.title) && Objects.equals(description, lesson.description) && Objects.equals(timeTableLesson, lesson.timeTableLesson) && Objects.equals(studentMarks, lesson.studentMarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, presentStudents, date, title, description, timeTableLesson, studentMarks);
    }
}
