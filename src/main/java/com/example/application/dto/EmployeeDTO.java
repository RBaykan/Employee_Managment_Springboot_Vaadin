package com.example.application.dto;

import com.example.application.validation.TCValid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDTO {

    private Long id;

    @TCValid
    private String tckn;

    @NotBlank(message = "Not Blank")
    private String firstname;
    @NotBlank(message = "Not Blank")
    private String lastname;
}
