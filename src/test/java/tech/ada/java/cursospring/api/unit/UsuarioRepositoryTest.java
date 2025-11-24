package tech.ada.java.cursospring.api.unit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tech.ada.java.cursospring.api.usuario.Usuario;
import tech.ada.java.cursospring.api.usuario.UsuarioRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Salvar e buscar por UUID")
    void saveAndFindByUuid() {
        UUID uuid = UUID.randomUUID();
        Usuario u = new Usuario(uuid, "Teste Unit", "unit@example.com", LocalDate.of(1990, 1, 1));
        usuarioRepository.save(u);

        assertTrue(usuarioRepository.findByUuid(uuid).isPresent());
    }
}
