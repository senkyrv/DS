package com.example.application.views.list;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Title;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.theme.lumo.Lumo;


import java.awt.*;

@PageTitle("Car rental app")
@Route(value = "")
public class LoginView extends Composite<LoginOverlay> implements RouterLayout {

    public LoginView() {

        LoginOverlay loginOverlay = getContent();
        loginOverlay.setOpened(true);
        loginOverlay.setTitle("Car rental app");
        loginOverlay.setDescription("");
        loginOverlay.addLoginListener( e -> {
            if ("user".equals(e.getUsername()) && "project".equals(e.getPassword())){
                UI.getCurrent().navigate(RentalsView.class);
            }else{
                loginOverlay.setError(true);
            }
        });





    }

}
