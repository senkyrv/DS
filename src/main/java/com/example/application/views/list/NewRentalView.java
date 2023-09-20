package com.example.application.views.list;

import com.example.application.dao.CarTable;
import com.example.application.dao.CustomerTable;
import com.example.application.dao.RentalTable;
import com.example.application.model.Car;
import com.example.application.model.Customer;
import com.example.application.model.Rental;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Title;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.NotificationVariant;
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
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;


import java.awt.*;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Car rental app | New Rental")
@Route(value = "new_rental")
public class NewRentalView extends VerticalLayout{

    CustomerTable customerTable = new CustomerTable();
    List<Customer> customers = customerTable.select();
    CarTable carTable = new CarTable();
    List<Car> cars = carTable.select();

    HorizontalLayout layout_detail1 = new HorizontalLayout();
    HorizontalLayout layout_detail2 = new HorizontalLayout();
    HorizontalLayout buttons = new HorizontalLayout();

    public NewRentalView() throws SQLException {
        setSizeFull();
        ThemeList themeList = getThemeList();
        themeList.add(Lumo.DARK);

        ComboBox<Customer> boxCustomers = new ComboBox<>("Customer:");
        ComboBox<Car> boxCars = new ComboBox<>("Car:");
        boxCars.setItemLabelGenerator(car -> car.getBrand() + " " + car.getModel());
        boxCustomers.setItemLabelGenerator(customer -> customer.getFirst_name() + " " +customer.getLast_name());

        DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
        multiFormatI18n.setDateFormats("dd.MM.yyyy", "yyyy-MM-dd", "MM/dd/yyyy");

        DatePicker datePicker_start = new DatePicker("Start date:");
        datePicker_start.setI18n(multiFormatI18n);
        DatePicker datePicker_end = new DatePicker("End date:");
        datePicker_end.setI18n(multiFormatI18n);

        boxCustomers.setPlaceholder("Select customer");
        boxCars.setPlaceholder("Select car");

        boxCustomers.setItems(customers);
        boxCars.setItems(cars);
        layout_detail1.add(boxCustomers, datePicker_start);
        layout_detail2.add(boxCars,datePicker_end);
        layout_detail1.setVerticalComponentAlignment(Alignment.CENTER, layout_detail1);
        layout_detail2.setVerticalComponentAlignment(Alignment.CENTER, layout_detail2);

        Button newRental = new Button("Create rental");
        Button back = new Button("Back");
        Button newCustomer = new Button("New customer");
        newCustomer.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newRental.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        back.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        buttons.setVerticalComponentAlignment(Alignment.CENTER, buttons);
        buttons.add(newRental, newCustomer, back);

        newCustomer.addClickListener(clickEvent -> {
            UI.getCurrent().navigate(NewCustomerView.class);
        });
        newRental.addClickListener(clickEvent -> {
            RentalTable rentalTable = new RentalTable();
            Rental rental = new Rental();
            rental.setRental_start(java.sql.Date.valueOf(datePicker_start.getValue()));
            rental.setRental_end(java.sql.Date.valueOf(datePicker_end.getValue()));
            rental.setCustomerID(boxCustomers.getValue().getId());
            rental.setCarID(boxCars.getValue().getId());
            rental.setUserID(1);
            int isOk = 0;
            try {
                isOk = rentalTable.insert(rental);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(isOk == 1){
                Notification notification = Notification.show("You have created new rental!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                UI.getCurrent().navigate(RentalsView.class);
            }else{
                Notification notification = Notification.show("Can't select past date!");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

        });
        back.addClickListener(clickEvent -> {
            UI.getCurrent().navigate("rentals");
        });
        add(layout_detail1, layout_detail2, buttons);

    }

}
