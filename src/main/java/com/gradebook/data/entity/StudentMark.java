package com.gradebook.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StudentMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer mark;

    private String type;

    private String description;

    @ManyToOne
    @NotNull
    private Student student;

    @ManyToOne
    @NotNull
    private Lesson lesson;
}
