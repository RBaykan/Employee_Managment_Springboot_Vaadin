package com.example.application.controller;


import com.example.application.dto.EmployeeDTO;
import com.example.application.exception.EmployeeNotFoundException;
import com.example.application.exception.EmployeesListNull;
import com.example.application.exception.SearchedTextNull;
import com.example.application.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {


    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getEmployees() {
        List<EmployeeDTO> employees = new ArrayList<>();
        try {

            employees = employeeService.getEmployees();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(employees);

        } catch (Exception e) {


            if (e instanceof DataAccessException) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(employees);
            } else if (e instanceof NullPointerException) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(employees);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(employees);
            }


        }


    }

    @PostMapping
    @CacheEvict(value = "employees", allEntries = true)
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid EmployeeDTO dto) {

        try {
            EmployeeDTO employeeDTO = employeeService.createEmployee(dto);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(employeeDTO);
        } catch (Exception e) {

            if (e instanceof DataIntegrityViolationException) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(null);
            } else if (e instanceof HttpClientErrorException.BadRequest) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null);
            } else {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);

            }

        }
    }


    @DeleteMapping("/{id}")
    @CacheEvict(value = "employees", allEntries = true)
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable Long id) {

        try {
            Boolean isDeleted = employeeService.deleteEmployee(id);

            if (isDeleted) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.ok(false);
            }


        } catch (Exception e) {

            if (e instanceof EmployeeNotFoundException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(false);
            } else if (e instanceof IllegalArgumentException) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(false);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(false);
            }
        }


    }

    @PutMapping("/{id}")
    @CacheEvict(value = "employees", allEntries = true)
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id,
                                                      @Valid @RequestBody EmployeeDTO employeeDTO) {

        try {

            EmployeeDTO updateEmployee = employeeService.updateEmployee(id, employeeDTO);


            return ResponseEntity.status(HttpStatus.OK)
                    .body(updateEmployee);


        } catch (Exception e) {

            if (e instanceof EmployeeNotFoundException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            } else if (e instanceof IllegalArgumentException) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        }


    }


    @GetMapping("search/{id}")
    public ResponseEntity<List<EmployeeDTO>> searchEmployee(@RequestParam String searchText) {


        var responseEntity = getEmployees();

        if (responseEntity.getStatusCode() == HttpStatus.OK) {


            try {
                List<EmployeeDTO> listEmp = findEmployee(searchText, responseEntity.getBody());
                return ResponseEntity.ok()
                        .body(listEmp);
            } catch (Exception e) {

                if (e instanceof EmployeesListNull || e instanceof SearchedTextNull) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(null);
                }


            }

        }

        return ResponseEntity.status(responseEntity.getStatusCode())
                .body(responseEntity.getBody());


    }

    public boolean isHaveSameTC(String tc){

        return employeeService.isHaveSameTC(tc);
    }


    public List<EmployeeDTO> findEmployee(String searchedText, List<EmployeeDTO> empls) {

        if (empls == null) {
            throw new EmployeesListNull();
        }

        if (searchedText == null) {
            throw new SearchedTextNull();
        }

        if (searchedText.trim().isEmpty() || searchedText.isEmpty()) {
            return empls;
        }


        searchedText = searchedText.trim().replaceAll("[^a-zA-Z0-9çğıöşüÇĞİÖŞÜ ]", "").toLowerCase();


        String[] keywords = searchedText.split("\\s+");
        Pattern[] p = new Pattern[keywords.length];
        for (int i = 0; i < p.length; i++) {

            p[i] = Pattern.compile(Pattern.quote(keywords[i]));
        }

        List<EmployeeDTO> employeesSearched = new ArrayList<>();

        for (var employee : empls) {

            int find = 0;

            String firstname = employee.getFirstname().trim().replaceAll("[^a-zA-Z0-9çğıöşüÇĞİÖŞÜ ]", "").toLowerCase();
            boolean first = false;
            String lastName = employee.getLastname().trim().replaceAll("[^a-zA-Z0-9çğıöşüÇĞİÖŞÜ ]", "").toLowerCase();
            boolean last = false;
            String tckn = employee.getTckn().trim().replaceAll("[^a-zA-Z0-9çğıöşüÇĞİÖŞÜ ]", "").toLowerCase();
            boolean tc = false;

            for (int i = 0; i < p.length; i++) {


                if (!first && p[i].matcher(firstname).find()) {
                    find++;
                    first = true;

                }

                if (!last && p[i].matcher(lastName).find()) {
                    find++;
                    last = true;


                }

                if (!tc && p[i].matcher(tckn).find()) {
                    find++;
                    tc = true;


                }

            }

            if (find >= p.length) {
                employeesSearched.add(employee);
            }

        }
        return employeesSearched;

    }


}
