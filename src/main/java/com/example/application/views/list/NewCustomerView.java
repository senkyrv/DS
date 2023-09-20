package com.example.application.views.list;

import com.example.application.dao.CustomerTable;
import com.example.application.model.Car;
import com.example.application.model.Customer;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.component.textfield.TextField;
import java.awt.*;
import java.sql.SQLException;

@PageTitle("Car rental app | New Customer")
@Route(value = "new_customer")
public class NewCustomerView extends VerticalLayout {
    CustomerTable customerTable = new CustomerTable();

    HorizontalLayout layout_detail1 = new HorizontalLayout();
    HorizontalLayout layout_detail2 = new HorizontalLayout();
    HorizontalLayout buttons = new HorizontalLayout();

    private TextField fname = new TextField("First name:");
    private TextField lname = new TextField("Last name:");
    private IntegerField age = new IntegerField("Age:");

    private TextField email = new TextField("Email:");
    private TextField phone = new TextField("Phone:");

    public NewCustomerView(){
        setSizeFull();
        ThemeList themeList = getThemeList();
        themeList.add(Lumo.DARK);
        layout_detail1.add(fname, lname, age);
        layout_detail2.add(email, phone);
        layout_detail1.setVerticalComponentAlignment(Alignment.CENTER, layout_detail1);
        layout_detail2.setVerticalComponentAlignment(Alignment.CENTER, layout_detail2);
        add(layout_detail1,layout_detail2,buttons);

        Button newCustomer = new Button("Add customer");
        Button back = new Button("Back");
        newCustomer.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        back.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        buttons.setVerticalComponentAlignment(Alignment.CENTER, buttons);
        buttons.add(newCustomer, back);
        back.addClickListener(clickEvent -> {
            UI.getCurrent().navigate("rentals");
        });
        newCustomer.addClickListener(clickEvent -> {
            Customer customer = new Customer();
            customer.setFirst_name(fname.getValue());
            customer.setLast_name(lname.getValue());
            customer.setAge(age.getValue());
            customer.setEmail(email.getValue());
            customer.setPhone(phone.getValue());
            try {
                customerTable.insert(customer);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Notification notification = Notification.show("You have created new customer!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().navigate("new_rental");
        });

    }
}
