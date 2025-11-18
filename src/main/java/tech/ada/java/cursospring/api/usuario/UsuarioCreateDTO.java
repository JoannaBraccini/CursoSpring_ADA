package tech.ada.java.cursospring.api.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para criação de usuário")
public class UsuarioCreateDTO {
    @Schema(description = "UUID do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String uuid;

    @Schema(description = "Nome do usuário", example = "Usuário Exemplar")
    private String nome;

    @Schema(description = "Email do usuário", example = "user@example.com")
    private String email;

    @Schema(description = "Data de nascimento", example = "2000-01-01")
    private String dob;

    // Getters e setters
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
