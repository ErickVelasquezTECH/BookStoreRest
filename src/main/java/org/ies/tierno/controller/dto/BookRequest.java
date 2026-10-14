package org.ies.tierno.controller.dto;

import jakarta.validation.constraints.*;

public record BookRequest(
        @NotBlank(message = "Requiere ISBN")
        @Pattern(regexp = "\\d{13}", message = "ISBN must have 13 digits")
        String isbn,

        @NotBlank(message = "Require titulo")
        @Size(max=200, message = "El titulo debe ser como maximo 200 caracteres")
        String title,

        @NotBlank(message = "Requiere un autor")
        @Size(max=100, message = "El nombre del autor debe ser como maximo 100 caracteres")

        String author,

        @NotNull(message = "Requiere un año de publicacion")
        @Min(value=1450, message = "Debe ser como minimo el año 1450")
        @Max(value = 2100,message = "Debe ser como maximo el año 2100")
        int publicationYear,


        @NotNull(message = "Requiere un numero de paginas")
        @Positive
        int pages
) {
}
