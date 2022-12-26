package com.gradebook.views.components;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.stefan.fullcalendar.*;

import java.time.*;
import java.time.format.DateTimeFormatter;

import java.util.*;


/**
 * @author Stefan Uebe
 */
public class CalendarViewToolbar extends MenuBar {
    public static final List<Timezone> SOME_TIMEZONES = Arrays.asList(Timezone.UTC, new Timezone(ZoneId.of("Europe/Berlin")), new Timezone(ZoneId.of("America/Los_Angeles")), new Timezone(ZoneId.of("Japan")));

    private final FullCalendar calendar;
    private final boolean allTimezones;
    private final boolean viewChangeable;
    private final boolean dateChangeable;
    private final boolean settingsAvailable;
    private final List<CalendarView> customViews;


    private CalendarView selectedView = CalendarViewImpl.DAY_GRID_MONTH;
    private Button buttonDatePicker;
    private MenuItem viewSelector;
    private Select<Timezone> timezoneSelector;

    @Builder
    private CalendarViewToolbar(FullCalendar calendar, boolean allTimezones, boolean viewChangeable, boolean dateChangeable, boolean settingsAvailable, List<CalendarView> customViews) {
        this.calendar = calendar;
        this.settingsAvailable = settingsAvailable;
        this.customViews = customViews;

        if (calendar == null) {
            throw new IllegalArgumentException("Calendar instance is required");
        }

        this.allTimezones = allTimezones;
        this.viewChangeable = viewChangeable;
        this.dateChangeable = dateChangeable;

        addThemeVariants(MenuBarVariant.LUMO_CONTRAST);

        initMenuBar();
    }

    protected void initMenuBar() {



        if (dateChangeable) {
            initDateItems();
        }

        if (viewChangeable) {
            initViewSelector();
        }

        if (settingsAvailable) {
            initGeneralSettings();
        }

    }

    private void initDateItems() {
        addItem(VaadinIcon.ANGLE_LEFT.create(), e -> calendar.previous());

        DatePicker gotoDate = new DatePicker();
        gotoDate.addValueChangeListener(event1 -> calendar.gotoDate(event1.getValue()));
        gotoDate.getElement().getStyle().set("visibility", "hidden");
        gotoDate.getElement().getStyle().set("position", "fixed");
        gotoDate.setWidth("0px");
        gotoDate.setHeight("0px");
        gotoDate.setWeekNumbersVisible(true);
        buttonDatePicker = new Button(VaadinIcon.CALENDAR.create());
        buttonDatePicker.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        buttonDatePicker.getElement().appendChild(gotoDate.getElement());
        buttonDatePicker.addClickListener(event -> gotoDate.open());
        buttonDatePicker.setWidthFull();
        addItem(buttonDatePicker);
        addItem(VaadinIcon.ANGLE_RIGHT.create(), e -> calendar.next());
        addItem("Today", e -> calendar.today());
    }

    private SubMenu initGeneralSettings() {
        SubMenu subMenu = addItem("Settings").getSubMenu();

        List<Locale> items = Arrays.asList(CalendarLocale.getAvailableLocales());
        ComboBox<Locale> localeSelector = new ComboBox<>("Locale");
        localeSelector.setClearButtonVisible(true);
        localeSelector.setItems(items);
        localeSelector.setValue(CalendarLocale.getDefault());
        localeSelector.addValueChangeListener(event -> {
            Locale value = event.getValue();
            calendar.setLocale(value != null ? value : CalendarLocale.getDefault());
            Notification.show("Locale changed to " + calendar.getLocale().toLanguageTag());
        });
        localeSelector.setPreventInvalidInput(true);

        timezoneSelector = new Select<>();
        timezoneSelector.setLabel("Timezone");
        timezoneSelector.setItemLabelGenerator(Timezone::getClientSideValue);
        if (allTimezones) {
            timezoneSelector.setItems(Timezone.getAvailableZones());
        } else {
            timezoneSelector.setItems(SOME_TIMEZONES);
        }

        timezoneSelector.setValue(Timezone.UTC);
        timezoneSelector.addValueChangeListener(event -> {
            if (!Objects.equals(calendar.getTimezone(), event.getValue())) {
                Timezone value = event.getValue();
                calendar.setTimezone(value != null ? value : Timezone.UTC);
                Notification.show("Timezone changed to " + calendar.getTimezone());
            }
        });

        subMenu.add(localeSelector, timezoneSelector);

        return subMenu;
    }

