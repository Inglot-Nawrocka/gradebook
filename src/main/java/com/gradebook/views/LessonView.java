package com.gradebook.views;

import com.gradebook.data.entity.Lesson;
import com.gradebook.data.entity.Student;
import com.gradebook.data.entity.StudentMark;
import com.gradebook.data.service.LessonService;
import com.gradebook.data.service.StudentMarkService;
import com.gradebook.data.service.StudentService;
import com.gradebook.views.components.AppNotification;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;

import javax.annotation.security.PermitAll;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "lesson", layout = MainLayout.class)
@PageTitle("Lesson Overview")
@PermitAll
public class LessonView extends VerticalLayout implements HasUrlParameter<String>, AfterNavigationObserver {
    private Long lessonId;
    private Lesson lesson = new Lesson();
    private final StudentService studentService;

    private final LessonService lessonService;

    private final StudentMarkService studentMarkService;

    private final Map<Long, Grid<StudentMark>> markGrids = new HashMap<>();

    public LessonView(StudentService studentService, LessonService lessonService, StudentMarkService studentMarkService) {
        this.studentService = studentService;
        this.lessonService = lessonService;
        this.studentMarkService = studentMarkService;
    }

    private VerticalLayout addLessonHeader() {
        if(lessonId != null) {
            lesson = lessonService.getLessonById(lessonId);
        }

        Long lessonId = lesson.getTimeTableLesson().getClassGroup().getId();

        VerticalLayout layout = new VerticalLayout();
        H3 pageTitle = new H3("Lesson overview");
        Paragraph subject = new Paragraph("Subject: " +
                lesson.getTimeTableLesson().getSubject().getName()
        );
        Paragraph classGroup = new Paragraph(
                "Class: " +
                        lesson.getTimeTableLesson().getClassGroup().getName());
        H5 topic = new H5("Topic: " + lesson.getTitle());
        Paragraph description = new Paragraph("Description: " + lesson.getDescription());

        layout.add(pageTitle, topic, subject, classGroup, description, getStudentsList(lessonId));

        return layout;
    }

    private Accordion getStudentsList(Long id) {
        List<Student> students = studentService.findStudentsByClassGroup(id);
        Accordion accordion = new Accordion();
        accordion.setWidthFull();

        students.forEach(student -> accordion.add(
                student.getLastName() + " " + student.getFirstName(),
                addMarksTable(student)));
        return accordion;
    }

    private void setStudentPresence(Long studentId, Lesson lesson, Boolean isPresent ) {
        studentService.updateStudentPresence(studentId, lesson, isPresent);
    }

    private VerticalLayout addMarksTable(Student student) {
        Boolean presenceStatus = student.getStudentPresence()
                .stream()
                .anyMatch(o -> o.getId().equals(lesson.getId()));

        VerticalLayout layout = new VerticalLayout();
        List<StudentMark> marks = studentMarkService.findByStudentAndLesson(student.getId(), lessonId);
        Grid<StudentMark> grid = new Grid<>(StudentMark.class, false);
        grid.addColumn(StudentMark::getMark).setHeader("Mark").setSortable(true);
        grid.addColumn(StudentMark::getType).setHeader("Type").setSortable(true);
        grid.addColumn(StudentMark::getDescription).setHeader("Description");
        grid.addColumn(e -> e.getLesson().getTimeTableLesson().getSubject().getName()).setHeader("Subject");
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, mark) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> removeMark(mark, mark.getStudent().getId()));
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                    button.getStyle().set("cursor", "pointer");
                })).setHeader("Remove mark");
        grid.setSortableColumns();
        ListDataProvider<StudentMark> dataProvider = new ListDataProvider<>(marks);
        grid.setItems(dataProvider);
        this.markGrids.putIfAbsent(student.getId(),grid);

        Checkbox studentPresenceCheckbox = new Checkbox();
        studentPresenceCheckbox.setLabel("Student presence");
        studentPresenceCheckbox.setValue(presenceStatus);
        studentPresenceCheckbox.addValueChangeListener(e -> setStudentPresence(student.getId(), lesson, e.getValue()));

        Button addMarkButton = new Button("Add new mark", e -> openDialog(student));
        addMarkButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);

        layout.add(studentPresenceCheckbox, addMarkButton, grid);

        return layout;
    }

    private void openDialog(Student student) {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add new grade");
        dialog.setModal(false);
        dialog.setDraggable(true);
        dialog.setWidth("30%");

        H3 title = new H3("Add new grade");
        dialog.add(title, addDialogContent(dialog, student));

        dialog.open();
    }

    private VerticalLayout addDialogContent(Dialog dialog, Student student) {
        StudentMark newMark = new StudentMark();
        Binder<StudentMark> binder = new Binder<>();
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout buttonWrapper = new HorizontalLayout();

        TextField type = new TextField("Type");
        TextArea markDescription = new TextArea("Description");
        IntegerField mark = new IntegerField();
        mark.setLabel("Mark");
        mark.setMin(1);
        mark.setMax(6);
        mark.setValue(1);
        mark.setHasControls(true);


        FormLayout formLayout = new FormLayout();
        formLayout.add(mark, type, markDescription);

        binder.bind(mark, StudentMark::getMark, StudentMark::setMark);
        binder.bind(type, StudentMark::getType, StudentMark::setDescription);
        binder.bind(markDescription, StudentMark::getDescription, StudentMark::setDescription);

        mark.addValueChangeListener(e -> newMark.setMark(e.getValue()));
        type.addValueChangeListener(e -> newMark.setType(e.getValue()));
        markDescription.addValueChangeListener(e -> newMark.setDescription(e.getValue()));

        newMark.setStudent(student);
        newMark.setLesson(lesson);

        Button saveButton = new Button("Save", e -> saveMark(newMark, dialog));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SMALL);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        buttonWrapper.add(saveButton, cancelButton);
        layout.add(formLayout, buttonWrapper);

        return layout;
    }

    private void saveMark(StudentMark newMark, Dialog dialog) {
        try {
            studentMarkService.add(newMark);
            dialog.close();
            refreshGrid(newMark.getStudent().getId());
            showNotification(NotificationVariant.LUMO_SUCCESS, "Mark saved.");

        } catch(Exception e) {
            showNotification(NotificationVariant.LUMO_ERROR, "Error occurred: " + e.getMessage());
        }
    }

    private void removeMark(StudentMark mark, Long studentId) {
        try {
            studentMarkService.delete(mark);
            refreshGrid(studentId);
            showNotification(NotificationVariant.LUMO_SUCCESS, "Mark removed.");

        } catch(Exception e) {
            showNotification(NotificationVariant.LUMO_ERROR, "Error occurred: " + e.getMessage());
        }
    }

    private void refreshGrid(Long studentId) {
        List<StudentMark> marks = studentMarkService.findByStudentAndLesson(studentId, lessonId);
        this.markGrids.get(studentId).setItems(marks);
        this.markGrids.get(studentId).getDataProvider().refreshAll();

    }
    private void showNotification(NotificationVariant variant, String text) {
        AppNotification notification = new AppNotification(
                variant, text
        );
        notification.open();
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {

        lessonId = Long.parseLong(parameter);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        add(addLessonHeader());
    }
}