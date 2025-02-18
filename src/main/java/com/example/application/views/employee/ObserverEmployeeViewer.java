package com.example.application.views.employee;

import com.example.application.dto.EmployeeDTO;

import java.util.List;

public interface ObserverEmployeeViewer {

    void onEmployeesLoad(List<EmployeeDTO> employees,
                         String info,
                         String infoColor);
    void onEmployeeAdd();
    void onEmployeeDelete();
    void onEmployeeUpdate();
    void onEmployeeSearch(String info, String infoColor);

    void showNotification(String message, String style);
    void onCreateGrid(List<EmployeeDTO> employees);

    void reload();

    void onEditEmployeeSpan(String info, String infoColor);

    void onSetCreateTextFields(EmployeeDTO employee);
    void onSetCleanTextField();
}
