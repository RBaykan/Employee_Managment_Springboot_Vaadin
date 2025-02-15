package com.example.application;




import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.application.dto.EmployeeDTO;
import jakarta.validation.Validator;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;



public class EmployeeDtoTest {
	
	private static Validator validator;
	
	@BeforeAll
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = (Validator) factory.getValidator();
	}
	
	
	@Test
	@DisplayName("Doğru girilmiş TC ile oluşturulmuş bir DTO")
	void testEmployeeCreationWithValidTCKN() {
		
		EmployeeDTO dto = new EmployeeDTO(1L, "78044796128" ,"Ali", "Koç");
		 
		Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(dto);
	
		assertTrue(violations.isEmpty());
		
		
	}
	
	@Test
	@DisplayName("Yanlış girilmiş TC ile oluşturulmuş bir DTO")
	void testEmployeeCreationWithNotValidTCKN() {
		
		EmployeeDTO dto = new EmployeeDTO(1L, "78044796127" ,"Ali", "Koç");
		 
		Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(dto);
		
		
		assertFalse(violations.isEmpty());
		
		
	}

}
