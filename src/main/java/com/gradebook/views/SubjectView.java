package com.gradebook.views;

import com.gradebook.data.entity.Subject;
import com.gradebook.data.service.SubjectService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.util.List;


@Route(value = "subjects", layout = MainLayout.class)
@RolesAllowed({"ADMIN", "TEACHER"})
@PageTitle("Subjects")
public class SubjectView extends VerticalLayout {

    List<Subject> subjects;
    private final SubjectService subjectService;
    private final Subject subject = new Subject();
    private final Subject mainSubjectSelected = new Subject();


    public SubjectView(SubjectService subjectService) {
        this.subjectService = subjectService;

        add(new H2("Subjects"));
        add(
                makeLayoutSubjects()
        );
    }

    private VerticalLayout makeLayoutSubjects() {

        subjects = subjectService.findAll();
        Grid<Subject> subjectGrid = new Grid<>(Subject.class, false);
        subjectGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new,(lay, icon) ->{
            lay.add(new Icon(VaadinIcon.PENCIL));
        }))
                .setWidth("2em")
                .setFlexGrow(0);
        Grid.Column<Subject> subjectNameColumn = subjectGrid.addColumn(Subject::getName)
                .setHeader("Name")
                .setAutoWidth(true)
                .setSortable(true);


        List<Subject> subjects = subjectService.findAll();
        subjectGrid.setItems(subjects);
        subjectGrid.setMaxWidth("25em");
        subjectGrid.setAllRowsVisible(true);
        subjectGrid.setSelectionMode(Grid.SelectionMode.NONE);
        subjectGrid.getColumns().forEach(column -> column.setAutoWidth(true));

        getThemeList().clear();
        getThemeList().add("spacing-s");

        return new VerticalLayout(subjectGrid);
    }
}
