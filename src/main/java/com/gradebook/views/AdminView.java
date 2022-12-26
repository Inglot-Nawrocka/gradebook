package com.gradebook.views;

import com.gradebook.data.entity.*;
import com.gradebook.data.service.*;
import com.gradebook.views.components.AppNotification;
import com.gradebook.views.components.CustomCrudFormFactory;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

import javax.annotation.security.RolesAllowed;

@Route(value = "admin", layout = MainLayout.class)
@RolesAllowed(value = "ADMIN")
public class AdminView extends VerticalLayout {

    private final UserCredentialService userCredentialService;
    private final SubjectService subjectService;
    private final Binder<UserCredential> binder;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ClassGroupService classGroupService;
    private final LessonService lessonService;
    private final TimeTableLessonService timeTableLessonService;
    private final StudentMarkService studentMarkService;

    public AdminView(SubjectService subjectService, UserCredentialService userCredentialService, TeacherService teacherService, StudentService studentService, ClassGroupService classGroupService, LessonService lessonService, TimeTableLessonService timeTableLessonService, StudentMarkService studentMarkService) {
        this.userCredentialService = userCredentialService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.classGroupService = classGroupService;
        this.lessonService = lessonService;
        this.timeTableLessonService = timeTableLessonService;
        this.studentMarkService = studentMarkService;
        this.binder = new Binder<>(UserCredential.class);
        add(makeAccordion());
    }

    private Accordion makeAccordion() {
        Accordion accordion = new Accordion();
        accordion.add("User", getUserGridCrud());
        accordion.add("Teacher", getTeacherGridCrud());
        accordion.add("Students",getStudentGridCrud());
        accordion.add("Class Groups", getClassGroupGridCrud());
        accordion.add("Subjects", getSubjectGridCrud());
        accordion.add("Lessons", getLessonGridCrud());
        accordion.add("Time Table Lessons", getTimeTableLessonsGridCrud());
        accordion.add("Students Marks", getStudentsMarksGridCrud());
        accordion.setWidth("100%");
        return accordion;
    }


    private GridCrud<Teacher> getTeacherGridCrud() {
        GridCrud<Teacher> grid = new GridCrud<>(Teacher.class, teacherService);
        grid.getGrid().setColumns("firstName", "lastName", "address", "phoneNumber", "email");
        grid.getCrudFormFactory().setVisibleProperties("firstName", "lastName", "address", "phoneNumber", "email");
        return grid;
    }

    private GridCrud<Student> getStudentGridCrud() {
        GridCrud<Student> grid = new GridCrud<>(Student.class, studentService);
        grid.getGrid().setColumns("firstName", "lastName", "address", "phoneNumber", "email");
        grid.getGrid().addColumn(stu -> stu.getClassGroup().getName())
                .setHeader("Class Group")
                .setKey("key")
                .setSortable(true)
                .setAutoWidth(true);
        grid.getCrudFormFactory().setFieldProvider("classGroup",() ->{
            ComboBox<ClassGroup> listBoxClasses = new ComboBox<>();
            listBoxClasses.setItems(classGroupService.findAll());
            listBoxClasses.setItemLabelGenerator(ClassGroup::getName);
            return listBoxClasses;
        });
        grid.getCrudFormFactory().setVisibleProperties("firstName", "lastName", "address", "phoneNumber", "email", "classGroup");
        return grid;
    }

    private GridCrud<UserCredential> getUserGridCrud() {

        GridCrud<UserCredential> grid = new GridCrud<>(UserCredential.class, userCredentialService);
        CustomCrudFormFactory<UserCredential> crudFormFactory = new CustomCrudFormFactory<>(UserCredential.class);

        grid.setCrudFormFactory(crudFormFactory);
        crudFormFactory.setButtonCaption(CrudOperation.ADD,"Adding User");
        crudFormFactory.setFieldCreationListener(CrudOperation.ADD,"username", field -> {
            field.setRequiredIndicatorVisible(true);
            field.addValueChangeListener(event -> {
                if(userCredentialService.existsUserName(event.getValue().toString())){
                    new AppNotification(NotificationVariant.LUMO_ERROR, "User Already Exists! Choose different name!").open();
                    crudFormFactory.getOperationButton().setEnabled(false);
                }
                else {
                    crudFormFactory.getOperationButton().setEnabled(true);
                }
            });
        });
        grid.getGrid().setColumns("username","roles");
        grid.getCrudFormFactory().setVisibleProperties("username","password","roles");
        grid.getCrudFormFactory().setFieldProvider(CrudOperation.UPDATE, "username", () ->{
            TextField textField = new TextField();
            textField.setRequired(true);

            textField.setRequiredIndicatorVisible(true);
            textField.setErrorMessage("User already exists!");

            binder.forField(textField)
                    .withValidator(userCredentialService::existsUserName,"User already exsist")
                    .bind(UserCredential::getUsername, UserCredential::setUsername);
            return textField;
        });
        grid.getCrudFormFactory().setFieldProvider("password", () -> {
            PasswordField passwordField = new PasswordField();
            passwordField.setRevealButtonVisible(false);
            passwordField.setRequired(true);
            passwordField.setRequiredIndicatorVisible(true);
            passwordField.addFocusListener(event -> passwordField.setValue(""));
            return passwordField;
        });
        grid.getCrudFormFactory().setFieldProvider("roles",() -> {
            CheckboxGroup<Role> checkBox = new CheckboxGroup<>();
            checkBox.setItems(Role.getRoles());
            checkBox.setItemLabelGenerator(Role::getRoleName);
            return checkBox;
        }
        );
        return grid;
    }

