package com.example.application;

import com.example.application.validation.impl.TCValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TCValidationTest {

	private TCValidator tcValidator;

	@BeforeEach
	void tcknValid() {

		tcValidator = new TCValidator();

	}

	@Test
	@DisplayName("Doğru girilen TC numarasını test etme")
	void tcknTrue() {
		assertThat(tcValidator.isValid("17969700100", null)).isTrue();
	}


	@Test
	@DisplayName("Null, girilmeyen TC numarasını test etme")
	void tcknNull() {
		assertThat(tcValidator.isValid(null, null)).isFalse();
	}

	@Test
	@DisplayName("Eksik karakter girilen TC numarasını test etme")
	void tcknMissing() {
		assertThat(tcValidator.isValid("12312", null)).isFalse();
	}

	@Test
	@DisplayName("Boş girilen TC numarasını test etme")
	void tcknEmpty() {
		assertThat(tcValidator.isValid("", null)).isFalse();
	}

	@Test
	@DisplayName("İlk rakamı sıfır girilen TC Numarasını test etme")
	void tcknFirstCharZero() {
		assertThat(tcValidator.isValid("07969700100", null)).isFalse();
	}
	
	@Test
	@DisplayName("Matematiksel olarak yanlış bir TC numarasını test etme")
	void tcknMathInvalid() {
		assertThat(tcValidator.isValid("17969700101", null)).isFalse();
	}

	@Test
	@DisplayName("Matematiksel olarak doğru bir TC numarasını test etme")
	void tcknMathValid() {
	    assertThat(tcValidator.isValid("17969700100", null)).isTrue();
	}
	
	
	

}
