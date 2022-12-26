package com.gradebook.views;

import com.gradebook.data.entity.Role;
import com.gradebook.data.entity.UserCredential;
import com.gradebook.data.service.AuthenticatedUser;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import java.util.Optional;


public class MainLayout extends AppLayout {
    private final AuthenticatedUser authenticatedUser;

    public MainLayout(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H3 logo = new H3("Gradebook");
        logo.addClassNames("text-m", "m-m");

        Button logout = new Button("Logout", event -> authenticatedUser.logout());
        HorizontalLayout buttonLayout = new HorizontalLayout(logout);
        buttonLayout.setMargin(true);
        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
                buttonLayout
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink mainLink = new RouterLink("Overview", MainView.class);
        mainLink.setHighlightCondition(HighlightConditions.sameLocation());

        if(checkUser(Role.ADMIN)){
            addToDrawer(new VerticalLayout(
                            mainLink,
                            new RouterLink("Class Groups", ClassesView.class),
                            new RouterLink("Subjects", SubjectView.class),
                            new RouterLink("Timetable", TimeTableView.class),
                            new RouterLink("Admin View", AdminView.class)
            ));
        }  else if (checkUser(Role.TEACHER)){
            addToDrawer(new VerticalLayout(
                    mainLink,
                    new RouterLink("Class Groups", ClassesView.class),
                    new RouterLink("Subjects", SubjectView.class),
                    new RouterLink("Timetable", TimeTableView.class)
            ));
        }
        else   {
            addToDrawer(new VerticalLayout(
                    mainLink
            ));
        }
    }

    private boolean checkUser(Role role) {
        Optional<UserCredential> optionalUser = authenticatedUser.get();
        return optionalUser.map(user -> user.getRoles().contains(role)).orElse(false);
    }


}
