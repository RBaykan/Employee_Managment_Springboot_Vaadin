package com.example.application.views.employee;

import com.example.application.controller.EmployeeController;
import com.example.application.dto.EmployeeDTO;

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
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;
import java.util.List;


@PageTitle("Employee")
@Route("employee")
@Menu(order = 1, icon = LineAwesomeIconUrl.EMPIRE)
public class EmployeeView extends VerticalLayout {


    // Employees Grid
    Span employeeListSpan = new Span("Employee List:");
    Span employeesShowStatus = new Span("All Employess data are listing");
    boolean isSearched = false;

    Grid<EmployeeDTO> grid = new Grid<>();

    TextField tckn = new TextField("TC ID");
    TextField firstname = new TextField("Firstname");
    TextField lastname = new TextField("Lastname");
    Long id = -1L;
    boolean isProcess = false;


    Span dataBaseConnectInfo;


    Span editEmployeeSpan = new Span("");
    BeanValidationBinder<EmployeeDTO> binder = new BeanValidationBinder<>(EmployeeDTO.class);


    public EmployeeView(final EmployeeController employeeController) {


        List<EmployeeDTO> employees;
        ResponseEntity<List<EmployeeDTO>> responseEntityEmployees = employeeController.getEmployees();

        if (responseEntityEmployees.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            // Eğer H2 Database'den veri döndürmezse memory tabanlı verilerle çalışılacaktır
            employees = new ArrayList<EmployeeDTO>();
            dataBaseConnectInfo = new Span(responseEntityEmployees.getStatusCode().toString() + ": Database'den veri çekilemedi!" +
                    "\nBellek tabanlı oluşturulan veriler ile çalışılacaktır");
            dataBaseConnectInfo.getStyle().set("color", "red");

        } else if (responseEntityEmployees.getStatusCode() == HttpStatus.NO_CONTENT) {
            // Eğer H2 Database'ye bağlanırsa ama veri döndürmezse
            employees = responseEntityEmployees.getBody();
            dataBaseConnectInfo = new Span(responseEntityEmployees.getStatusCode().toString() + ": Database bağlantısı kuruldu ama veriler çekilmedi. " +
                    "\n Bellek tabanlı oluşturulan veriler ile çalışılacaktır");
            dataBaseConnectInfo.getStyle().set("color", "green");
        } else if (responseEntityEmployees.getStatusCode() == HttpStatus.OK) {
            // Eğer H2 Database'den veri dönerse H2 Database'teki veriler ile çalışılacktır.
            employees = responseEntityEmployees.getBody();
            dataBaseConnectInfo = new Span(responseEntityEmployees.getStatusCode().toString() + ": Database bağlantısı kuruldu.");
            dataBaseConnectInfo.getStyle().set("color", "green");
        } else {
            dataBaseConnectInfo = new Span(responseEntityEmployees.getStatusCode() + ": Unknown Error!");
            dataBaseConnectInfo.getStyle().set("color", "red");
            employees = new ArrayList<EmployeeDTO>();
        }

        // Employee Search Form
        TextField searchTextField = new TextField("Employee Search");
        searchTextField.setPlaceholder("firstname, lastname or TC ID");
        searchTextField.getElement().executeJs(
                "this.querySelector('input').style.fontSize='12px';"
        );

        Button searchButton = new Button("Search", e -> {

            if (id == -2L) {
                showNotification(" Please Wait!", "error");
                return;
            }

            if (id != -1L) {
                showNotification(" Please complete other process", "error");
                return;
            }


            String searchedText = searchTextField.getValue();
            if (searchedText == null || searchedText.isEmpty() || searchedText.trim().isEmpty()) {
                showNotification("Please correct fill in the required fields", "error");
                employeesShowStatus.setText("No text searched");
                employeesShowStatus.getStyle().set("color", "red");


            } else {

                employeesShowStatus.setText("Searching " + searchedText);
                employeesShowStatus.getStyle().set("color", "gray");
                ResponseEntity<List<EmployeeDTO>> searchedEntity = employeeController.searchEmployee(searchedText);
                isSearched = true;


                if (searchedEntity.getStatusCode() == HttpStatus.OK) {

                    List<EmployeeDTO> searchedEmployeeList = searchedEntity.getBody();
                    grid.removeAllColumns();
                    employeesShowStatus.setText("Searched listening " + searchedText);
                    employeesShowStatus.getStyle().set("color", "gray");

                    createGrid(searchedEmployeeList, employeeController);
                }

                if (searchedEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {

                    showNotification(HttpStatus.BAD_REQUEST + ": Please fill in the required fields",
                            "error");

                }

            }


        });

        searchButton.getStyle().set("background-color", "#80669d")
                .set("color", "white");

        Button clearSearchButton = new Button("Clear/Refresh");
        clearSearchButton.getStyle().set("background-color", "#5dbea3")
                .set("color", "white");


        clearSearchButton.addClickListener(e -> {
            searchTextField.clear();

            if (isSearched) {


                grid.removeAllColumns();
                createGrid(employees, employeeController);

                isSearched = false;
            }

            employeesShowStatus.setText("All Employees data are listing");
            employeesShowStatus.getStyle().set("color", "black");


        });


        HorizontalLayout searchEmployeeForm = new HorizontalLayout(searchTextField, searchButton, clearSearchButton);
        searchEmployeeForm.setAlignSelf(Alignment.END, searchButton);
        searchEmployeeForm.setAlignSelf(Alignment.END, clearSearchButton);


        HorizontalLayout employeeListShowSpan = new HorizontalLayout(employeeListSpan, employeesShowStatus);
        createGrid(employees, employeeController);
        VerticalLayout employeesGrid = new VerticalLayout(employeeListShowSpan, grid, dataBaseConnectInfo);




        // ADD/Save Employee Form
        Span createFormSpan = new Span("Add/Edit Emplooyee");


        EmployeeDTO employeeDTO = new EmployeeDTO();


        binder.bindInstanceFields(this);
        binder.setBean(employeeDTO);


        Button savedButton = new Button("Save");
        savedButton.getStyle().set("background-color", "#4681f4")
                .set("color", "white");

        savedButton.addClickListener(e -> {

            if (id == -2L) {
                showNotification(" Please Wait!", "error");
                return;
            }


              BinderValidationStatus<EmployeeDTO> validationStatus = binder.validate();



            if (validationStatus.hasErrors()) {

                showNotification("Please fill in the required fields", "error");
            } else {
                // Create Employee
                if (id == -1L) {
                    ResponseEntity<EmployeeDTO> savedEmployeeResponseEntity
                            = employeeController.createEmployee(employeeDTO);

                    if (savedEmployeeResponseEntity.getStatusCode() == HttpStatus.CONFLICT) {
                        showNotification(HttpStatus.CONFLICT +
                                ": This TR ID No is registered in the system.", "error");

                    }
                    if (savedEmployeeResponseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        showNotification(HttpStatus.BAD_REQUEST +
                                ": Please fill in the required fields!", "error");


                    }
                    if (savedEmployeeResponseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                        showNotification(HttpStatus.INTERNAL_SERVER_ERROR +
                                ": Database could not be accessed.", "error");


                    }

                    if (savedEmployeeResponseEntity.getStatusCode() == HttpStatus.OK) {

                        showNotification(HttpStatus.OK +
                                ": Employee was saved successfully", "success");
                        savedButton.setVisible(false);
                        UI.getCurrent().getPage().executeJs("setTimeout(() => location.reload(), 2000);");

                        id = -2L;
                    }
                    // Edit Employee
                } else {


                    ResponseEntity<EmployeeDTO> updateEmployeeEmployeeResponseEntity
                            = employeeController.updateEmployee(id, employeeDTO);

                    if (updateEmployeeEmployeeResponseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
                        showNotification(HttpStatus.NOT_FOUND +
                                ": This TR ID No is not registered in the system.", "error");

                    }
                    if (updateEmployeeEmployeeResponseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        showNotification(HttpStatus.BAD_REQUEST +
                                ": Please fill in the required fields", "error");


                    }
                    if (updateEmployeeEmployeeResponseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {

                        showNotification(HttpStatus.INTERNAL_SERVER_ERROR +
                                ": Database could not be accessed", "error");

                    }

                    if (updateEmployeeEmployeeResponseEntity.getStatusCode() == HttpStatus.OK) {

                        showNotification(HttpStatus.OK +
                                ": Employee was updated successfully", "success");
                        savedButton.setVisible(false);
                        UI.getCurrent().getPage().executeJs("setTimeout(() => location.reload(), 2000);");
                        id = -2L;

                    }
                }


            }


        });

        HorizontalLayout createForm = new HorizontalLayout(tckn, firstname, lastname, savedButton);
        savedButton.getStyle().set("margin-top", "37px");
       // createForm.setAlignSelf(Alignment.END, savedButton);

        VerticalLayout createFormLayout = new VerticalLayout(createFormSpan, editEmployeeSpan, createForm);


        add(searchEmployeeForm, employeesGrid, createFormLayout);

    }


    private void showNotification(String message, String style) {
        Notification notification = new Notification(message, 2000, Notification.Position.MIDDLE);
        notification.getElement().getThemeList().add(style);
        notification.open();
    }

    private void createGrid(List<EmployeeDTO> employees, EmployeeController employeeController) {

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

                if (id == -2L) {
                    showNotification(" Please Wait!", "error");
                    return;
                }


                if (id == -1L) {
                    if (editButton.getText().equals("Cancel")) {
                        showNotification("Please refresh page", "error");
                    } else {
                        editButton.setText("Cancel");
                        editButton.getStyle().set("background-color", "blue")
                                .set("color", "white");
                        deleteButton.setVisible(false);
                        deleteButton.setVisible(false);
                        editEmployeeSpan.setText("ID of Employee selected for editing: " + employee.getId());
                        editEmployeeSpan.getStyle().set("color", "red");
                        tckn.setValue(employee.getTckn());
                        firstname.setValue(employee.getFirstname());
                        lastname.setValue(employee.getLastname());

                        id = employee.getId();
                    }
                } else {

                    if (editButton.getText().equals("Edit")) {
                        showNotification("Please complete other actions", "error");
                        return;
                    }

                    id = -1L;

                    editButton.setText("Edit");
                    editButton.getStyle().set("background-color", "green")
                            .set("color", "white");
                    deleteButton.setVisible(true);

                    editEmployeeSpan.setText("");
                    tckn.clear();
                    firstname.clear();
                    lastname.clear();
                    binder.readBean(null);

                }
            });


            deleteButton.addClickListener(click -> {

                if (id == -2L) {
                    showNotification("Please Wait!", "error");
                    return;
                }

                if (id != -1L) {
                    showNotification("Please complete other actions", "error");
                    return;
                }


                if (id == -1L) {
                    ResponseEntity<Boolean> isDeleted = employeeController.deleteEmployee(employee.getId());
                    if (isDeleted.getStatusCode() == HttpStatus.OK) {

                        if (isDeleted.getBody() != null && isDeleted.getBody()) {
                            deleteButton.setVisible(false);
                            editButton.setVisible(false);
                            showNotification(HttpStatus.OK +
                                    ": Employee was delete successfully", "success");
                            UI.getCurrent().getPage().executeJs("setTimeout(() => location.reload(), 2000);");

                        } else {

                            showNotification("Unkown Error", "error");
                            UI.getCurrent().getPage().executeJs("setTimeout(() => location.reload(), 2000);");


                        }

                    }

                    if (isDeleted.getStatusCode() == HttpStatus.NOT_FOUND) {

                        showNotification(HttpStatus.NOT_FOUND +
                                ": Employee not Found", "error");

                    }


                    if (isDeleted.getStatusCode() == HttpStatus.BAD_REQUEST) {

                        showNotification(HttpStatus.BAD_REQUEST +
                                " ID is not defined", "error");

                    }

                    if (isDeleted.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {

                        showNotification(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "error");


                    }
                    id = -2L;
                }


            });


            HorizontalLayout layout = new HorizontalLayout(editButton, deleteButton);
            return layout;
        }).setHeader("Actions");
    }


}
