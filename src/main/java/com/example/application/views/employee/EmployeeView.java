package com.example.application.views.employee;

import com.example.application.dto.EmployeeDTO;


import com.example.application.presenter.EmployeePresenter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;




@PageTitle("Employee")
@Route("employee")
@Menu(order = 1, icon = LineAwesomeIconUrl.EMPIRE)
public class EmployeeView extends VerticalLayout implements ObserverEmployeeViewer {




    EmployeePresenter presenter;



     Span dataBaseConnectInfo = new Span("");


    // Employee Search
     TextField searchTextField = new TextField("Employee Search");


    // Employees Grid
     Span employeeListSpan = new Span("Employee List:");
     Span employeesShowStatus = new Span("All Employess data are listing");


     Grid<EmployeeDTO> grid = new Grid<>();



    // EmployeeDTO add/update

     EmployeeDTO employeeDTO = new EmployeeDTO();
    BeanValidationBinder<EmployeeDTO> binder = new BeanValidationBinder<>(EmployeeDTO.class);
     TextField tckn = new TextField("TC ID");
     TextField firstname = new TextField("Firstname");
     TextField lastname = new TextField("Lastname");


    boolean isProcess = false;

    //Search Buttons
     Button searchButton = new Button("Saerch");
     Button clearSearchButton = new Button("Clear/Refresh");

    // Add Button
     Button savedButton = new Button("Save");

    Span editEmployeeSpan = new Span("");



    public EmployeeView(EmployeePresenter employeePresenter){

        presenter = employeePresenter;

        presenter.setObserver(this);

        presenter.load();


        HorizontalLayout employeeListShowSpan = new HorizontalLayout(employeeListSpan, employeesShowStatus);


        Button addRandom = new Button("Add Random", e -> {

            presenter.addNew();

        });

        addRandom.getStyle().set("background-color", "#ffbd03")
                .set("color", "black");


        VerticalLayout employeesGrid = new VerticalLayout(employeeListShowSpan, grid, dataBaseConnectInfo, addRandom);
        employeesGrid.setAlignSelf(Alignment.END, addRandom);

        searchTextField.setPlaceholder("firstname, lastname or TC ID");
        searchTextField.getElement().executeJs(
                "this.querySelector('input').style.fontSize='12px';"
        );


        Button searchButton = new Button("Search", e -> {

            presenter.search(searchTextField.getValue());

        });

        searchButton.getStyle().set("background-color", "#80669d")
                .set("color", "white");

        Button clearSearchButton = new Button("Clear/Refresh");
        clearSearchButton.getStyle().set("background-color", "#5dbea3")
                .set("color", "white");


        clearSearchButton.addClickListener(e -> {
            searchTextField.clear();

            presenter.clearSearch();


        });


        HorizontalLayout searchEmployeeForm = new HorizontalLayout(searchTextField, searchButton, clearSearchButton);
        searchEmployeeForm.setAlignSelf(Alignment.END, searchButton);
        searchEmployeeForm.setAlignSelf(Alignment.END, clearSearchButton);



        // Employee Create / Update

        Span createFormSpan = new Span("Add/Edit Emplooyee");
        EmployeeDTO employeeDTO = new EmployeeDTO();
        binder.bindInstanceFields(this);
        binder.setBean(employeeDTO);

        Button savedButton = new Button("Save");
        savedButton.getStyle().set("background-color", "#4681f4")
                .set("color", "white");

        savedButton.addClickListener(e -> {

            BinderValidationStatus<EmployeeDTO> validationStatus = binder.validate();

            if (validationStatus.hasErrors()) {

                showNotification("Please fill in the required fields", "error");
            }else {
                presenter.add(employeeDTO);
            }





        });


        HorizontalLayout createForm = new HorizontalLayout(tckn, firstname, lastname, savedButton);
        savedButton.getStyle().set("margin-top", "37px");
        // createForm.setAlignSelf(Alignment.END, savedButton);




        VerticalLayout createFormLayout = new VerticalLayout(createFormSpan, editEmployeeSpan, createForm);


        add(searchEmployeeForm, employeesGrid, createFormLayout);



    }


    @Override
    public void onEmployeesLoad(List<EmployeeDTO> employees,
                                String info,
                                String infoColor) {

        dataBaseConnectInfo.setText(info);
        dataBaseConnectInfo.getStyle().set("color", infoColor);

        if(infoColor.equals("green")){
            onCreateGrid(employees);
        }



    }

    @Override
    public void onEmployeeAdd() {

    }

    @Override
    public void onEmployeeDelete() {

    }

    @Override
    public void onEmployeeUpdate() {

    }

    @Override
    public void onEmployeeSearch(String info, String infoColor) {
        employeesShowStatus.setText(info);
        employeesShowStatus.getStyle().set("color", infoColor);
    }


    private void createGrid(List<EmployeeDTO> employees) {

        grid.setItems(employees);
        grid.addColumn(EmployeeDTO::getId).setHeader("ID");
        grid.addColumn(EmployeeDTO::getTckn).setHeader("TC ID Number");
        grid.addColumn(EmployeeDTO::getFirstname).setHeader("First name");
        grid.addColumn(EmployeeDTO::getLastname).setHeader("Last name");


        grid.addComponentColumn(employee -> {

            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");

            editButton.getStyle().set("background-color", "green")
                    .set("color", "white");

            deleteButton.getStyle().set("background-color", "red")
                    .set("color", "white");


            editButton.addClickListener(click -> {

                presenter.update(editButton, deleteButton, employee);
            });


            deleteButton.addClickListener(click -> {

                presenter.delete(editButton, deleteButton, employee.getId());


            });


            HorizontalLayout layout = new HorizontalLayout(editButton, deleteButton);
            return layout;
        }).setHeader("Actions");
    }

    @Override
    public void showNotification(String message, String style) {
        Notification notification = new Notification(message, 2000, Notification.Position.MIDDLE);
        notification.getElement().getThemeList().add(style);
        notification.open();
    }

    @Override
    public void onCreateGrid(List<EmployeeDTO> employees) {
        grid.removeAllColumns();
        createGrid(employees);
    }

    @Override
    public void reload() {
        UI.getCurrent().getPage().executeJs("setTimeout(() => location.reload(), 2000);");

    }

    @Override
    public void onEditEmployeeSpan(String info, String infoColor) {
        editEmployeeSpan.setText(info);
        editEmployeeSpan.getStyle().set("color", infoColor);
    }

    @Override
    public void onSetCreateTextFields(EmployeeDTO employee) {
        tckn.setValue(employee.getTckn());
        firstname.setValue(employee.getFirstname());
        lastname.setValue(employee.getLastname());


    }

    @Override
    public void onSetCleanTextField() {
        tckn.clear();
        firstname.clear();
        lastname.clear();
        binder.readBean(null);
    }


}



