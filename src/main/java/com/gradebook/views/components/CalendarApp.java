package com.gradebook.views.components;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import elemental.json.JsonObject;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.FullCalendarScheduler;

import java.time.LocalTime;

@Getter
@Setter
public class CalendarApp extends VerticalLayout {

    private FullCalendar calendar;
    private CalendarViewToolbar toolbar;
    private HorizontalLayout additionalHeader;

    @Builder
    public CalendarApp(Boolean getAdditionalHeader, JsonObject defaultInitialOptions,  boolean withToolbar) {

        createCalendar(getAdditionalHeader, defaultInitialOptions, withToolbar);

    }

    private void createCalendar(Boolean getAdditionalHeader, JsonObject defaultInitialOptions,  boolean withToolbar) {        calendar = FullCalendarBuilder.create()
                .withAutoBrowserTimezone()
                .withInitialOptions(defaultInitialOptions)
                .withScheduler("CC-Attribution-NonCommercial-NoDerivatives")
                .build();

        calendar.getStyle().set("margin-top", "1em");
        calendar.addCustomStyles(".fc .fc-scrollgrid-section-sticky > * {z-index: 1;} ");


        calendar.setHeightByParent();
        ((FullCalendarScheduler) calendar).setResourceAreaWidth("5%");
        ((FullCalendarScheduler) calendar).setSlotMinWidth("100");
        ((FullCalendarScheduler) calendar).setResourcesInitiallyExpanded(false);
        ((FullCalendarScheduler) calendar).setEntryResourceEditable(false);

        calendar.setNowIndicatorShown(true);
        calendar.setNumberClickable(true);

        calendar.setSlotMinTime(LocalTime.of(5, 0));
        calendar.setSlotMaxTime(LocalTime.of(20, 0));

        HorizontalLayout header = new HorizontalLayout();

        if(getAdditionalHeader) {
            additionalHeader = new HorizontalLayout();
            header.add(additionalHeader);
        }
        if(withToolbar) {
            toolbar = createToolbar(CalendarViewToolbar.builder()
                    .calendar(calendar)
                    .viewChangeable(true)
                    .dateChangeable(true));
            calendar.addDatesRenderedListener(event -> toolbar.updateInterval(event.getIntervalStart()));
            header.add(toolbar);
            header.setVerticalComponentAlignment(Alignment.CENTER, toolbar);
        }
        add(header);
        calendar.setWidthFull();
        add(calendar);
        setHorizontalComponentAlignment(Alignment.CENTER, header);
    }

    protected CalendarViewToolbar createToolbar(CalendarViewToolbar.CalendarViewToolbarBuilder toolbarBuilder) {
        return toolbarBuilder != null ? toolbarBuilder.build() : null;
    }
}
