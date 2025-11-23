package tech.ada.java.cursospring.api.amizade;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import tech.ada.java.cursospring.api.usuario.UsuarioDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Schema(description = "DTO representando uma amizade entre dois usuários")
public class AmizadeDTO {

    @Schema(description = "ID da amizade (PK no banco)", example = "1")
    private Long id;

    @Schema(description = "Usuário A da amizade")
    private UsuarioDTO usuarioA;

    @Schema(description = "Usuário B da amizade")
    private UsuarioDTO usuarioB;

}
