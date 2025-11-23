package tech.ada.java.cursospring.api.usuario;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(description = "DTO para criação de usuário")
public class UsuarioDTO {
    @Schema(description = "UUID do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID uuid;

    @Schema(description = "Nome do usuário", example = "Usuário Exemplar")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "Email do usuário", example = "user@example.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Data de nascimento", example = "2000-01-01")
    @NotBlank(message = "Data de nascimento é obrigatória")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Data deve ter formato YYYY-MM-DD")
    private String dob;
}