package tech.ada.java.cursospring.api;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {
    private UUID uuid;
    @NotBlank // não pode ser nulo ou vazio
    private String nome;
    @Email // deve ser um email válido
    private String email;
    @Past // deve ser uma data no passado
    private LocalDate dob;
}
