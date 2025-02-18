package com.example.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.application.repository.EmployeeRepository;
import com.example.application.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.application.validation.impl.TCValidator;


@ExtendWith(MockitoExtension.class)
public class TCGeneratorTest {

    private TCValidator tcValidator;

    @Mock EmployeeRepository employeeRepository;


    private EmployeeServiceImpl employeeService;

    List<String> tc;
    @BeforeEach
    void tcknValidInit() {

        employeeService = new EmployeeServiceImpl(employeeRepository);

        tcValidator = new TCValidator();

        tc = new ArrayList<>();

        for(int i = 0; i<100; i++) {

            tc.add(createTCID());
        }



    }


    @Test
    void allCreatedRandomTCTrueTest() {
        for(var t : tc ) {

            assertThat(tcValidator.isValid(t, null)).isTrue();
        }
    }

    @Test
    void createDifferentTC(){
        for(int i = 0; i<10; i++) {
            String tc = createTCID();
            while(employeeService.isHaveSameTC(tc)){
                tc = createTCID();
            }
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



}
