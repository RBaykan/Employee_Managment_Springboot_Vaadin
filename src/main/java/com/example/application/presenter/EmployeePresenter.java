package com.example.application.presenter;

import com.example.application.dto.EmployeeDTO;
import com.example.application.views.employee.EmployeeView;
import com.example.application.views.employee.ObserverEmployeeViewer;
import com.vaadin.flow.component.button.Button;
import org.springframework.stereotype.Component;


public interface EmployeePresenter {

    void load();
    void add(EmployeeDTO dto);
    void update(Button edit, Button delete, EmployeeDTO employee);
    void delete(Button edit, Button delete, Long emplooyeID);
    void search(String searchedText);
    void clearSearch();

    void setObserver(ObserverEmployeeViewer observerEmployeeViewer);

    void addNew();

}
    