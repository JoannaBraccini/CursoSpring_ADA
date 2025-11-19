package tech.ada.java.cursospring.api.amizade;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tech.ada.java.cursospring.api.usuario.Usuario;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Amizade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_a")
    private Usuario usuarioA;

    @ManyToOne
    @JoinColumn(name = "usuario_b")
    private Usuario usuarioB;

    public Amizade(Usuario usuarioA, Usuario usuarioB) {
        this.usuarioA = usuarioA;
        this.usuarioB = usuarioB;
    }

}
