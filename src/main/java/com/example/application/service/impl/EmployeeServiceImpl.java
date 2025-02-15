package com.example.application.service.impl;

import com.example.application.dto.EmployeeDTO;
import com.example.application.exception.EmployeeNotFoundException;
import com.example.application.mapper.EmployeeMapper;
import com.example.application.model.Employee;
import com.example.application.repository.EmployeeRepository;
import com.example.application.service.EmployeeService;
import javassist.NotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Cacheable(value = "employees")
    public List<EmployeeDTO> getEmployees() {
        System.out.println("çağrıldı");
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(
                EmployeeMapper::modelToDTO
        ).collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeByName(String name) {

return  null;

    }

    @Override
    public EmployeeDTO getEmployeeByTCKN() {
        return null;
    }

    @Override
    public Boolean deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);


        if (employee.isEmpty()) {
            throw new EmployeeNotFoundException();
        }

        employeeRepository.delete(employee.get());
        return true;


    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setTckn(employeeDTO.getTckn());
        employee.setFirstname(employeeDTO.getFirstname());
        employee.setLastname(employeeDTO.getLastname());

        Employee savedEmployee =  employeeRepository.save(employee);



        return EmployeeMapper.modelToDTO(savedEmployee);

    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Optional<Employee> employeeOP = employeeRepository.findById(id);

        if(employeeOP.isPresent()){
            Employee employee = employeeOP.get();
            employee.setTckn(employeeDTO.getTckn());
            employee.setFirstname(employeeDTO.getFirstname());
            employee.setLastname(employeeDTO.getLastname());

            Employee savedEmployee = employeeRepository.save(employee);
            return EmployeeMapper.modelToDTO(savedEmployee);

        }else {
            throw new EmployeeNotFoundException();
        }
    }





}
