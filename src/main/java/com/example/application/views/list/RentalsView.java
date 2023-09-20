package com.example.application.views.list;


import com.example.application.dao.CarTable;
import com.example.application.dao.CustomerTable;
import com.example.application.dao.RentalTable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.model.Rental;
import com.example.application.model.Customer;
import com.example.application.model.Car;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.material.Material;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@PageTitle("Car rental app | Rentals")
@Route(value = "rentals")
public class RentalsView extends VerticalLayout {



    HorizontalLayout grid_layout = new HorizontalLayout();
    HorizontalLayout layout = new HorizontalLayout();
    HorizontalLayout layout_detail1 = new HorizontalLayout();
    HorizontalLayout layout_detail2 = new HorizontalLayout();
    HorizontalLayout layout_detail_buttons = new HorizontalLayout();
    HorizontalLayout layout_date = new HorizontalLayout();
    HorizontalLayout layout_extend = new HorizontalLayout();
    HorizontalLayout layout_dates = new HorizontalLayout();


    private TextField fname = new TextField();
    private TextField lname = new TextField();
    private TextField email = new TextField();
    private TextField phone = new TextField();
    private TextField brand = new TextField();
    private TextField model = new TextField();
    private TextField ftype = new TextField();
    private TextField spz = new TextField();
    private DatePicker date_start = new DatePicker("Start:");
    private DatePicker date_end = new DatePicker("End:");
    private int temp_id;
    Grid<Rental> grid = new Grid<>(Rental.class, false);