    private void initViewSelector() {
        List<CalendarView> calendarViews;
        if (customViews != null && !customViews.isEmpty()) {
            calendarViews = customViews;
            if (!customViews.contains(selectedView)) {
                selectedView = customViews.get(0);
            }
        } else {
            calendarViews = new ArrayList<>(Arrays.asList(CalendarViewImpl.DAY_GRID_MONTH, CalendarViewImpl.TIME_GRID_WEEK, CalendarViewImpl.TIME_GRID_DAY));
        }

        calendarViews.sort(Comparator.comparing(CalendarView::getName));

        viewSelector = addItem("View: " + getViewName(selectedView));
        SubMenu subMenu = viewSelector.getSubMenu();
        calendarViews.stream()
                .sorted(Comparator.comparing(this::getViewName))
                .forEach(view -> {
                    String viewName = getViewName(view);
                    subMenu.addItem(viewName, event -> {
                        calendar.changeView(view);
                        if(view.equals(CalendarViewImpl.TIME_GRID_DAY)){
                            calendar.setWidth("600px");
                        } else {
                            calendar.setWidthFull();
                        }
                        viewSelector.setText("View: " + viewName);
                        selectedView = view;
                    });
                });
    }

    private String getViewName(CalendarView view) {
        String name = null /*customViewNames.get(view)*/;
        if (name == null) {
            name = StringUtils.capitalize(String.join(" ", StringUtils.splitByCharacterTypeCamelCase(view.getClientSideValue())));
        }

        return name;
    }

    public void updateInterval(LocalDate intervalStart) {
        if (buttonDatePicker != null && selectedView != null) {
            updateIntervalLabel(buttonDatePicker, selectedView, intervalStart);
        }
    }

    void updateIntervalLabel(HasText intervalLabel, CalendarView view, LocalDate intervalStart) {
        String text = "--";
        Locale locale = calendar.getLocale();

        if (view instanceof CalendarViewImpl) {
            switch ((CalendarViewImpl) view) {
                default:
                case DAY_GRID_MONTH:
                case LIST_MONTH:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(locale));
                    break;
                case TIME_GRID_DAY:
                case DAY_GRID_DAY:
                case LIST_DAY:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(locale));
                    break;
                case TIME_GRID_WEEK:
                case DAY_GRID_WEEK:
                case LIST_WEEK:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " - " + intervalStart.plusDays(6).format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " (cw " + intervalStart.format(DateTimeFormatter.ofPattern("ww").withLocale(locale)) + ")";
                    break;
                case LIST_YEAR:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("yyyy").withLocale(locale));
                    break;
            }
        } else if (view instanceof SchedulerView) {
            switch ((SchedulerView) view) {
                case TIMELINE_DAY:
                case RESOURCE_TIMELINE_DAY:
                case RESOURCE_TIME_GRID_DAY:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(locale));
                    break;
                case TIMELINE_WEEK:
                case RESOURCE_TIMELINE_WEEK:
                case RESOURCE_TIME_GRID_WEEK:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " - " + intervalStart.plusDays(6).format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " (cw " + intervalStart.format(DateTimeFormatter.ofPattern("ww").withLocale(locale)) + ")";
                    break;
                case TIMELINE_MONTH:
                case RESOURCE_TIMELINE_MONTH:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(locale));
                    break;
                case TIMELINE_YEAR:
                case RESOURCE_TIMELINE_YEAR:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("yyyy").withLocale(locale));
                    break;
            }
        } else {
            String pattern = view != null && view.getDateTimeFormatPattern() != null ? view.getDateTimeFormatPattern() : "MMMM yyyy";
            text = intervalStart.format(DateTimeFormatter.ofPattern(pattern).withLocale(locale));

        }

        intervalLabel.setText(text);
    }

    public void setTimezone(Timezone timezone) {
        if (timezoneSelector != null) {
            timezoneSelector.setValue(timezone);
        }
    }
}
