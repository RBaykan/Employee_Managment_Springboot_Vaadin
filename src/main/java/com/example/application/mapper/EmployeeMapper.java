package com.example.application.mapper;

import com.example.application.dto.EmployeeDTO;
import com.example.application.model.Employee;

public class EmployeeMapper {

    public static EmployeeDTO modelToDTO(Employee model){

        return EmployeeDTO.builder()
                .id(model.getId())
                .tckn(model.getTckn())
                .firstname(model.getFirstname())
                .lastname(model.getLastname())
                .build();

    }

    public static Employee dtoToModel(EmployeeDTO model){

        return Employee.builder()
                .id(model.getId())
                .tckn(model.getTckn())
                .firstname(model.getFirstname())
                .lastname(model.getLastname())
                .build();

    }
}
