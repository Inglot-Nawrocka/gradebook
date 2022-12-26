package com.gradebook.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends AbstractUser {

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private ClassGroup classGroup;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "present_students",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    private List<Lesson> studentPresence = new LinkedList<>();

    @OneToMany(mappedBy="student", cascade = CascadeType.REMOVE)
    private List<StudentMark> studentMarks = new LinkedList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return classGroup.equals(student.classGroup) && studentPresence.equals(student.studentPresence) && studentMarks.equals(student.studentMarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classGroup, studentPresence, studentMarks);
    }
}
