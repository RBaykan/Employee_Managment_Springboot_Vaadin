package com.example.application.service;

import com.example.application.dto.EmployeeDTO;


import java.util.List;


public interface EmployeeService {

    List<EmployeeDTO> getEmployees();

    EmployeeDTO getEmployeeByName(String name);
    EmployeeDTO getEmployeeByTCKN();
    Boolean deleteEmployee(Long id);

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);




}
