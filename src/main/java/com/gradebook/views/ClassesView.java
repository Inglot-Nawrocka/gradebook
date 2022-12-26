package com.gradebook.views;

import com.gradebook.data.entity.Student;
import com.gradebook.data.service.ClassGroupService;
import com.gradebook.data.service.StudentService;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Route(value = "classes", layout = MainLayout.class)
@PageTitle("Gradebook")
@RolesAllowed({"ADMIN","TEACHER"})
public class ClassesView extends VerticalLayout {

    private final StudentService studentService;
    private final ClassGroupService classGroupService;

    private final Student student = new Student();

    public ClassesView(StudentService studentService, ClassGroupService classGroupService) {
        this.studentService = studentService;
        this.classGroupService = classGroupService;

        add(
                new H2("Class Groups"),
                getClassesAccordion()
        );
    }

    private Accordion getClassesAccordion() {
        Accordion accordion = new Accordion();

        classGroupService.findAll().forEach(classGroup -> {
            Long id = classGroup.getId();

            accordion.add(classGroup.getName(),
                    new VerticalLayout(
                            getStudentPanel(id)
                    ));
            }
        );
        accordion.setWidth("100%");


        return accordion;
    }

    private VerticalLayout getStudentPanel(Long id) {
        // build view with students table and the button
        VerticalLayout verticalLayout = new VerticalLayout();
        List<Student> students = studentService.findStudentsByClassGroup(id);

        Grid<Student> grid = new Grid<>(Student.class, false);
        grid.addColumn(Student::getFirstName).setHeader("First name").setSortable(true);
        grid.addColumn(Student::getLastName).setHeader("Last name").setSortable(true);
        grid.addColumn(Student::getPhoneNumber).setHeader("Phone Number").setSortable(true);
        grid.addColumn(Student::getEmail).setHeader("Email").setSortable(true);
        grid.addColumn(Student::getAddress).setHeader("Address").setSortable(true);

        grid.setItems(students);
        grid.setAllRowsVisible(true);

        verticalLayout.add(grid);


        return verticalLayout;
    }

}

