package edu.campus.library.domain;
import jakarta.validation.constraints.*;

public record NewBook(
        @NotBlank
        @Size(max = 200)
        String title,
        @NotBlank
        String author,
        @Pattern(regexp = "\\d{13}")
        String isbn
) { }