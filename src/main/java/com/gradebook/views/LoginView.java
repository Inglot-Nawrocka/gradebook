package com.gradebook.views;

import com.gradebook.data.service.AuthenticatedUser;
import com.gradebook.views.components.AppNotification;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@AnonymousAllowed
public class LoginView extends Composite<LoginOverlay> {



    public LoginView(AuthenticatedUser authenticatedUser) {

        getContent().setOpened(true);
        getContent().setAction("login");
        getContent().setDescription("Welcome to Gradebook" +
                " Try to login using teacher/teacher or admin/admin");
        getContent().setTitle("Gradebook");
        getContent().addLoginListener(event -> {
            if("teacher".equals(event.getUsername()) || "admin".equals(event.getUsername())){
                UI.getCurrent().navigate("/");
            }
            else {
                AppNotification notification = new AppNotification(
                        NotificationVariant.LUMO_ERROR,
                        "Wrong credentials"
                );
            }
        });
    }
}
