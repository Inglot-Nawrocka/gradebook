package com.gradebook.views;

import com.gradebook.data.service.*;
import com.gradebook.views.components.*;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@Route(value = "timetable", layout = MainLayout.class)
@PageTitle("TimeTable")
@RolesAllowed({"ADMIN","TEACHER"})
public class TimeTableView extends VerticalLayout {
    private final AuthenticatedUser authenticatedUser;
    private final TimeTableLessonService timeTableLessonService;
    private final ClassGroupService classGroupService;
    private final TeacherService teacherService;

    private TimeTableView(AuthenticatedUser authenticatedUser, TimeTableLessonService timeTableLessonService, ClassGroupService classGroupService, LessonService lessonService, TeacherService teacherService) {
        this.authenticatedUser = authenticatedUser;
        this.timeTableLessonService = timeTableLessonService;
        this.classGroupService = classGroupService;
        this.teacherService = teacherService;

        H2 h2 = new H2("Time Table");
        h2.getStyle().set("margin","1em");
        VerticalLayout timetable = createTimeTableView();
        timetable.getStyle().set("padding","0em");
        add(
                h2,
                timetable
        );
    }

    private VerticalLayout createTimeTableView() {
        return new TimeTableCalendar(this.classGroupService,this.authenticatedUser,this.timeTableLessonService, this.teacherService);
    }

}