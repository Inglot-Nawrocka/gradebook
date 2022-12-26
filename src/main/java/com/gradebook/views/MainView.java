package com.gradebook.views;

import com.gradebook.data.service.LessonService;
import com.gradebook.data.service.TimeTableLessonService;
import com.gradebook.views.components.MainViewCalendar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;


@Route(value = "", layout = MainLayout.class)
@PermitAll
@PageTitle("Gradebook")
public class MainView extends VerticalLayout {
    private final LessonService lessonService;
    private final TimeTableLessonService timeTableLessonService;
    public MainView(LessonService lessonService, TimeTableLessonService timeTableLessonService) {
        this.lessonService = lessonService;
        this.timeTableLessonService = timeTableLessonService;

        add(
                new H1("Home"),
                createOverview()
        );
    }

    private VerticalLayout createOverview() {
        return new MainViewCalendar(this.lessonService, this.timeTableLessonService);
    }
}
