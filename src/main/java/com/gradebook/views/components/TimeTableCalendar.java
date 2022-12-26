package com.gradebook.views.components;

import com.gradebook.data.entity.ClassGroup;
import com.gradebook.data.entity.Teacher;
import com.gradebook.data.entity.TimeTableLesson;
import com.gradebook.data.service.AuthenticatedUser;
import com.gradebook.data.service.ClassGroupService;
import com.gradebook.data.service.TeacherService;
import com.gradebook.data.service.TimeTableLessonService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import elemental.json.Json;
import elemental.json.JsonObject;
import org.springframework.boot.ansi.AnsiColor;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.dataprovider.EagerInMemoryEntryProvider;
import org.vaadin.stefan.fullcalendar.dataprovider.EntryProvider;

import java.time.LocalTime;
import java.util.List;

public class TimeTableCalendar extends VerticalLayout {

    private final static String TEACHER = "TEACHER";
    private final static String CLASS = "CLASS";
    private String filter;

    private final FullCalendar calendar;
    private final EagerInMemoryEntryProvider<Entry> entriesProvider;
    private final ClassGroupService classGroupService;
    private final TimeTableLessonService timeTableLessonService;
    private final TeacherService teacherService;
    private ComboBox<ClassGroup> classGroupComboBox;
    private ComboBox<Teacher> teacherComboBox;
    private ClassGroup classGroup;
    private Teacher teacher;

    public TimeTableCalendar(ClassGroupService classGroupService, AuthenticatedUser authenticatedUser, TimeTableLessonService timeTableLessonService, TeacherService teacherService){
        this.classGroupService = classGroupService;
        this.timeTableLessonService = timeTableLessonService;
        this.teacherService = teacherService;
        this.entriesProvider = EntryProvider.eagerInMemory();

        CalendarApp calendarApp = CalendarApp.builder()
                .withToolbar(false)
                .defaultInitialOptions(createDefaultInitialOptions())
                .getAdditionalHeader(true)
                .build();
        this.calendar = calendarApp.getCalendar();
        initializeFilterClass();
        this.filter = TEACHER;
        fillWithData(filter);

        calendarApp.getAdditionalHeader().add(new H3("Sort by Teacher or Class: "),this.teacherComboBox,this.classGroupComboBox);
        calendarApp.getAdditionalHeader().setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.calendar.setEntryProvider(this.entriesProvider);
        calendarApp.getStyle().set("padding","0em");
        add(calendarApp);
        getStyle().set("padding","0em");

    }

    private void fillWithData(String filter) {

        if (this.entriesProvider != null) {
            this.entriesProvider.removeAllEntries();
        }

        if(filter.equals("CLASS")){
            List<TimeTableLesson> timeTableLessons = this.timeTableLessonService.findAll();
            if (this.classGroup == null) {
                this.classGroup = this.timeTableLessonService.findAll().get(0).getClassGroup();
            }
            this.classGroupComboBox.setValue(this.classGroup);
            timeTableLessons.stream()
                    .filter(timeTableLesson -> timeTableLesson
                            .getClassGroup()
                            .getName()
                            .equals(this.classGroup.getName()))
                    .forEach(this::makeEntry);
        }   else if(filter.equals("TEACHER")){
            List<TimeTableLesson> timeTableLessons = this.timeTableLessonService.findAll();
            if(this.teacher==null){
                this.teacher = timeTableLessons.get(0).getTeacher();
            }
            this.teacherComboBox.setValue(this.teacher);
            timeTableLessons.stream()
                    .filter(timeTableLesson -> timeTableLesson
                            .getTeacher()
                            .getFullName()
                            .equals(this.teacher.getFullName()))
                    .forEach(this::makeEntry);
        }
        if (this.calendar != null && this.entriesProvider!=null) {
            this.calendar.setEntryProvider(this.entriesProvider);
        }
    }

    private void makeEntry(TimeTableLesson timeTableLesson) {
        Entry entry = new Entry();
        entry.setRecurringStartTime(LocalTime.parse(timeTableLesson.getHour()));
        entry.setRecurringEndTime(LocalTime.parse(timeTableLesson.getHour()).plusMinutes(45));
        entry.setRecurringDaysOfWeek(timeTableLesson.getDay());
        setViewEntry(entry, timeTableLesson);
        entry.setColor(AnsiColor.BRIGHT_RED.name());
        entry.getBackgroundColor();
        entry.setEditable(false);
        this.entriesProvider.addEntry(entry);
    }

    private void setViewEntry(Entry entry, TimeTableLesson timeTableLesson) {
        if(this.filter.equals(TEACHER)){
            entry.setTitle(timeTableLesson.getSubject().getName()
                    + " | " +
                    timeTableLesson.getClassGroup().getName());
        } else if(this.filter.equals(CLASS)) {
            entry.setTitle(timeTableLesson.getSubject().getName()
                    + " | " +
                    timeTableLesson.getTeacher().getFullName());
        }
    }

    private void initializeFilterClass() {
        this.classGroupComboBox = new ComboBox<>();
        this.classGroupComboBox.setPlaceholder("Choose Class Group");
        this.classGroupComboBox.setWidth(250, Unit.PIXELS);
        List<ClassGroup> listOfClasses = this.classGroupService.findAll();
        this.classGroupComboBox.setItems(listOfClasses);
        this.classGroupComboBox.setItemLabelGenerator(ClassGroup::getName);
        this.classGroupComboBox.addValueChangeListener(event -> changeEntriesByClassId(event.getValue()));

        this.teacherComboBox = new ComboBox<>();
        this.teacherComboBox.setPlaceholder("Choose Teacher");
        this.teacherComboBox.setWidth(250, Unit.PIXELS);
        List<Teacher> listOfTeachers = this.teacherService.findAll();
        this.teacherComboBox.setItems(listOfTeachers);
        this.teacherComboBox.setItemLabelGenerator(Teacher::getFullName);
        this.teacherComboBox.addValueChangeListener(event -> changeEntriesByTeacherId(event.getValue()));
    }

    private void changeEntriesByTeacherId(Teacher teacher) {
        this.calendar.removeAllEntries();
        this.teacher = teacher;
        this.filter = TEACHER;
        fillWithData(this.filter);
    }

    private void changeEntriesByClassId(ClassGroup classGroup) {
        this.calendar.removeAllEntries();
        this.classGroup = classGroup;
        this.filter = CLASS;
        fillWithData(this.filter);
    }

    protected JsonObject createDefaultInitialOptions() {
        JsonObject initialOptions = Json.createObject();
        initialOptions.put("initialView", "timeGridWeek");
        initialOptions.put("contentHeight", "auto");
        initialOptions.put("expandRows", true);
        initialOptions.put("eventMaxStack", 1);
        initialOptions.put("eventMinHeight", 45);
        initialOptions.put("editable", false);
        initialOptions.put("header", true);
        initialOptions.put("handleWindowResize", true);
        initialOptions.put("timeZone", "local");
        initialOptions.put("header", true);
        initialOptions.put("allDaySlot", false);
        initialOptions.put("slotEventOverlap", false);
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
}
