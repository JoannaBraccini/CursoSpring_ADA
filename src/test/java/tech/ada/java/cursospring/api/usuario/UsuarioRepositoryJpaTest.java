package tech.ada.java.cursospring.api.usuario;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("postgres")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryJpaTest {

    @Autowired
    private UsuarioRepository repository;

    @Test
    @DisplayName("Encontrar por UUID")
    void findByUuid() {
        // cenário
        UUID uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        // ação
        Optional<Usuario> usuario = this.repository.findByUuid(uuid);
        // validação
        Assertions.assertTrue(usuario.isPresent());
        Assertions.assertEquals("João Silva", usuario.get().getNome());
    }
}
