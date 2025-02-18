package com.example.application.presenter.impl;

import com.example.application.controller.EmployeeController;
import com.example.application.dto.EmployeeDTO;
import com.example.application.presenter.EmployeePresenter;
import com.example.application.views.employee.ObserverEmployeeViewer;
import com.vaadin.flow.component.button.Button;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class EmployeePresenterImpl implements EmployeePresenter {

    Process process = Process.NO_PROCESS;
    private ObserverEmployeeViewer observerEmployeeViewer;
    private final EmployeeController employeeController;

    List<EmployeeDTO> employees = new ArrayList<>();
    EmployeeDTO employeeDTO = new EmployeeDTO();
    EmployeeDTO tempEmployeeDTO = new EmployeeDTO();

    String[] randomFirstNames;
    String[] randomLastNames;

    public EmployeePresenterImpl(EmployeeController employeeController) {
        this.employeeController = employeeController;

        addRandomNames();


    }




    @Override
    public void load() {
        employees = new ArrayList<>();
        ResponseEntity<List<EmployeeDTO>> responseEntityEmployees = employeeController.getEmployees();

        if (responseEntityEmployees.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            // Eğer H2 Database'den veri döndürmezse
            employees = new ArrayList<EmployeeDTO>();

            observerEmployeeViewer.onEmployeesLoad(employees, responseEntityEmployees.getStatusCode().toString() + ": Database'den veri çekilemedi!"
                    , "red");


        } else if (responseEntityEmployees.getStatusCode() == HttpStatus.NO_CONTENT) {
            // Eğer H2 Database'ye bağlanırsa ama veri döndürmezse
            employees = responseEntityEmployees.getBody();
            observerEmployeeViewer.onEmployeesLoad(employees, responseEntityEmployees.getStatusCode().toString()
                    + ": Database bağlantısı kuruldu ama veriler çekilmedi. " +
                    "\n Bellek tabanlı oluşturulan veriler ile çalışılacaktır", "red");


        } else if (responseEntityEmployees.getStatusCode() == HttpStatus.OK) {
            // Eğer H2 Database'den veri dönerse H2 Database'teki veriler ile çalışılacktır.
            employees = responseEntityEmployees.getBody();

            observerEmployeeViewer.onEmployeesLoad(employees, responseEntityEmployees.getStatusCode().toString()
                    + ": Database was connected.", "green");


        } else {


            employees = new ArrayList<EmployeeDTO>();
            observerEmployeeViewer.onEmployeesLoad(employees, responseEntityEmployees.getStatusCode() + ": Unknown Error!", "red");
        }

        process = Process.NO_PROCESS;
    }

    @Override
    public void add(EmployeeDTO employeeDTO) {

        if (process == Process.RELOAD) {
            observerEmployeeViewer.showNotification(" Please Wait!", "error");
            return;
        }

        // Create Employee
        if (process == Process.NO_PROCESS) {

            ResponseEntity<EmployeeDTO> savedEmployeeResponseEntity
                    = employeeController.createEmployee(employeeDTO);

            if (savedEmployeeResponseEntity.getStatusCode() == HttpStatus.CONFLICT) {
                observerEmployeeViewer.showNotification(HttpStatus.CONFLICT +
                        ": This TR ID No is registered in the system.", "error");

            }
            if (savedEmployeeResponseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
                observerEmployeeViewer.showNotification(HttpStatus.BAD_REQUEST +
                        ": Please fill in the required fields!", "error");


            }
            if (savedEmployeeResponseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                observerEmployeeViewer.showNotification(HttpStatus.INTERNAL_SERVER_ERROR +
                        ": Database could not be accessed.", "error");


            }

            if (savedEmployeeResponseEntity.getStatusCode() == HttpStatus.OK) {

                observerEmployeeViewer.showNotification(HttpStatus.OK +
                        ": Employee was saved successfully" +
                        "\r\n TC ID: " + employeeDTO.getTckn() +
                        "\r\n firstname: " + employeeDTO.getFirstname() +
                        "\r\n lastname: " + employeeDTO.getLastname(), "success");


                observerEmployeeViewer.reload();
                process = Process.RELOAD;
            }

        } else {

            if (process == Process.UPDATE) {
                ResponseEntity<EmployeeDTO> updateEmployeeEmployeeResponseEntity
                        = employeeController.updateEmployee(this.employeeDTO.getId(), this.employeeDTO);

                if (updateEmployeeEmployeeResponseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
                    observerEmployeeViewer.showNotification(HttpStatus.NOT_FOUND +
                            ": This TC ID No is not registered in the system.", "error");

                }
                if (updateEmployeeEmployeeResponseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    observerEmployeeViewer.showNotification(HttpStatus.BAD_REQUEST +
                            ": Please fill in the required fields", "error");


                }
                if (updateEmployeeEmployeeResponseEntity.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {

                    observerEmployeeViewer.showNotification(HttpStatus.INTERNAL_SERVER_ERROR +
                            ": Database could not be accessed", "error");

                }

                if (updateEmployeeEmployeeResponseEntity.getStatusCode() == HttpStatus.OK) {

                    observerEmployeeViewer.showNotification(HttpStatus.OK +
                            ": Employee was updated successfully", "success");

                    observerEmployeeViewer.reload();
                    process = Process.RELOAD;

                }
            }


        }


    }

    @Override
    public void update(Button edit, Button delete, EmployeeDTO employee) {
        if (process == Process.RELOAD) {
            observerEmployeeViewer.showNotification(" Please Wait!", "error");
            return;
        }


        if (process == Process.NO_PROCESS) {
            if (edit.getText().equals("Cancel")) {
                observerEmployeeViewer.showNotification("Please refresh page", "error");
            } else {
                this.employeeDTO = employee;
                edit.setText("Cancel");
                edit.getStyle().set("background-color", "blue")
                        .set("color", "white");
                delete.setVisible(false);
                observerEmployeeViewer.onEditEmployeeSpan("ID of Employee selected for editing: "
                        + employee.getId(), "red");
                observerEmployeeViewer.onSetCreateTextFields(employee);
                process = Process.UPDATE;
            }
        } else {

            if (process == Process.UPDATE) {
                observerEmployeeViewer.showNotification("Please complete other actions", "error");
                return;
            }

            process = Process.NO_PROCESS;
            this.employeeDTO = tempEmployeeDTO;
            edit.setText("Edit");
            edit.getStyle().set("background-color", "green")
                    .set("color", "white");
            delete.setVisible(true);
            observerEmployeeViewer.onSetCleanTextField();
            observerEmployeeViewer.onEditEmployeeSpan("", "black");


        }
    }

    @Override
    public void delete(Button edit, Button delete, Long emplooyeID) {
        if (process == Process.RELOAD) {
            observerEmployeeViewer.showNotification("Please Wait!", "error");
            return;
        }


        if (process == Process.UPDATE) {
            observerEmployeeViewer.showNotification("Please complete other actions", "error");
            return;
        }


        if (process == Process.NO_PROCESS) {
            ResponseEntity<Boolean> isDeleted = employeeController.deleteEmployee(emplooyeID);
            if (isDeleted.getStatusCode() == HttpStatus.OK) {

                if (isDeleted.getBody() != null && isDeleted.getBody()) {
                    delete.setVisible(false);
                    edit.setVisible(false);
                    observerEmployeeViewer.showNotification(HttpStatus.OK +
                            ": Employee was delete successfully", "success");
                    observerEmployeeViewer.reload();
                } else {

                    observerEmployeeViewer.showNotification("Unkown Error", "error");
                    observerEmployeeViewer.reload();

                }

            }

            if (isDeleted.getStatusCode() == HttpStatus.NOT_FOUND) {

                observerEmployeeViewer.showNotification(HttpStatus.NOT_FOUND +
                        ": Employee not Found", "error");

            }


            if (isDeleted.getStatusCode() == HttpStatus.BAD_REQUEST) {

                observerEmployeeViewer.showNotification(HttpStatus.BAD_REQUEST +
                        " ID is not defined", "error");

            }

            if (isDeleted.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {

                observerEmployeeViewer.showNotification(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "error");


            }
            process = Process.RELOAD;
        }
    }

    public boolean isSearched = false;

    @Override
    public void search(String searchedText) {

        if (process != Process.NO_PROCESS) {
            return;
        }

        if (searchedText == null || searchedText.isEmpty() || searchedText.trim().isEmpty()) {
            observerEmployeeViewer.showNotification("Please correct fill in the required fields", "error");
            observerEmployeeViewer.onEmployeeSearch("No text searched", "red");


        } else {

            observerEmployeeViewer.onEmployeeSearch("Searching: " + searchedText,
                    "gray");
            ResponseEntity<List<EmployeeDTO>> searchedEntity = employeeController.searchEmployee(searchedText);
            isSearched = true;


            if (searchedEntity.getStatusCode() == HttpStatus.OK) {

                List<EmployeeDTO> searchedEmployeeList = searchedEntity.getBody();

                observerEmployeeViewer.onEmployeeSearch("Searched listening " + searchedText,
                        "gray");
                observerEmployeeViewer.onCreateGrid(searchedEmployeeList);


            }

            if (searchedEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {

                observerEmployeeViewer.showNotification(HttpStatus.BAD_REQUEST + ": Please fill in the required fields",
                        "error");

            }

        }

    }

    @Override
    public void clearSearch() {

        if (process != Process.NO_PROCESS) {
            return;
        }


        if (isSearched) {


            observerEmployeeViewer.onCreateGrid(employees);


            isSearched = false;
        }

        observerEmployeeViewer.onEmployeeSearch("All Employees data are listing", "black");


    }

    @Override
    public void setObserver(ObserverEmployeeViewer observerEmployeeViewer) {
        this.observerEmployeeViewer = observerEmployeeViewer;
    }

    @Override
    public void addNew() {

        if (process == Process.RELOAD) {
            observerEmployeeViewer.showNotification(" Please Wait!", "error");
            return;
        }

        if(process == Process.NO_PROCESS){
            Random rand = new Random();

            employeeDTO.setFirstname(randomFirstNames[rand.nextInt(0, randomFirstNames.length)]);
            employeeDTO.setLastname(randomLastNames[rand.nextInt(0, randomLastNames.length)]);
            employeeDTO.setTckn(createTCID());
            while(employeeController.isHaveSameTC(employeeDTO.getTckn())){
                employeeDTO.setTckn(createTCID());
            }

            add(employeeDTO);
            process = Process.RELOAD;
        }else {
            observerEmployeeViewer.showNotification(" Please Complete other actions!", "error");
        }


    }


    String createTCID() {


        Random rand = new Random();
        int random_pool1 = rand.nextInt(99999 - 10000 + 1) + 10000;
        int random_pool2 = rand.nextInt(9999 - 1000 + 1) + 1000;

        String random_pool1_str = String.valueOf(random_pool1);
        String random_pool2_str = String.valueOf(random_pool2);

        int k1_sum = 0;
        for (char c : random_pool1_str.toCharArray()) {
            k1_sum += Character.getNumericValue(c);
        }

        int k2_sum = 0;
        for (char c : random_pool2_str.toCharArray()) {
            k2_sum += Character.getNumericValue(c);
        }

        StringBuilder tckn = new StringBuilder();
        for (int i = 0; i < random_pool2_str.length(); i++) {
            tckn.append(random_pool1_str.charAt(i)).append(random_pool2_str.charAt(i));
        }

        int digit_10 = (k1_sum * 7 - k2_sum) % 10;
        int digit_11 = (k1_sum + k2_sum + digit_10) % 10;

        return tckn.toString() + random_pool1_str.charAt(random_pool1_str.length() - 1) + digit_10 + digit_11;

    }

    void addRandomNames(){

        randomFirstNames = new String[]{"Ahmet", "Ayşe","Ali", "Fatma"
                ,"Mehmet", "Zeynep", "Kadir", "Emine", "Hasan", "Buse"};

        randomLastNames = new String[]{"Kılıç", "Koç", "Yeşil", "Yurt", "Ercan"
                ,"Baş", "Kaş", "Tek", "Yılmaz", "Yılan"};
    }




}


enum Process {
    ADD, UPDATE, DELETE, SEARCH, RELOAD, NO_PROCESS
}
