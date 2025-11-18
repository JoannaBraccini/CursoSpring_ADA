package tech.ada.java.cursospring.api.usuario;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id; // chave primária do banco de dados, não exposta na API

    private UUID uuid;
    @NotBlank // não pode ser nulo ou vazio
    private String nome;
    @Email // deve ser um email válido
    private String email;
    @Past // deve ser uma data no passado
    private LocalDate dob;

    public Usuario(UUID uuid, @NotBlank String nome, @Email String email, @Past LocalDate dob) {
        this.uuid = uuid;
        this.nome = nome;
        this.email = email;
        this.dob = dob;
    }
}
