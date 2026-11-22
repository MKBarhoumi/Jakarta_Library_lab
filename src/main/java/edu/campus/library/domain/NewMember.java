package edu.campus.library.domain;
import jakarta.validation.constraints.*;

public record NewMember(
        @NotBlank String name,
        @NotBlank
        @Email String email
) { }
