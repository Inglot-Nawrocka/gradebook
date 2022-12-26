package com.gradebook.views.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.shared.Registration;

public class AppNotification extends Notification {
    NotificationVariant notificationVariant;
    String statusText;

    private static final FlexComponent.Alignment alignment = FlexComponent.Alignment.START;

    public AppNotification(NotificationVariant notificationVariant, String statusText) {
        this.notificationVariant = notificationVariant;
        this.statusText = statusText;

        addThemeVariants(notificationVariant);
        setDuration(5000);
        add(
                createNotificationLayout()
        );
    }

    private FlexLayout createNotificationLayout() {
        FlexLayout layout = new FlexLayout();
       Icon icon = VaadinIcon.WARNING.create();
       Div status = new Div(new Text(this.statusText));
       layout.setAlignItems(alignment);
        layout.getStyle()
                .set("flex-direction", "row")
                .set("align-items", "center")
                .set("justify-content", "space-between")
                .set("min-width", "5rem");
        icon.getStyle().set("margin-right", "1rem");
        status.getStyle().set("margin-right", "1rem");
        Button closeButton = new Button(new Icon("lumo", "cross"));

        closeButton.addClickListener(e -> this.close());

        layout.add(icon, status, closeButton);

       return layout;
    }

    // Events
    public static abstract class AppNotificationEvent extends ComponentEvent<Notification> {

        protected AppNotificationEvent(Notification source) {
            super(source, false);
        }
    }
}
