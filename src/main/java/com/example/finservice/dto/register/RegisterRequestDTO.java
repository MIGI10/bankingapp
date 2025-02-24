package com.example.finservice.dto.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Size(max = 128, message = "Password must be less than 128 characters long")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[!¡@#$%^&*(),.¿?\":;{}|<>=/_€-].*", message = "Password must contain at least one special character")
    @Pattern(regexp = "^[^\\s]*$", message = "Password cannot contain whitespace")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email: ${validatedValue}")
    private String email;
}