    public RentalsView() throws SQLException, ParseException {
        setSizeFull();
        ThemeList themeList = getThemeList();
        themeList.add(Lumo.DARK);

        add(grid_layout);

        setRentalGrid();
        setButtons();
        setDetail();

        layout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, layout);
        add(layout,layout_detail1, layout_detail2, layout_dates, layout_detail_buttons, layout_date,layout_extend);
    }


    public void setRentalGrid() throws SQLException {
        RentalTable rentalTable = new RentalTable();
        List<Rental> rentals = rentalTable.select();
        grid.setItems(rentals);
        grid.setHeight("300px");
        grid.setWidth("1200px");

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatter1 = new SimpleDateFormat("dd.MM.yyyy");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.addColumn(rental -> {
            CustomerTable customerTable = new CustomerTable();
            Customer customer = null;
            try {
                customer = customerTable.findById(rental.getCustomerID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return customer.getFirst_name() + " " + customer.getLast_name();
        }).setHeader("Customer");

        grid.addColumn(rental -> {
            CarTable carTable = new CarTable();
            Car car = null;
            try {
                car = carTable.findById(rental.getCarID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return car.getBrand() + " " + car.getModel();
        }).setHeader("Car");

        grid.addColumn(rental -> {
            String date = null;
            try {
                date = formatter1.format(formatter.parse(String.valueOf(rental.getRental_start())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }).setHeader("Start");

        grid.addColumn(rental -> {
            String date = null;
            try {
                date = formatter1.format(formatter.parse(String.valueOf(rental.getRental_end())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }).setHeader("End");


        grid.addSelectionListener(selection -> {
            Optional<Rental> selectedItem = selection.getFirstSelectedItem();
            temp_id = selection.getFirstSelectedItem().get().getId();
            date_start.setValue((selection.getFirstSelectedItem().get().getRental_start()).toLocalDate());
            date_end.setValue((selection.getFirstSelectedItem().get().getRental_end()).toLocalDate());
            date_start.setReadOnly(true);
            date_end.setReadOnly(true);
            System.out.println(temp_id);
            try {
                CustomerTable customerTable = new CustomerTable();

                fname.setValue(customerTable.findById(selection.getFirstSelectedItem().get().getCustomerID()).getFirst_name());
                lname.setValue(customerTable.findById(selection.getFirstSelectedItem().get().getCustomerID()).getLast_name());
                email.setValue(customerTable.findById(selection.getFirstSelectedItem().get().getCustomerID()).getEmail());
                phone.setValue(customerTable.findById(selection.getFirstSelectedItem().get().getCustomerID()).getPhone());

            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                CarTable carTable = new CarTable();
                brand.setValue(carTable.findById(selection.getFirstSelectedItem().get().getCarID()).getBrand());
                model.setValue(carTable.findById(selection.getFirstSelectedItem().get().getCarID()).getModel());
                ftype.setValue(carTable.findById(selection.getFirstSelectedItem().get().getCarID()).getFuel_type());
                spz.setValue(carTable.findById(selection.getFirstSelectedItem().get().getCarID()).getSpz());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            layout.setVisible(false);
            layout_detail1.setVisible(true);
            layout_detail2.setVisible(true);
            layout_detail_buttons.setVisible(true);
            layout_dates.setVisible(true);
        });
            grid_layout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, grid_layout);
            grid_layout.add(grid);


    }
    public void setButtons(){
        Button addRental = new Button("New rental");
        addRental.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layout.add(addRental);
        addRental.addClickListener(clickEvent -> {
            UI.getCurrent().navigate(NewRentalView.class);
        });
    }
    public void setDetail(){
        Button extend = new Button("Extend");
        Button remove = new Button("Delete");
        Button hide = new Button("Hide");
        remove.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        extend.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        hide.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
        multiFormatI18n.setDateFormats("dd.MM.yyyy", "dd.MM.yyyy");

        DatePicker datePicker_end = new DatePicker("End date:");
        datePicker_end.setI18n(multiFormatI18n);

        Button extendConf = new Button("Extend");
        Button hide2 = new Button("Cancel");
        extendConf.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        hide2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        fname.setLabel("First name:");
        fname.setReadOnly(true);
        lname.setLabel("Last name:");
        lname.setReadOnly(true);
        email.setLabel("Email:");
        email.setReadOnly(true);
        phone.setLabel("Phone:");
        phone.setReadOnly(true);
        brand.setLabel("Brand:");
        brand.setReadOnly(true);
        model.setLabel("Model:");
        model.setReadOnly(true);
        ftype.setLabel("Fuel type:");
        ftype.setReadOnly(true);
        spz.setLabel("Plate number");
        spz.setReadOnly(true);

        layout_detail1.add(fname,lname,brand,model );
        layout_detail2.add(email, phone, ftype, spz);
        layout_detail_buttons.add(extend, remove, hide);
        layout_dates.add(date_start, date_end);
        layout_dates.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, layout_dates);
        layout_detail1.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, layout_detail1);
        layout_detail2.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, layout_detail2);
        layout_detail_buttons.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, layout_detail_buttons);
        layout_extend.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, layout_extend);
        layout_date.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, layout_date);
        layout_detail1.setVisible(false);
        layout_detail2.setVisible(false);
        layout_detail_buttons.setVisible(false);
        layout_dates.setVisible(false);
        layout_date.add(datePicker_end);
        layout_extend.add(extendConf, hide2);
        layout_date.setVisible(false);
        layout_extend.setVisible(false);

        hide.addClickListener(clickEvent -> {
            layout_detail1.setVisible(false);
            layout_detail2.setVisible(false);
            layout_detail_buttons.setVisible(false);
            layout_dates.setVisible(false);
            layout.setVisible(true);
        });
        remove.addClickListener(clickEvent -> {
            RentalTable rentalTable = new RentalTable();
            int isOk = 0;
            try {
                isOk = rentalTable.delete(temp_id);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(isOk == 1){
                Notification notification = Notification.show("Rental was deleted!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                layout_detail1.setVisible(false);
                layout_detail2.setVisible(false);
                layout_detail_buttons.setVisible(false);
                layout_dates.setVisible(false);
                layout.setVisible(true);
                // Something is wrong when I want to read data from database and refresh grid,
                // so this works as substitution for grid refresh
                UI.getCurrent().navigate("new_rental");
                UI.getCurrent().navigate("rentals");


            }else{
                Notification notification = Notification.show("Can't delete ongoing rental!");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }


        });

        extend.addClickListener(clickEvent -> {
            layout_detail1.setVisible(false);
            layout_detail2.setVisible(false);
            layout_detail_buttons.setVisible(false);
            layout.setVisible(false);
            layout_dates.setVisible(false);
            layout_date.setVisible(true);
            layout_extend.setVisible(true);
        });

        extendConf.addClickListener(clickEvent -> {
            RentalTable rentalTable = new RentalTable();
            int isOk = 0;
            try {
                isOk = rentalTable.extend(temp_id, java.sql.Date.valueOf(datePicker_end.getValue()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //UI.getCurrent().getPage().reload();
            if(isOk == 1){
                Notification notification = Notification.show("Rental was extended!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                layout_detail1.setVisible(true);
                layout_detail2.setVisible(true);
                layout_detail_buttons.setVisible(true);
                layout_dates.setVisible(true);
                layout_date.setVisible(false);
                layout_extend.setVisible(false);
                UI.getCurrent().navigate("new_rental");
                UI.getCurrent().navigate("rentals");

            }else{
                Notification notification = Notification.show("Can't select past date!");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }


        });
        hide2.addClickListener(clickEvent -> {
            layout_detail1.setVisible(true);
            layout_detail2.setVisible(true);
            layout_detail_buttons.setVisible(true);
            layout_dates.setVisible(true);
            layout.setVisible(false);
            layout_date.setVisible(false);
            layout_extend.setVisible(false);
        });

    }

}