    private GridCrud<Subject> getSubjectGridCrud() {
        GridCrud<Subject> grid = new GridCrud<>(Subject.class, subjectService);
        grid.getGrid().setColumns("name");
        grid.getCrudFormFactory().setVisibleProperties("name");
        return grid;
    }

    private GridCrud<ClassGroup> getClassGroupGridCrud(){
        GridCrud<ClassGroup> grid = new GridCrud<>(ClassGroup.class, classGroupService);
        grid.getGrid().setColumns("name", "schoolYear");
        grid.getCrudFormFactory().setVisibleProperties("name", "schoolYear");
        return grid;
    }

    private GridCrud<Lesson> getLessonGridCrud(){
        GridCrud<Lesson> grid = new GridCrud<>(Lesson.class, lessonService);
        grid.getGrid().setColumns("date", "title", "description");
        grid.getGrid().addColumn(tTL -> tTL.getTimeTableLesson().getSubject().getName())
                .setHeader("Time Table Lesson")
                .setKey("key")
                .setSortable(true)
                .setAutoWidth(true);
        grid.getCrudFormFactory().setFieldProvider("timeTableLesson",() -> {
            ComboBox<TimeTableLesson> comboBox = new ComboBox<>();
            comboBox.setItems(timeTableLessonService.findAll());
            comboBox.setItemLabelGenerator(val -> val.getSubject().getName());
            return comboBox;
        });
        grid.getCrudFormFactory().setVisibleProperties("date", "title", "description", "timeTableLesson");
        return grid;
    }

    private GridCrud<StudentMark> getStudentsMarksGridCrud() {
        GridCrud<StudentMark> grid = new GridCrud<>(StudentMark.class, studentMarkService);
        grid.getGrid().setColumns("mark", "type", "description");
        grid.getGrid().addColumn(sM -> sM.getStudent().getFullName())
                .setHeader("Student")
                .setSortable(true)
                .setAutoWidth(true);
        grid.getGrid().addColumn(sM -> sM.getLesson().getTitle())
                .setHeader("Lesson")
                .setSortable(true)
                .setAutoWidth(true);
        grid.getCrudFormFactory().setFieldProvider("student",()->{
            ComboBox<Student> comboBox = new ComboBox<>();
            comboBox.setItems(studentService.findAll());
            comboBox.setItemLabelGenerator(Student::getFullName);
            return comboBox;
        });
        grid.getCrudFormFactory().setFieldProvider("lesson",()->{
            ComboBox<Lesson> comboBox = new ComboBox<>();
            comboBox.setItems(lessonService.findAll());
            comboBox.setItemLabelGenerator(Lesson::getTitle);
            return comboBox;
        });
        grid.getCrudFormFactory().setVisibleProperties("mark", "type", "description", "student", "lesson");
        return grid;
    }

    private GridCrud<TimeTableLesson> getTimeTableLessonsGridCrud() {
        GridCrud<TimeTableLesson> grid = new GridCrud<>(TimeTableLesson.class, timeTableLessonService);
        grid.getGrid().setColumns("day", "hour");
        grid.getGrid().addColumn(tTL -> tTL.getTeacher().getFullName())
                .setHeader("Teacher")
                .setSortable(true)
                .setAutoWidth(true);
        grid.getGrid().addColumn(tTL -> tTL.getSubject().getName())
                .setHeader("Subject")
                .setSortable(true)
                .setAutoWidth(true);
        grid.getGrid().addColumn(tTL -> tTL.getClassGroup().getName())
                .setHeader("Class Group")
                .setKey("classess")
                .setSortable(true)
                .setAutoWidth(true);

        grid.getCrudFormFactory().setFieldProvider("teacher",()->{
            ComboBox<Teacher> comboBox = new ComboBox<>();
            comboBox.setItems(teacherService.findAll());
            comboBox.setItemLabelGenerator(Teacher::getFullName);
            return comboBox;
        });
        grid.getCrudFormFactory().setFieldProvider("subject",()->{
            ComboBox<Subject> comboBox = new ComboBox<>();
            comboBox.setItems(subjectService.findAll());
            comboBox.setItemLabelGenerator(Subject::getName);
            return comboBox;
        });
        grid.getCrudFormFactory().setFieldProvider("classGroup",()->{
            ComboBox<ClassGroup> comboBox = new ComboBox<>();
            comboBox.setItems(classGroupService.findAll());
            comboBox.setItemLabelGenerator(ClassGroup::getName);
            return comboBox;
        });
        grid.getCrudFormFactory().setVisibleProperties("day", "hour","teacher", "subject", "classGroup");
        return grid;
    }
}
