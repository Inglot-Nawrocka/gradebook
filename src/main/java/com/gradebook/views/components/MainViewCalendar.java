package com.gradebook.views.components;

import com.gradebook.data.entity.Lesson;
import com.gradebook.data.entity.TimeTableLesson;
import com.gradebook.data.service.LessonService;
import com.gradebook.data.service.TimeTableLessonService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import elemental.json.Json;
import elemental.json.JsonObject;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.dataprovider.EagerInMemoryEntryProvider;
import org.vaadin.stefan.fullcalendar.dataprovider.EntryProvider;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MainViewCalendar extends VerticalLayout {

    private final LessonService lessonService;
    private final TimeTableLessonService timeTableLessonService;

    DayOfWeek dayOfWeek;
    LocalDate date;

    private FullCalendar calendar;

    public MainViewCalendar(LessonService lessonService, TimeTableLessonService timeTableLessonService) {
        this.lessonService = lessonService;
        this.timeTableLessonService = timeTableLessonService;
        createAppCalendar();
    }
    private void createAppCalendar() {
        CalendarApp calendarApp = CalendarApp.builder()
                .defaultInitialOptions(createDefaultInitialOptions())
                .withToolbar(true)
                .getAdditionalHeader(false)
                .build();
        calendar = calendarApp.getCalendar();

        EagerInMemoryEntryProvider<Entry> entryProvider = EntryProvider.eagerInMemoryFromItems(createEntries());
        calendar.setEntryProvider(entryProvider);

        calendar.addEntryClickedListener(e ->
                UI.getCurrent().navigate("lesson/" + e.getEntry().getCustomProperty("lessonId")));
        calendar.addTimeslotClickedListener(e -> {
            dayOfWeek = e.getDate().getDayOfWeek();
            date = e.getDate();
            openDialog();
        });

        add(calendarApp);
        getStyle().set("padding","0em");


    }

    protected JsonObject createDefaultInitialOptions() {
        JsonObject initialOptions = Json.createObject();
        initialOptions.put("initialView", "dayGridMonth");
        initialOptions.put("contentHeight", "auto");
        initialOptions.put("editable", false);
        initialOptions.put("header", true);
        initialOptions.put("handleWindowResize", true);
        initialOptions.put("timeZone", "local");
        initialOptions.put("locale", "pl");
        initialOptions.put("navLinks", true);
        initialOptions.put("selectable", true);
        initialOptions.put("header", true);
        initialOptions.put("allDaySlot", false);
        initialOptions.put("slotEventOverlap", true);
        initialOptions.put("nowIndicator", true);
        initialOptions.put("slotDuration", "00:30:00");
        initialOptions.put("weekNumberCalculation", "ISO");
        initialOptions.put("slotMinTime", "06:00:00");
        initialOptions.put("slotMaxTime", "18:00:00");
        JsonObject slotLabelFormat = Json.createObject();
        slotLabelFormat.put("hour", "2-digit");
        slotLabelFormat.put("minute", "2-digit");
        slotLabelFormat.put("meridiem", false);
        slotLabelFormat.put("hour12", false);
        initialOptions.put("slotLabelFormat", slotLabelFormat);
        JsonObject dayHeaderFormat = Json.createObject();
        dayHeaderFormat.put("weekday", "long");
        initialOptions.put("dayHeaderFormat", dayHeaderFormat);
        initialOptions.put("weekNumbers", false);

        return initialOptions;
    }

    private List<Entry> createEntries() {
        List<Lesson> lessonsList = lessonService.findAll();
        List<Entry> entries = new ArrayList<>();

        for(Lesson lesson : lessonsList) {
            Entry entry = new Entry();

            entry.setStart(lesson.getDate()
                    .atTime(LocalTime.parse(lesson.getTimeTableLesson().getHour())));
            entry.setEnd(lesson.getDate()
                    .atTime(LocalTime.parse(lesson.getTimeTableLesson().getHour()).plusMinutes(45)));

            entry.setTitle(lesson.getTimeTableLesson().getSubject().getName());
            entry.setDescription(
                    lesson.getTimeTableLesson().getTeacher().getFirstName() + " " +
                            lesson.getTimeTableLesson().getTeacher().getLastName()
            );
            entry.setCustomProperty("lessonId", lesson.getId());
            entry.setColor("light-blue");
            entry.setEditable(true);

            entries.add(entry);
        }

        return entries;
    }
    private void openDialog() {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Add new lesson");
        dialog.setModal(false);
        dialog.setDraggable(true);
        dialog.setWidth("30%");

        H3 title = new H3("Add new lesson");
        dialog.add(title, addDialogContent(dialog));

        dialog.open();
    }

    private VerticalLayout addDialogContent(Dialog dialog) {
        Lesson newLesson = new Lesson();
        Binder<Lesson> binder = new Binder<>();
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout buttonWrapper = new HorizontalLayout();

        TextField title = new TextField("Lesson title");
        TextField teacher = new TextField("Teacher");
        teacher.setReadOnly(true);
        TextField classGroup = new TextField("Class group");
        classGroup.setReadOnly(true);
        TextArea description = new TextArea("Description");
        Select<TimeTableLesson> selectBox = new Select<>();
        selectBox.setItemLabelGenerator(o -> o.getSubject().getName());
        selectBox.setItems(timeTableLessonService.findByDayOfWeek(dayOfWeek));
        selectBox.setLabel("Timetable lesson");
        selectBox.addValueChangeListener(e -> {
            teacher.setValue(
                    e.getValue().getTeacher().getFirstName() + " " +
                            e.getValue().getTeacher().getLastName()
            );
            classGroup.setValue( e.getValue().getClassGroup().getName());
            newLesson.setTimeTableLesson(e.getValue());
            newLesson.setDate(date);
        });

        FormLayout formLayout = new FormLayout();
        formLayout.add(teacher, classGroup, selectBox, title,
                description);

        binder.bind(title, Lesson::getTitle, Lesson::setTitle);
        binder.bind(description, Lesson::getDescription, Lesson::setDescription);
        binder.bind(selectBox, Lesson::getTimeTableLesson, Lesson::setTimeTableLesson);
        title.addValueChangeListener(e -> newLesson.setTitle(e.getValue()));
        description.addValueChangeListener(e -> newLesson.setDescription(e.getValue()));

        Button saveButton = new Button("Save", e -> saveLesson(newLesson, dialog));
        Button cancelButton = new Button("Cancel", e -> dialog.close());

        buttonWrapper.add(saveButton, cancelButton);
        layout.add(formLayout, buttonWrapper);

        return layout;
    }

    private void saveLesson(Lesson lesson, Dialog dialog) {
        EagerInMemoryEntryProvider<Entry> entryProvider;
        try {
            lessonService.add(lesson);
            notificationShow(NotificationVariant.LUMO_SUCCESS, "Lesson added!");
            entryProvider = EntryProvider.eagerInMemoryFromItems(createEntries());
            calendar.setEntryProvider(entryProvider);
            dialog.close();
        } catch (Exception e)
        {
            notificationShow(NotificationVariant.LUMO_ERROR, e.getMessage());
        }
    }

    public void notificationShow(NotificationVariant notVariant, String text){
        AppNotification notification = new AppNotification(
                notVariant,
                text
        );
        notification.open();
    }

}
